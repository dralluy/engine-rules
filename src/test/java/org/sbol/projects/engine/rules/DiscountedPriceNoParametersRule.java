package org.sbol.projects.engine.rules;

import java.util.Map;
import java.util.function.Function;

/**
 * Test discounted price without parameters.
 *
 * @author david.ralluy
 *
 */
public class DiscountedPriceNoParametersRule extends BusinessRule<ItemCollection> {

    private static final int PRICE_99 = 99;

    /**
     * Rule name.
     */
    public DiscountedPriceNoParametersRule() {
        super("precioRebajadoRule");
    }

    /**
     * Regla que fija el precio a 99 para precios mayores que 100.
     */
    @Override
    public StreamRule<ItemCollection> defineRule() {
        return s -> s.peek(p -> {
            if (p.getPrecio() > ((Integer) this.getParameters().get("precio")).intValue()) {
                p.setPrecio(DiscountedPriceNoParametersRule.PRICE_99);
            }
        });
    }

    @Override
    public Map<String, Object> defineParameters() {
        return null;
    }

    @Override
    public Function<ItemCollection, ItemCollection> defineTransformation() {
        return null;
    }

    @Override
    protected ItemCollection internalProcess(final ItemCollection context) {
        return null;
    }
}
