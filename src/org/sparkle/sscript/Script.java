package org.sparkle.sscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yew_mentzaki
 */
public class Script {

    //Загрузка скрипта из файла...
    public Script(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String code = new String();
            while (scanner.hasNextLine()) {
                code += scanner.nextLine() + '\n';
            }
            if (code.length() > 0) {
                try {
                    parse(code);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Argument error: file \"" + file.getAbsolutePath() + "\" not found.");
        }
    }

    //...Или из строки, если кому-то так будет удобнее.
    public Script(String code) {
        try {
            parse(code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Собственно, тело скрипта.
    private Block scriptBlock;

    //Парсинг, почти полностью перемещенный в блок.
    public void parse(String code) throws Exception {
        //Удаляем все комментарии из кода.
        code = code.replaceAll("(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)", "") + " ";
        //Далее блок парсится собственным алгоритом.
        scriptBlock = new Block(code);
        //А теперь мы просто собираем сливки, в нашем случае, события, функции:
        for (Element element : scriptBlock.elements) {
            if (element instanceof Event) {
                events.add((Event) element);
            }
            if (element instanceof Func) {
                func.add((Func) element);
            }
        }
        //Очевидно, что собрать их лучше именно сейчас, пока фризы из-за инициализаций ещё
        //есть. А потом перебирать сто тыщ мильёнов элементов не придется.
    }

    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<Func> func = new ArrayList<Func>();
    private VarSet vars = new VarSet();

    public void event(final String event, final String... tags) throws Exception {
        for (Event e : events) {
            if (e.name.equals(event)) {
                if (e.tags.length > 0) {
                    boolean ex = true;
                    for (String tag : tags) {
                        for (String etag : e.tags) {
                            if (!tag.equals(etag)) {
                                ex = false;
                                break;
                            }
                        }
                    }
                    if (ex) {
                        e.block.exec(vars, this);
                    }
                } else {
                    e.block.exec(vars, this);
                }
            }
        }
    }

    public void start() throws Exception {
        scriptBlock.exec(vars, this);
        event("started");
    }
}
