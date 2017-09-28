package lab01.main;

import java.util.ArrayList;

class Printer extends Thread {

     protected void printResult(ArrayList<WordCounter.Word> words) {
         int count = 0;
         for (WordCounter.Word word : words) {
             count++;
             System.out.printf("%-13s - %d, ",word.getValue(), word.getCounter());
             if(count == 7) {
                 count = 0;
                 System.out.println();
             }
         }
     }

     public void run() {
         printResult(WordCounter.getWords());
     }
}
