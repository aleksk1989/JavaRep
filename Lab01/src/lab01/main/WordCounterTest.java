package lab01.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WordCounterTest {

    private static WordCounter wc;
    private static WordCounter.Word word;
    private static PrinterProxy proxy;

    @BeforeAll
    public static void init() {
        try {
            wc = new WordCounter("res/file1.txt");
            word = wc.new Word("value");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lineIsValid() {
        String str1 = "Invalid line";
        String str2 = "Валидная строка";
        String str3 = "Не vaлидная строка";
        boolean b1 = wc.lineIsValid(str1);
        boolean b2 = wc.lineIsValid(str2);
        boolean b3 = wc.lineIsValid(str3);
        assertFalse(b1);
        assertTrue(b2);
        assertFalse(b1);
    }

    @Test
    public void readLine() {
        String str1 = "В строке 5 уникальных слов";
        String str2 = "В строке 9 уникальных слов и в строке 2 повторяющихся слова";
        String str3 = "строка строка строка строка";
        String str4 = "строка foo строка foo foo строка";
        wc.parseLine(str1);
        int result = WordCounter.getWords().size();
        assertEquals(5, result);
        WordCounter.getWords().clear();
        wc.parseLine(str2);
        result = WordCounter.getWords().size();
        assertEquals(9, result);
        WordCounter.getWords().clear();
        wc.parseLine(str3);
        result = WordCounter.getWords().size();
        assertEquals(1, result);
        WordCounter.getWords().clear();
        wc.parseLine(str4);
        result = WordCounter.getWords().size();
        assertEquals(2, result);
        WordCounter.getWords().clear();
    }

    @Test
    public void testIncrementor() {
        for (int i = 0; i < 1000000; i++) {
            word.incrementCounter();
        }
        int result = word.getCounter();
        assertEquals(1000001, result);
    }
}