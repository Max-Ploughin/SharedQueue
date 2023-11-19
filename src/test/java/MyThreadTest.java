import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MyThreadTest {

    public String getPath() throws FileNotFoundException {
        URL url = Main.class.getResource("test.txt");
        if (url == null) {
            throw new FileNotFoundException();
        }
        return url.getPath();
    }

    @Test
    public void testReadFile() throws FileNotFoundException {
        MyThread myThread = new MyThread(getPath());
        myThread.readFile();
        ArrayList<String> dataArray = myThread.getDataArray();
        assertEquals(21, dataArray.size());
    }

    @Test
    public void testRun() throws InterruptedException, FileNotFoundException {

        MyThread writer = new MyThread(getPath());
        writer.readFile();
        writer.write();

        ArrayList<String> readerArray1 = new ArrayList<>();
        MyThread reader1 = new MyThread(readerArray1);
        ArrayList<String> readerArray2 = new ArrayList<>();
        MyThread reader2 = new MyThread(readerArray2);

        reader1.start();
        reader2.start();
        writer.join();
        reader1.join();
        reader2.join();

        assertEquals("T", readerArray1.get(0));
        assertEquals("h", readerArray2.get(0));
        assertEquals("e", readerArray1.get(1));

    }

    // Test to check queue assignment.
    @Test
    public void queueNum() throws InterruptedException, FileNotFoundException {

        MyThread writer = new MyThread(getPath());
        writer.readFile();
        writer.write();

        ArrayList<String> readerArray1 = new ArrayList<>();
        MyThread reader1 = new MyThread(readerArray1);
        ArrayList<String> readerArray2 = new ArrayList<>();
        MyThread reader2 = new MyThread(readerArray2);

        reader1.start();
        reader2.start();

        writer.join();
        reader1.join();
        reader2.join();

        assertEquals(1, reader1.getNumOfReaderQueue());
        assertEquals(2, reader2.getNumOfReaderQueue());
    }

    // Test to check the size of the readers' queue.
    @Test
    public void queueSize() throws InterruptedException, FileNotFoundException {

        MyThread writer = new MyThread(getPath());
        writer.readFile();
        writer.write();

        ArrayList<String> readerArray1 = new ArrayList<>();
        MyThread reader1 = new MyThread(readerArray1);
        ArrayList<String> readerArray2 = new ArrayList<>();
        MyThread reader2 = new MyThread(readerArray2);

        reader1.start();
        reader2.start();

        writer.join();
        reader1.join();
        reader2.join();

        assertEquals(2, MyThread.getQueueSize());

    }

}