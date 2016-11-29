package org.sbol.projects.engine.rules;

import java.util.Map;
import java.util.function.Function;

/**
 * Test discounted price without parameters.
 *
 * @author david.ralluy
 *
 */
public class PrecioRebajadoNoParametersRule extends BusinessRule<ProductCatalog> {

    private static final int PRICE_99 = 99;

    /**
     * Rule name.
     */
    public PrecioRebajadoNoParametersRule() {
        super("precioRebajadoRule");
    }

    /**
     * Regla que fija el precio a 99 para precios mayores que 100.
     */
    @Override
    public StreamRule<ProductCatalog> defineRule() {
        return s -> s.peek(p -> {
            if (p.getPrecio() > ((Integer) this.getParameters().get("precio")).intValue()) {
                p.setPrecio(PrecioRebajadoNoParametersRule.PRICE_99);
            }
        });
    }

    @Override
    public Map<String, Object> defineParameters() {
        return null;
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
