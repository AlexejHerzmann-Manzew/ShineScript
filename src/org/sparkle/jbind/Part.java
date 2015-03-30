package org.sparkle.jbind;

import java.nio.ByteBuffer;
import org.sparkle.jcfg.JCFG;
import org.sparkle.jcfg.Parser;
import org.sparkle.jcfg.Writer;

/**
 *
 * @author yew_mentzaki
 */
public class Part {

    private String title;
    private byte[] data;

    Part(byte[] data) {
        
    }

    public Part(String title, byte[] data) {
        this.title = title;
        this.data = data;
    }

    public Part(String title, String data) {
        this.title = title;
        this.data = data.getBytes();
    }

    public Part(String title, JCFG data) {
        this.title = title;
        this.data = Writer.writeToString(data).getBytes();
    }

    public byte[] getDataAsByteArray() {
        return data;
    }

    byte[] getPart() {
        byte[] bytes = new byte[4 + title.getBytes().length + 4 + data.length];
        int caret = 0;
        byte[] titleLength = ByteBuffer.allocate(4).putInt(title.length()).array();
        byte[] titleBytes = title.getBytes();
        for (int i = 0; i < titleLength.length; i++) {
            bytes[caret] = titleLength[i];
            caret++;
        }
        for (int i = 0; i < titleBytes.length; i++) {
            bytes[caret] = titleBytes[i];
            caret++;
        }
        byte[] dataLength = ByteBuffer.allocate(4).putInt(data.length).array();
        byte[] dataBytes = data;
        for (int i = 0; i < dataLength.length; i++) {
            bytes[caret] = dataLength[i];
            caret++;
        }
        for (int i = 0; i < dataBytes.length; i++) {
            bytes[caret] = dataBytes[i];
            caret++;
        }
        return bytes;
    }

    public String getDataAsString() {
        return new String(data);
    }

    public JCFG getDataAsJCFG() {
        return Parser.parse(new String(data));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
