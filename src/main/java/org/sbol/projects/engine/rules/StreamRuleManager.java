package org.sbol.projects.engine.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import org.sbol.projects.engine.constants.Channel;
import org.sbol.projects.engine.rules.annotations.Rule;
import org.sbol.projects.engine.utils.S3ClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Ejecutor de reglas de negocio basadas en Java Lambdas y su aplicación sobre la lista de productos resultantes.
 *
 * @param <P> Item type
 * @author david.ralluy
 *
 */
public abstract class StreamRuleManager<P> implements RuleManager<P> {

    private static final String TEST_BUCKET_NAME = "mng-config/test/shop";

    private Map<Class<?>, Map<String, BusinessRule<P>>> rulesMap = new HashMap<>();

    private Map<Class<?>, Map<Channel, StreamRule<P>>> rulesCompositeMap = new HashMap<>();

    private Map<Class<?>, Map<Channel, List<BusinessRule<P>>>> rulesAppliedMap = new HashMap<>();

    @Autowired
    private ApplicationContext context;

    protected ApplicationContext getContext() {
        return this.context;
    }

    @Autowired
    private CriterionRuleFactory<P> criterionRuleFactory;

    private CountDownLatch startSignal = null;

    private boolean recarga = false;

    @Override
    public Map<String, BusinessRule<P>> getRules() {
        if (!this.rulesMap.containsKey(this.getTargetClass())) {
            this.loadRules(null);
        }
        return this.rulesMap.get(this.getTargetClass());
    }

    /**
     * Get rule composite.
     *
     * @return Rules map for a channel
     */
    public Map<Channel, StreamRule<P>> getRulesComposite() {
        return this.rulesCompositeMap.get(this.getTargetClass());
    }

    /**
     * Get the rules applied.
     *
     * @return Rules map applied
     */
    public Map<Channel, List<BusinessRule<P>>> getRulesApplied() {
        return this.rulesAppliedMap.get(this.getTargetClass());
    }

    /**
     * Get the rules applied for a channel.
     *
     * @param channel Channel
     *
     * @return Rules map applied
     */
    public List<BusinessRule<P>> getRulesApplied(final Channel channel) {
        return this.rulesAppliedMap.get(this.getTargetClass()).get(channel);
    }

