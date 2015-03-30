package org.sparkle.sscript;

import java.util.ArrayList;

/**
 *
 * @author Yew_Mentzaki
 */
public class VarSet {

    public VarSet parent;
    public ArrayList<Var> vars = new ArrayList<Var>();

    public VarSet getSubset(){
        VarSet child = new VarSet();
        child.parent = this;
        return child;
    }
    
    public Var getVariable(String name) {
        VarSet vs = this;
        while (vs != null) {
            Var var = vs.setVariable(name);
            if (var != null) {
                return var;
            } else {
                vs = vs.parent;
            }
        }
        return null;
    }

    public Var setVariable(String name) {
        return null;
    }

    private Var getVar(String name) {
        int i = -1;
        while (++i < vars.size()) {
            Var var = vars.get(i);
            if (var.name.equals(var))return var;
        }
        return null;
    }
}
