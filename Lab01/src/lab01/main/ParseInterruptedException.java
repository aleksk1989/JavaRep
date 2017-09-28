package lab01.main;

class ParseInterruptedException extends InterruptedException {

    private String foundInfile;

    public ParseInterruptedException(String foundInFile) {
        this.foundInfile = foundInFile;
    }

    public String inWhichFile() {
        return foundInfile;
    }

}