    @Override
    public List<P> executeRules(final List<P> productos, final Channel channel) {

        if (!this.rulesCompositeMap.containsKey(this.getTargetClass())) {
            this.loadRules(null);
        }

        if (this.recarga) {
            try {
                this.startSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (this.getRulesComposite().get(channel) != null) {
            return this.getRulesComposite().get(channel).apply(productos.stream()).collect(Collectors.toList());
        }
        return productos;
    }

    /**
     * Devuelve la clase sobre la que aplicar las reglas el StreamRuleManager
     * con el fin de poder distinguir dentro del contexto de Spring entre reglas
     * que aplican a diferentes clases.
     *
     * @return
     */
    protected abstract Class<P> getTargetClass();

    /**
     * Recupera las BusinessRules del contexto de Spring que apliquen a a la
     * clase que se recibe como parámetro.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, BusinessRule<P>> getRulesFromSpringContext() {
        Map<String, BusinessRule<P>> result = new HashMap<>();

        Map<String, Object> mapTemp = this.getContext().getBeansWithAnnotation(Rule.class);
        for (String key : mapTemp.keySet()) {
            Object currentEntry = mapTemp.get(key);
            Rule ruleAnnotation = currentEntry.getClass().getAnnotation(Rule.class);
            if (ruleAnnotation.type() == this.getTargetClass()) {
                result.put(((BusinessRule<P>) currentEntry).getName(), (BusinessRule<P>) currentEntry);
            }
        }

        return result;
    }

    /**
     * Cargar automáticamente todas las reglas del tipo BusinessRule.
     * Posteriomente se filtran las que estén activas y ordenadas por prioridad.
     *
     * El resultado final es la composición de todas las reglas de negocio por canal dentro de una única regla por
     * canal, la cual
     * se aplicará al conjunto resultado.
     *
     */
    @Override
    public void loadRules(final String ruleFile) {

        if (!this.rulesMap.containsKey(this.getTargetClass())) {
            this.rulesMap.put(this.getTargetClass(),
                    // Via Spring obtenemos todas las clases con anotacion Rule cuyo type coincida con el clase
                    // gestionado por una instancia concreta del StreamRuleManager
                    // (p.e. el StremaRuleManagerProductCatalog trata con reglas aplicables sobre ProductCatalog). De
                    // aqui obtenemos una lista inicial de BusinessRule que encapsula
                    // su respectiva StreamRule (que es la que aplica las reglas de negocio en si sobre los productos).
                    this.getRulesFromSpringContext());
            this.rulesCompositeMap.put(this.getTargetClass(), new HashMap<Channel, StreamRule<P>>());
            this.rulesAppliedMap.put(this.getTargetClass(), new HashMap<Channel, List<BusinessRule<P>>>());
        }

        // Agrupamos las reglas por canal
        Map<Channel, List<BusinessRule<P>>> reglasCanales = this
            .getRules()
            .values()
            .stream()
            .collect(Collectors.groupingBy(BusinessRule::getChannel));

        for (Entry<Channel, List<BusinessRule<P>>> reglasCanal : reglasCanales.entrySet()) {

            // Ordenamos por prioridad y seleccionamos las activas.
            List<BusinessRule<P>> reglas = reglasCanal
                .getValue()
                .stream()
                .filter(p -> p.isEnabled())
                .sorted((p1, p2) -> Integer.compare(p2.getPriority(), p1.getPriority()))
                .collect(Collectors.toList());

            for (BusinessRule<P> businessRule : reglas) {
                // Este metodo agrupa (dentro de CriterioRuleFactory) todas las StreamRule encapsuladas en cada
                // BusinessRule por canal.
                // Esta agrupacion por canal se exploatra después en el método build
                this.criterionRuleFactory.fromCriterion(businessRule.getRule(), reglasCanal.getKey(),
                        this.getTargetClass());
            }

            if (!reglas.isEmpty()) {
                this.getRulesComposite().put(reglasCanal.getKey(),
                        this.criterionRuleFactory.build(reglasCanal.getKey(), this.getTargetClass()));
                this.getRulesApplied().put(reglasCanal.getKey(), reglas);
            }
        }
    }

    @Override
    public void removeRule(final String ruleName) {
        this.getRules().remove(ruleName);
        this.criterionRuleFactory.clearRules();
        this.loadRules(null);
    }

    @Override
    public void addRule(final String ruleCollection, final String ruleName) {

        @SuppressWarnings("unchecked")
        BusinessRule<P> rule = (BusinessRule<P>) this.loadRuleFromS3(ruleName);

        if (rule != null) {
            this.startSignal = new CountDownLatch(1);
            this.recarga = true;
            this.getRules().put(rule.getName(), rule);
            this.criterionRuleFactory.clearRules();
            this.loadRules(null);
            this.recarga = false;
            this.startSignal.countDown();
        }
    }

    @Override
    public void clearRules() {
        this.getRules().clear();
        this.getRulesComposite().clear();
    }

    @Override
    public void activateRule(final String ruleCollection, final String ruleName, final boolean active) {
        if (this.getRules().containsKey(ruleName)) {
            this.getRules().get(ruleName).setEnabled(active);
            this.criterionRuleFactory.clearRules();
            this.loadRules(null);
        }
    }

    @Override
    public void updateRulePriority(final String ruleCollection, final String ruleName, final int priority) {
        if (this.getRules().containsKey(ruleName)) {
            this.getRules().get(ruleName).setPriority(priority);
            this.criterionRuleFactory.clearRules();
            this.loadRules(null);
        }
    }

    @Override
    public void updateRule(final String ruleCollection, final String ruleName, final Map<String, Object> parameters) {
        if (this.getRules().containsKey(ruleName)) {
            this.getRules().get(ruleName).updateParameters(parameters);
        }
    }

    private Object loadRuleFromS3(final String ruleName) {

        ClassLoader classLoaderCurrent = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = new S3ClassLoader(StreamRuleManager.TEST_BUCKET_NAME,
                Region.getRegion(Regions.EU_WEST_1), classLoaderCurrent);

        // Obtener la clase
        Class<?> ruleClass = null;
        try {
            ruleClass = classLoader.loadClass(ruleName);
            return ruleClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // Assert.fail(e.getMessage());
        }

        return null;

    }

}
