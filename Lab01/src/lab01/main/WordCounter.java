package lab01.main;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**@class WordCounter - основной класс,
 * содержащий логику парсера и подсчета
 * количества появлений каждого слова
 * в переданном файле
 */
public class WordCounter extends Parser {

    private static ArrayList<Word> words = new ArrayList<>();
    private BufferedReader reader;

    /**@class Word - внутренний класс,
     * который ассоциирует строковое значение
     * слова с счетчиков этого значения
     */
    class Word {
        private final String value;
        private int counter;

        //word ctor
        public Word(String value) {
            this.value = value;
            counter = 1;
        }

        //word getters
        public String getValue() {
            return value;
        }

        public int getCounter() {
            return counter;
        }

        //incrementor
        public void incrementCounter() {
            counter++;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Word word = (Word) o;
            return value != null ? value.equals(word.value) : word.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    //WordCounter getters
    public static ArrayList<Word> getWords() {
        return words;
    }

    //WordCounter ctors
    public WordCounter(File file) throws FileNotFoundException {
        super(file);
        reader = new BufferedReader(this);
    }

    public WordCounter(String fileName) throws FileNotFoundException {
        super(fileName);
        reader = new BufferedReader(this);
    }

    /**Запуск парсера через вызов
     * метода parseFile {@see WordCounter#parseFile()}
     */
    public void execute() {
        try {
            parseFile();
        } catch (ParseInterruptedException exc) {
            ThreadsManager.stopAll();
            synchronized (words) {
                words.notifyAll();
            }
            System.out.println("Некорректные символы в файле: " + exc.inWhichFile());
        }
    }

    /**@param line
     * Парсинг строки. Метод разбивает строку
     * на слова, складывает их в буфер и проверяет
     * каждое слово на предмет вхождения в массив слов
     */
    protected void parseLine(String line) {

        ArrayList<String> buffer = new ArrayList<>();
        Collections.addAll(buffer, line.split(" "));

        synchronized (words) {
            for (String val: buffer) {
                if (val.equals("")) {
                    continue;
                }
                val = val.toLowerCase();
                if (words.contains(new Word(val))) {
                    for (Word word : words) {
                        if (word.equals(new Word(val))) {
                            word.incrementCounter();
                        }
                    }
                } else {
                    words.add(new Word(val));
                }
            }
        }
    }

    /**Метод, осуществляющий построчный парсинг файла.
     * Метод может выбрасывать исключение -
     * @throws ParseInterruptedException,
     * которое говорит о том, что в процессе парсинга
     * встретились некорректные символы {@see WordCounter#lineIsValid(String line)}
     */
    protected void parseFile() throws ParseInterruptedException {
        String line = "";
        try {
            while ((line = reader.readLine()) != null && !ThreadsManager.shouldIStop()) {
                line = line.trim();
                if (!lineIsValid(line.replaceAll("\\pP", ""))) {
                    throw new ParseInterruptedException(fileName);
                } else {
                    parseLine(line.replaceAll("\\pP", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {;
                e.printStackTrace();
            }
        }
    }

    /**@param line - строка для валидации
     * @return - значение true или false
     * Метод получает на входи строку
     * и сравнивает с шаблоном.
     */
    public boolean lineIsValid(String line) {
        Pattern pattern = Pattern.compile("[" + "а-яА-ЯёЁ" + "\\s" + "\\p{Punct}" + "]" + "*");
        Matcher matcher = pattern.matcher(line);
        if(!matcher.matches()) {
            return false;
        }
        return true;
    }
}
