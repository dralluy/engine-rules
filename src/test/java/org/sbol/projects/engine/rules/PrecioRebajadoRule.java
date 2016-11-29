package org.sbol.projects.engine.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Test discounted price rule.
 *
 * @author david.ralluy
 *
 */
public class PrecioRebajadoRule extends BusinessRule<ProductCatalog> {

    private static final int PRICE_99 = 99;
    private static final int PRICE_100 = 100;

    /**
     * Rule name.
     */
    public PrecioRebajadoRule() {
        super("precioRebajadoRule");
    }

    /**
     * Regla que fija el precio a 99 para precios mayores que 100.
     */
    @Override
    public StreamRule<ProductCatalog> defineRule() {
        return s -> s.peek(p -> {
            if (p.getPrecio() > ((Integer) this.getParameters().get("precio")).intValue()) {
                p.setPrecio(PrecioRebajadoRule.PRICE_99);
            }
        });
    }

    @Override
    public Map<String, Object> defineParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("precio", new Integer(PrecioRebajadoRule.PRICE_100));
        return params;
    }

    @Override
    public Function<ProductCatalog, ProductCatalog> defineTransformation() {
        return null;
    }

    @Override
    protected ProductCatalog internalProcess(final ProductCatalog context) {
        return null;
    }
}
