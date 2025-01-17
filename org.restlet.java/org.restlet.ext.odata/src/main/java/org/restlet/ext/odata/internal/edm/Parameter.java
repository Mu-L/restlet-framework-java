/**
 * Copyright 2005-2024 Qlik
 * 
 * The contents of this file is subject to the terms of the Apache 2.0 open
 * source license available at http://www.opensource.org/licenses/apache-2.0
 * 
 * Restlet is a registered trademark of QlikTech International AB.
 */

package org.restlet.ext.odata.internal.edm;

/**
 * Represents a parameter used when invoking a {@link FunctionImport}.
 * 
 * @author Thierry Boileau
 * @see <a
 *      href="http://msdn.microsoft.com/en-us/library/cc716710.aspx">FunctionImport
 *      Element (CSDL)</a>
 */
public class Parameter extends NamedObject {

    /** The maximum length of the parameter value. */
    private int maxLength;

    /** The mode of the parameter among "In", "Out", and "InOut". */
    private String mode;

    /** The precision of the parameter value. */
    private int precision;

    /** The scale of the parameter value. */
    private int scale;

    /** The type of the parameter. */
    private String type;

    /**
     * Constructor.
     * 
     * @param name
     *            The name of the parameter.
     */
    public Parameter(String name) {
        super(name);
    }

    /**
     * Returns the maximum length of the parameter value.
     * 
     * @return The maximum length of the parameter value.
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Returns the mode of the parameter among "In", "Out", and "InOut".
     * 
     * @return The mode of the parameter among "In", "Out", and "InOut".
     */
    public String getMode() {
        return mode;
    }

    /**
     * Returns the precision of the parameter value.
     * 
     * @return The precision of the parameter value.
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Returns the scale of the parameter value.
     * 
     * @return The scale of the parameter value.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Returns the type of the parameter.
     * 
     * @return The type of the parameter.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the maximum length of the parameter value.
     * 
     * @param maxLength
     *            The maximum length of the parameter value.
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Sets the mode of the parameter.
     * 
     * @param mode
     *            The mode of the parameter.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Sets the precision of the parameter value.
     * 
     * @param precision
     *            The precision of the parameter value.
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Sets the scale of the parameter value.
     * 
     * @param scale
     *            The scale of the parameter value.
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * Sets the type of the parameter.
     * 
     * @param type
     *            The type of the parameter.
     */
    public void setType(String type) {
        this.type = type;
    }

}
