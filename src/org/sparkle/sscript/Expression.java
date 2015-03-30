package org.sparkle.sscript;

/**
 *
 * @author Yew_Mentzaki
 */
public class Expression extends Instruction{
    public static boolean check(String element){
        return (element.substring(0, 4).equals("event"));
    }
    Var to;
    String exp;

    public Expression(String exp) {
        this.exp = exp;
    }
    @Override
    public void exec(VarSet vars, Script script) throws Exception {
        to.value = new Value(exp, vars);
    }
}
