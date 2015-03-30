package org.sparkle.sscript;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author yew_mentzaki
 */
public class Value {

    //Базовый оператор
    private abstract class Operator {

        public Operator(String s, int priority) {
            this.priority = priority;
            this.s = s;
        }

        public abstract Object exec(Object... args) throws Exception;

        @Override
        public String toString() {
            return s; //To change body of generated methods, choose Tools | Templates.
        }

        String s;
        int priority;
    }

    //Бинарный оператор, типа 2+2.
    private abstract class BinaryOperator extends Operator {

        public BinaryOperator(String s, int priority) {
            super(s, priority);
        }

        @Override
        public Object exec(Object... args) throws Exception {
            //Сначала читаем b, а потом a. Всё потому, что b на вершине стека,
            //а так читабильней.
            Object a = args[0];
            Object b = args[1];
            return exec(a, b);
        }

        //Красивый метод, в котором операнды вынесены в отдельные аргументы.
        public abstract Object exec(Object a, Object b) throws Exception;
    }
    //В объявлении всех бинарных операндов будет многа букав, так что прокомментирую
    //только первый. Остальные, в принципе, аналогичны и даже проще.
    Operator[] operators = {
        //Конструктор оператора "+", тут всё понятно.
        new BinaryOperator("+", 1) {
            @Override
            //А тут его исполнение:
            public Object exec(Object a, Object b) throws Exception {
                //Если один из двух операндов строка, мы склеиваем строки.
                if (a instanceof String || b instanceof String) {
                    String result = (a.toString() + b.toString());
                    //Далее, если получится, записываем результат в стек как число.
                    try {
                        return (Double.valueOf(result));
                    } catch (NumberFormatException e) {
                        //А если не получится, как строку.
                        return (result);
                    }
                    //Если оба оператора числа, то мы и складываем их как числа.
                } else if (a instanceof Double && b instanceof Double) {
                    return (((Double) a) + ((Double) b));
                    //А вот это безобразие для всяких математиков, которые любят писать не
                    //П и -П, а +П и -П. Плюс, как можно понять, просто делает код красивше.
                } else if (a == null && b instanceof Double) {
                    return (b);
                    //А если что-то еще, выбрасываем исключение, ибо нефиг.
                } else {
                    throw new Exception("Bad operand types for binary operator \"+\": " + a.getClass().getName() + " and " + b.getClass().getName());
                }
            }
        },
        new BinaryOperator("-", 1) {
            @Override
            public Object exec(Object a, Object b) throws Exception {
                //А вот тут проверки для строк не будет. Вы когда-нибудь видели, чтобы из строк вычитали?
                //Вот и я нет.
                if (a instanceof Double && b instanceof Double) {
                    return (((Double) a) - ((Double) b));
                    //А вот здесь унарные фетиши математиков уже полезны: данное безобразие меняет знак числа.
                } else if (a == null && b instanceof Double) {
                    return (-((Double) b));
                } else {
                    throw new Exception("Bad operand types for binary operator \"-\": " + a.getClass().getName() + " and " + b.getClass().getName());
                }
            }
        },
        new BinaryOperator("*", 2) {
            @Override
            public Object exec(Object a, Object b) throws Exception {
                if (a instanceof Double && b instanceof Double) {
                    return (((Double) a) * ((Double) b));
                } else {
                    throw new Exception("Bad operand types for binary operator \"*\": " + a.getClass().getName() + " and " + b.getClass().getName());
                }
            }
        },
        new BinaryOperator("/", 2) {
            @Override
            public Object exec(Object a, Object b) throws Exception {
                if (a instanceof Double && b instanceof Double) {
                    return (((Double) a) / ((Double) b));
                } else {
                    throw new Exception("Bad operand types for binary operator \"/\": " + a.getClass().getName() + " and " + b.getClass().getName());
                }
            }
        },};

