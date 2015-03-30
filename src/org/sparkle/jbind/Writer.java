package org.sparkle.jbind;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yew_mentzaki
 */
public class Writer {

    public static byte[] write(JBinD bind) {
        Part[] parts = bind.getAllParts();
        byte[][] bytes = new byte[parts.length][];
        int length = 0;
        for (int i = 0; i < parts.length; i++) {
            bytes[i] = parts[i].getPart();
            length += bytes[i].length;
        }
        byte[] b = new byte[length];
        int caret = 0;
        for (int i = 0; i < parts.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                b[caret] = bytes[i][j];
                caret++;
            }
        }
        return b;
    }

    public static void write(JBinD bind, File file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.write(write(bind));
    }
}
