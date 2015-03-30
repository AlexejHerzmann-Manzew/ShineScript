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

package org.sparkle.jcfg;

import java.util.ArrayList;

/**
 *
 * @author yew_mentzaki
 */
public class JCFG {

    private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

    public JCFG() {
    }

    public Parameter[] list(){
        Parameter[] parameter = new Parameter[parameters.size()];
        for (int i = 0; i < parameter.length; i++) {
            parameter[i] = parameters.get(i);
        }
        return parameter;
    }
    
    public boolean contains(String name) {
        int i = 0;
        while (i < parameters.size()) {
            if (parameters.get(i).getName().equals(name)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public Parameter get(String name) {
        int i = 0;
        while (i < parameters.size()) {
            if (parameters.get(i).getName().equals(name)) {
                return parameters.get(i);
            }
            i++;
        }
        return new Parameter(name, null);
    }

    public void add(Parameter param) {
        if (param != null) {
            if (!contains(param.getName())) {
                parameters.add(param);
            }
        }
    }

    public void set(Parameter param) {
        if (param != null) {
            if (!contains(param.getName())) {
                parameters.add(param);
            } else {
                get(param.getName()).setValue(param.getValue());
            }
        }
    }

    public void set(String name, Object value) {
        set(new Parameter(name, value));
    }
    
}
