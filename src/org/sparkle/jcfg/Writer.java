package org.sparkle.jcfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*
 * Copyright (C) 2015 yew_mentzaki
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author yew_mentzaki
 */
public class Writer {
    public static String writeToString(JCFG jcfg){
        String s = new String();
        for (Parameter p : jcfg.list()) {
            s += p.getName() + ": " + p.getValueAsString() + "\n";
        }
        return s;
    }
    public static void writeToFile(JCFG jcfg, File f) throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(f);
        for (Parameter p : jcfg.list()) {
            pw.println(p.getName() + ": " + p.getValueAsString().replace("#", "\\#").replace("\n", "\\n"));
        }
        pw.close();
    }
    
}
