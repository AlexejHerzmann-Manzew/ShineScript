package org.sparkle.jcfg;

/*
 * Copyright (C) 2015 yew_mentzaki
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 *
 * @author yew_mentzaki
 */
public class Parameter {

    private String name;
    private Object value;

    public Parameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean getValueAsBoolean() {
        String value = this.value.toString().toLowerCase();
        return (value.equals("true")
                || value.equals("yes")
                || value.equals("enabled")
                || value.equals("1")
                || value.equals("1.0"));
    }

    public double getValueAsDouble() {
        try {
            return Double.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getValueAsString() {
        return value.toString();
    }

    public int getValueAsInteger() {
        try {
            return Double.valueOf(value.toString()).intValue();
        } catch (NumberFormatException e) {
            return 0;
        } catch (NullPointerException e) {
            return 0;
        }
    }

}
