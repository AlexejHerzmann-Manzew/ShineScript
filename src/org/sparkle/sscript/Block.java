package org.sparkle.sscript;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yew_mentzaki
 */
public class Block {

    Block(String code) throws Exception {
        parse(code);
    }

    //Список всех элементов.
    ArrayList<Element> elements = new ArrayList<Element>();

    //Добавление элемента без блока в список, с определением его принадлежности.
    private void addElement(String element) {
        System.out.println("Element without block: " + element);
    }

    //Добавление элемента с блоком в список, также с определением.
    private void addElement(String element, Block block) {
        System.out.println("Element with block: " + element);
    }

    //Лексический и синтаксический анализ блока:
    private void parse(String code) throws Exception {
        //Разбиваем строку на символы, создаем переменные для обработки данных.
        char[] charact = code.toCharArray();
        String element = new String();
        String block = new String();
        boolean whiteSpace = false;
        int blockLevel = 0;
        int parenthesesLevel = 0;
        //Посимвольно перебираем строку.
        for (int caret = 0; caret < charact.length; caret++) {
            if (charact[caret] == '(') {
                parenthesesLevel++;
            }
            if (charact[caret] == ')') {
                parenthesesLevel--;
                if(parenthesesLevel < 0){
                    throw new Exception("Excess closing parenthese!");
                }
            }
            //Обработка пробела:
            if (charact[caret] == ' ') {
                //Если предыдущий символ тоже был пробелом, мы его не записываем.
                if (whiteSpace) {
                    continue;
                } else {
                    whiteSpace = true;
                    if (blockLevel == 0) {
                        element += ' ';
                    } else {
                        block += ' ';
                    }
                }
            } else {
                whiteSpace = false;
                //Обработка, если глубина вложения блока == 0, то есть запись элементов.
                if (blockLevel == 0) {
                    //Если блок открывается, повышаем уровень блока.
                    if (charact[caret] == '{') {
                        blockLevel++;
                    } else //А если закрывается, то программист накосячил, о чём и бугуртим.
                    if (charact[caret] == '}') {
                        throw new Exception("Excess closing brace");
                    } else if (charact[caret] == '\n') {
                        //Если переменная element содержит непробельные символы.
                        if (element.trim().length() > 0) {
                            //А вот тут мы проверяем последующий код на наличие других элементов, блока или конца файла.
                            for (int subcaret = caret + 1; subcaret < charact.length; subcaret++) {
                                //Если мы видим открывающийся брейс, то прекращаем проверку: кто-то просто их
                                //переносит на следующую строку. Или на 100 следующих строк. Да.
                                if (charact[subcaret] == '{') {
                                    caret = subcaret - 1;
                                    break;
                                }
                                //Если видим закрывающийся брейс или находим что-то не похожее на пробел, перенос строки или табуляцию,
                                //то добавляем элемент без блока.
                                if (charact[subcaret] == '}' || charact[subcaret] != ' ' & charact[subcaret] != '\n' & charact[subcaret] != '\t') {
                                    addElement(element.trim());
                                    element = new String();
                                    caret = subcaret - 1;
                                    break;
                                }
                            }
                        }
                    //Если попалась точка с запятой и уровень круглых скобочек == 0.
                    } else if (charact[caret] == ';' && parenthesesLevel == 0) {
                        addElement(element.trim());
                        element = new String();
                    } else {
                        element += charact[caret];
                    }
                } else {
                    //Если брейс открывается, углубляемся.
                    if (charact[caret] == '{') {
                        blockLevel++;
                    }
                    //Красивое условие? Мне тоже нравится. Если у нас закрывается брейс,
                    //то уменьшаем уровень прямо из условия и если он равен нулю, создаем элемент,
                    //на сей раз уже с блоком. Последний закрывающий блок, как и положено, не запишется.
                    if (charact[caret] == '}' && --blockLevel == 0) {
                        addElement(element.trim(), new Block(block));
                        element = new String();
                        block = new String();
                        continue;
                    }
                    block += charact[caret];
                }
            }
        }
        //Если кто-то не закрыл брейсы, это его проблема. Выбрасываем исключение.
        if (blockLevel > 0 || parenthesesLevel > 0) {
            throw new Exception("Reached end of file while parsing");
        //Ну и добавляем последний оператор, если он есть. Из-за свойств кода выше, его пришлось вынести сюда.
        } else {
            if (element.trim().length() > 0) {
                addElement(element.trim());
                element = new String();
            }
        }
    }
    //Исполнение содержимого блока:
    public void exec(VarSet varset, Script script) throws Exception{
        //Перебираем все элементы.
        for (Element element : elements) {
            //Если элемент - инструкция, то есть безаговорочно выполняемый в этом месте,
            //Исполняем его. Если в нём еггог, собираем опарышей в виде throws Exception.
            if(element instanceof Instruction){
                ((Instruction)element).exec(varset, script);
            }
        }
       
    }
}
