import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {

    private static final BlockingQueue<String> writerArray = new ArrayBlockingQueue<>(100); // Queue where data from the file is written.
    private static final ArrayList<String> dataArray = new ArrayList<>(); // List from which the writer takes data for the queue.
    private ArrayList<String> readerArray = new ArrayList<>(); // List where the reader places data from the writer's queue.
    private String filePath; // Path to the file from which data is taken for the writer.
    private static int queue = 1; // Counter for determining the queue for reading (1st thread, 2nd thread, etc.).
    private static final AtomicInteger count = new AtomicInteger(); // Counter for readers.
    private static final AtomicInteger writersCounter = new AtomicInteger(); // Counter for writers.
    private static final List<Integer> threadsReaders = new CopyOnWriteArrayList<>(); // Creating a thread-safe array for further calculation of the readers' queue.
    private int numOfReaderQueue; // Reader's queue number.
    private static volatile int queueSize; // Size of the readers' queue.


    // Writer constructor.
    public MyThread(String filePath) {
        this.filePath = filePath;
    }

    public static ArrayList<String> getDataArray() {
        return dataArray;
    }

    // Reader constructor.
    public MyThread(ArrayList<String> readerArray) {
        this.readerArray = readerArray;
        int queueOfThread;
    }

    // Reading the file to obtain data.
    public void readFile() {
        try (FileReader reader = new FileReader(filePath)) {
            // Reading character by character.
            int numChar;
            while ((numChar = reader.read()) != -1) {
                dataArray.add(String.valueOf((char) numChar));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Method for the writer to write 5 values per second.
    private void startWrite() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < dataArray.size(); i++) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime >= 200) {
                writerArray.put(dataArray.get(i));
                startTime = currentTime;
            } else {
                i--;
            }
        }
    }

    // Method for calculating the queue number for the reader thread.
    private synchronized int queueNum(){
        String threadName = Thread.currentThread().getName();
        int indexFirstDigit = threadName.indexOf("-") + 1;
        String digitString = threadName.substring(indexFirstDigit);
        return Integer.parseInt(digitString);
    }

    // Method for calculating the size of the overall readers' queue.
    private synchronized int queueSize(){
        ArrayList<String> userThreads = new ArrayList<>();
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        allStackTraces.forEach((k, v) -> {
            if (k.getName().startsWith("Thread")) {
                userThreads.add(k.getName());
            }
        });
        return userThreads.size();
    }

    // Method for the reader to provide data to the writer.
    @Override
    public void run() {

        threadsReaders.add(queueSize()-writersCounter.get());

        int currentThreadSize = queueSize() - writersCounter.get();
        synchronized (threadsReaders) {
            threadsReaders.add(currentThreadSize);
        }

        int sizeOfQueue = Collections.max(threadsReaders);
        int limit = dataArray.size();
        int numInQueue = queueNum()-writersCounter.get();
        this.setNumOfReaderQueue(numInQueue);
        setQueueSize(sizeOfQueue);

        while (count.get() < limit) {
            if (queue > sizeOfQueue){
                queue = 1;
            }

            try {
                if (numInQueue == queue) {
                    readerArray.add(writerArray.take());
                    System.out.println(readerArray.get(readerArray.size()-1));
                    System.out.println("Queue:" + queue);
                    count.incrementAndGet();
                    incrementQueue();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    // Synchronized method for increasing the queue order.
    public synchronized void incrementQueue () {
            queue++;
        }

    // Method for writing by the writer.
    public void write(){
        Thread write = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    writersCounter.incrementAndGet();
                    startWrite();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
        write.start();
    }

    // Method for testing. Getting the reader's queue number.
    public int getNumOfReaderQueue() {
        return numOfReaderQueue;
    }

    // Method for testing. Setting the reader's queue number.
    public void setNumOfReaderQueue(int numOfReaderQueue) {
        this.numOfReaderQueue = numOfReaderQueue;
    }

    // Method for testing. Getting the queue size.
    public static int getQueueSize() {
        return queueSize;
    }

    // Method for testing. Setting the queue size.
    public static void setQueueSize(int queueSize) {
        MyThread.queueSize = queueSize;
    }

}
