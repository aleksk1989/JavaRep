package lab01.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public abstract class Parser extends FileReader {

    protected String fileName;

    public Parser(String fileName) throws FileNotFoundException {
        super(fileName);
        this.fileName = fileName;
    }

    public Parser(File file) throws FileNotFoundException {
        super(file);
        this.fileName = file.getName();
    }

    public abstract void execute();

    protected abstract void parseLine(String line);

    protected abstract void parseFile() throws ParseInterruptedException;

}
