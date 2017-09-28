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


class WordCounter extends Parser {

    private static ArrayList<Word> words = new ArrayList<>();
    private BufferedReader reader;

    private static volatile int threadCount;

    class Word {
        private final String value;
        private int counter;

        public Word(String value) {
            this.value = value;
            counter = 1;
        }

        public String getValue() {
            return value;
        }

        public void incrementCounter() {
            counter++;
        }

        public int getCounter() {
            return counter;
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

    public static ArrayList<Word> getWords() {
        return words;
    }

    //class ctors
    public WordCounter(File file) throws FileNotFoundException {
        super(file);
        reader = new BufferedReader(this);
        threadCount = ThreadsManager.getInstance().threadsSize();
    }

    public WordCounter(String fileName) throws FileNotFoundException {
        super(fileName);
        reader = new BufferedReader(this);
        threadCount = ThreadsManager.getInstance().threadsSize();
    }

    //start parsing
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

    //parse functions
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

    //line validation func
    public boolean lineIsValid(String line) {
        Pattern pattern = Pattern.compile("[" + "а-яА-ЯёЁ" + "\\s" + "\\p{Punct}" + "]" + "*");
        Matcher matcher = pattern.matcher(line);
        if(!matcher.matches()) {
            return false;
        }
        return true;
    }
}
