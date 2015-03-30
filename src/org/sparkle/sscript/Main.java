package org.sparkle.sscript;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yew_mentzaki
 */
public class Main {
    public static void main(String[] args) {
        Script script = new Script(new File("script/Program.s"));
        try {
            script.start();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
