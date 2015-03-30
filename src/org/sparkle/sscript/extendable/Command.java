package org.sparkle.sscript.extendable;

/**
 *
 * @author Yew_Mentzaki
 */
public abstract class Command {
    public final String name;
    public Command(String name) {
        this.name = name;
    }
    public abstract void exec(Object... args) throws Exception;
}
