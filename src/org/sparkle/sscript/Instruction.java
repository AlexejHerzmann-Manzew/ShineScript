package org.sparkle.sscript;

/**
 *
 * @author Yew_Mentzaki
 */
public abstract class Instruction extends Element{
    public abstract void exec(VarSet vars, Script script) throws Exception;
}
