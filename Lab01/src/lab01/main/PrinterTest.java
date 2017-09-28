package lab01.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.*;

class PrinterTest {
    private static Printer proxy;
    private static Printer printer;

    @BeforeAll
    public static void init() {
        printer = new Printer();
        PrinterProxy printerHandler = new PrinterProxy(printer);
        proxy = (Printer) Proxy.newProxyInstance(Printer.class.getClassLoader(),
                Printer.class.getInterfaces(), printerHandler);
    }

    @Test
    void printResult() {

    }

}