
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {

        String filePath = Main.class.getResource("test.txt").getPath();

        MyThread writerThread = new MyThread(filePath);
        writerThread.readFile();
        writerThread.write();

        ArrayList<String> reader1 = new ArrayList<>();
        MyThread readerThread1 = new MyThread(reader1);

        ArrayList<String> reader2 = new ArrayList<>();
        MyThread readerThread2 = new MyThread(reader2);

        ArrayList<String> reader3 = new ArrayList<>();
        MyThread readerThread3 = new MyThread(reader3);

        ArrayList<String> reader4 = new ArrayList<>();
        MyThread readerThread4 = new MyThread(reader4);

        ArrayList<String> reader5 = new ArrayList<>();
        MyThread readerThread5 = new MyThread(reader5);


        readerThread1.start();
        readerThread2.start();
        readerThread3.start();
        readerThread4.start();
        readerThread5.start();

    }

}