    public Object value;

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }

    //Выполняется анализ выражения.
    public Value(String expression, VarSet vars) throws Exception {
        //Сначала ставим пробелы вокруг всех скобок и операторов.
        expression = expression.replace("(", " ( ");
        expression = expression.replace(")", " ) ");
        for (Operator op : operators) {
            expression = expression.replace(op.s, " " + op.s + " ");
        }
        //Затем разделяем строку с выражением по пробелам.
        String elems[] = expression.split(" ");
        ArrayList<Object> elements = new ArrayList<Object>();
        String subvalue = new String();
        int braketlevel = 0;
        //Последовательно обрабатываем каждый элемент строки:
        for (String elem : elems) {
            //Если элемент пустой, игнорируем.
            if (elem.length() <= 0) {
                continue;
            //Если открывается скобочка, поднимаемся на уровень наверх, начинаем запись подвыражения.
            } else if (elem.equals("(")) {
                braketlevel++;
                if (braketlevel == 1) {
                    continue;
                }
            //Если закрывается скобочка и мы вернулись на нулевой уровень, заканчиваем запись подвыражения и записываем его значение.
            } else if (elem.equals(")")) {
                braketlevel--;
                if (braketlevel == 0) {
                    elements.add(new Value(subvalue, vars).value);
                    subvalue = new String();
                    continue;
                }
            }
            //Если уровень вложения в скобки равен нулю, колдунствуем:
            if (braketlevel == 0) {
                boolean next = true;
                //Если это оператор, добавляем оператор в список.
                for (Operator op : operators) {
                    if (elem.equals(op.s)) {
                        next = false;
                        elements.add(op);
                        continue;
                    }
                }
                if (!next) {
                    continue;
                }
                
                //Если существует такая переменная, добавляем переменную в список.
                Var var = vars.getVariable(elem);
                if (var != null) {
                    next = false;
                    elements.add(var.value);
                    continue;
                //Иначе:
                } else {
                    //Обрабатываем возможные варианты записи логического значения "Правда" и "Ложь". 
                    //Их много. А всё для того, чтобы у вас была возможность писать код читабильно.
                    if (elem.equals("true") || elem.equals("yes") || elem.equals("enabled") || elem.equals("on")) {
                        elements.add(true);
                    } else if (elem.equals("false") || elem.equals("no") || elem.equals("disabled") || elem.equals("off")) {
                        elements.add(true);
                    //Для пустого указателя и то два варианта.
                    } else if (elem.equals("null") || elem.equals("nothing")) {
                        elements.add(null);
                    } else {
                        try {
                            //Тут пытаемся записать как Double. В ShineScript не будет интеджеров, так
                            //что и изощраться не надо.
                            elements.add(Double.valueOf(elem));
                        } catch (Exception e) {
                            //А если терпим отказ, просто записываем null. 10 лет работаю проктологом, но такую фигню
                            //вижу первый раз - так думает этот парсер, получая исключение, и так думаю я, глядя на код.
                            elements.add(null);
                        }
                    }
                }
            } else {
                //А вот это если у нас имеется что-то в скобочках - записываем это в подстроку для дальнейшей
                //обработки.
                subvalue += elem + ' ';
            }
        }
        //Тут тоже нечто страшное... Эта херня ещё не может в унарные операторы вообще и в отрицательные числа в частности.
        //Постепенно спускаемся от максимального приоритета 3 до минимального 0.
        for (int priority = 3; priority >= 0; priority--) {
            //Пробегаемся по всему коду в поисках оператора с таким приоритетом.
            for (int caret = 0; caret < elements.size(); caret++) {
                if (elements.get(caret) instanceof Operator) {
                    Operator operator = (Operator) elements.get(caret);
                    if (operator.priority == priority) {
                        //Найдя оператор, выполняем действие со значениями справа и слева от него,
                        Object o = operator.exec(elements.get(caret - 1), elements.get(caret + 1));
                        //после чего выпиливаем всех троих
                        for (int i = 0; i < 3; i++) {
                            elements.remove(caret - 1);
                        }
                        //и записываем на их место результат о.
                        elements.add(caret - 1, o);
                        caret = 0;
                    }
                }
            }
        }
        //После такой тумба-юмбы в списке должно остаться одно значение. Оно и будет результатом вычисления.
        value = elements.get(0);
    }
}
