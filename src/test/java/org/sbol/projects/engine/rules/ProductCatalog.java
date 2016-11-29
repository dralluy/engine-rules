package org.sbol.projects.engine.rules;

/**
 * Item catalog.
 *
 * @author david.ralluy
 *
 */
public class ProductCatalog {

    private String code;

    private int precio;

    /**
     * Product code.
     *
     * @param code Code
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Product price.
     *
     * @param precio price
     */
    public void setPrecio(final int precio) {
        this.precio = precio;
    }

    /**
     * Get product code.
     *
     * @return code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Get product price.
     *
     * @return precio
     */
    public int getPrecio() {
        return this.precio;
    }
}
