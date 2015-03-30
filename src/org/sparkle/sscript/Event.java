package org.sparkle.sscript;

/**
 *
 * @author Yew_Mentzaki
 */
public class Event extends Element{
    String name; String tags[] = new String[0];

    public Event(String name, Block block) {
        super(block);
        this.name = name;
    }
    
    public static boolean check(String element){
        return (element.substring(0, 4).equals("event"));
    }
}
