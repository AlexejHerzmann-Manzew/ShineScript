package org.sparkle.jbind;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yew_mentzaki
 */
public class Reader {
    public static JBinD read(byte[] bytes){
        JBinD bind = new JBinD();
        int caret = 0;
        while(caret < bytes.length){
            try {
                
            byte[] b = new byte[4];
            for (int i = 0; i < 4; i++) {
                b[i] = bytes[caret];
                caret++;
            }
            int length = b[0] << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | (b[3] & 0xFF);
            b = new byte[length];
            for (int i = 0; i < length; i++) {
                b[i] = bytes[caret];
                caret++;
            }
            String title = new String(b);
            b = new byte[4];
            for (int i = 0; i < 4; i++) {
                b[i] = bytes[caret];
                caret++;
            }
            length = b[0] << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | (b[3] & 0xFF);
            b = new byte[length];
            for (int i = 0; i < length; i++) {
                b[i] = bytes[caret];
                caret++;
            }
            try {
                bind.addPart(new Part(title, b));
            } catch (JBinDException ex) {
                Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            } catch (OutOfMemoryError e) {
                return bind;
            }
        }
        return bind;
    }
}
