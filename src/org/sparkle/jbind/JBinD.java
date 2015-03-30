package org.sparkle.jbind;

import java.util.ArrayList;

/**
 *
 * @author yew_mentzaki
 */
public class JBinD {

    private ArrayList<Part> parts = new ArrayList<Part>();

    public void addPart(Part part) throws JBinDException {
        if (part == null) {
            throw new JBinDException("That part is empty");
        }
        if (containsPart(part.getTitle())){
            throw new JBinDException("That JBinD is early contains part titled as \"" + part.getTitle() + "\"");
        }
        parts.add(part);
    }

    public void removePart(Part part) {
        parts.remove(part);
    }

    public void removePart(String title) {
        parts.remove(getPart(title));
    }
    
    public boolean containsPart(String title) {
        return getPart(title) != null;
    }

    public Part getPart(String title) {
        for (Part part : parts) {
            if (part.getTitle().equals(title)) {
                return part;
            }
        }
        return null;
    }
    
    public Part[] getAllParts(){
        Part[] parts = new Part[this.parts.size()];
        for(int i = 0; i < parts.length; i++){
            parts[i] = this.parts.get(i);
        }
        return parts;
    }
}
