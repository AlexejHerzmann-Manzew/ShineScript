package org.sparkle.jcfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public final class Parser {

        public static JCFG parse(File file) throws FileNotFoundException {
        JCFG cfg = new JCFG();
        Scanner scanner = new Scanner(file, "UTF8");
        while (scanner.hasNextLine()) {
            String l = scanner.nextLine();
            char[] ch = l.toCharArray();
            l = new String();
            boolean control = false;

            for (int i = 0; i < ch.length; i++) {
                if (control) {
                    control = false;
                } else {
                    if (ch[i] == '#') {
                        break;
                    }
                    if (ch[i] == '\\') {
                        control = true;
                    }
                }
                l = l + ch[i];
            }
            if (l.contains(":")) {
                int c = l.indexOf(":");
                String param = l.substring(0, c).trim();
                char[] v = l.substring(c + 1, l.length()).trim().toCharArray();
                String value = new String();
                control = false;
                for (int i = 0; i < v.length; i++) {

                    if (control) {
                        switch (v[i]) {
                            case '0':
                                value += '\0';
                                break;
                            case 'f':
                                value += '\f';
                                break;
                            case 'b':
                                value += '\b';
                                break;
                            case 't':
                                value += '\t';
                                break;
                            case 'n':
                                value += '\n';
                                break;
                            case 'r':
                                value += '\r';
                                break;
                            case '\"':
                                value += '\"';
                                break;
                            case '\'':
                                value += '\'';
                                break;
                            case '#':
                                value += '#';
                                break;
                            case '\\':
                                value += '\\';
                                break;

                        }
                        control = false;
                    } else {
                        if (v[i] == '\\') {
                            control = true;
                        } else {
                            value += v[i];
                        }
                    }

                }
                cfg.set(new Parameter(param, value));
            }
        }
        return cfg;
    }
    
    public static JCFG parse(String line) {
        JCFG cfg = new JCFG();
        String[] lines = line.split("\n");
        for (String l : lines) {
            char[] ch = l.toCharArray();
            l = new String();
            boolean control = false;

            for (int i = 0; i < ch.length; i++) {
                if (control) {
                    control = false;
                } else {
                    if (ch[i] == '#') {
                        break;
                    }
                    if (ch[i] == '\\') {
                        control = true;
                    }
                }
                l = l + ch[i];
            }
            if (l.contains(":")) {
                int c = l.indexOf(":");
                String param = l.substring(0, c).trim();
                char[] v = l.substring(c + 1, l.length()).trim().toCharArray();
                String value = new String();
                control = false;
                for (int i = 0; i < v.length; i++) {

                    if (control) {
                        switch (v[i]) {
                            case '0':
                                value += '\0';
                                break;
                            case 'f':
                                value += '\f';
                                break;
                            case 'b':
                                value += '\b';
                                break;
                            case 't':
                                value += '\t';
                                break;
                            case 'n':
                                value += '\n';
                                break;
                            case 'r':
                                value += '\r';
                                break;
                            case '\"':
                                value += '\"';
                                break;
                            case '\'':
                                value += '\'';
                                break;
                            case '#':
                                value += '#';
                                break;
                            case '\\':
                                value += '\\';
                                break;

                        }
                        control = false;
                    } else {
                        if (v[i] == '\\') {
                            control = true;
                        } else {
                            value += v[i];
                        }
                    }

                }
                cfg.set(new Parameter(param, value));
            }
        }
        return cfg;
    }
    
}
