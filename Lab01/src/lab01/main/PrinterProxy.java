package lab01.main;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PrinterProxy implements InvocationHandler {
    Printer printer;

    public PrinterProxy(Printer printer) {
        this.printer = printer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method:" + method.getName());
        return method.invoke(printer, args);
    }
}
