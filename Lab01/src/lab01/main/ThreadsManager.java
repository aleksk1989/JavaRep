package lab01.main;

import java.io.IOException;
import java.util.ArrayList;


class ThreadsManager {

    private static volatile boolean shouldStopAll = false;
    private static ThreadsManager instance;
    private ArrayList<String> resources = new ArrayList<>();
    private ArrayList<Thread> threads = new ArrayList<>();

    public void removeRes(String filename) {
        resources.remove(filename);
    }

    public int threadsSize() {
        return threads.size();
    }

    public int resoursesSize() {
        return resources.size();
    }

    private ThreadsManager() {

    }

    public static ThreadsManager getInstance() {
        if(instance == null) {
            instance = new ThreadsManager();
        }
        return instance;
    }

    public void addFile(String fileName) {
        resources.add(fileName);
    }

    public void addFiles(ArrayList<String> files) {
        resources.addAll(files);
    }

    private Thread createThread(String fileName) {
        Thread thread = new Thread(() -> {
            try {
                Parser parser = new WordCounter(fileName);
                parser.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return thread;
    }

    private void createThreads() {
        for (String resource: resources) {
            threads.add(createThread(resource));
        }
    }

    private void startThreads() {
        createThreads();
        for (Thread thread: threads) {
            thread.start();
        }
    }

    public void joinAll() {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
    }

    public static void stopAll() {
        shouldStopAll = true;
    }

    public static boolean shouldIStop() {
        return shouldStopAll;
    }

    public void execute() {
        startThreads();
        joinAll();
        new Printer().start();
        stopAll();
    }

}
