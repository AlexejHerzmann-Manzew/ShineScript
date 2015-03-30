package org.sparkle.sscript;

/**
 *
 * @author Yew_Mentzaki
 */
public class Func extends Element{
    public static boolean check(String element){
        return (element.substring(0, 3).equals("func"));
    }
}