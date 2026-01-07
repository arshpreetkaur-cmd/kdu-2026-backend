import java.util.Deque;
import java.util.ArrayDeque;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class MessageQueue1 {

    //Queue used to store messages shared by all threads
    private final Deque<String> queue = new ArrayDeque<>();

    //lock is being used instead of synchronized
    private final Lock lock = new ReentrantLock();

    //condition to make consumers wait when queue is empty
    private final Condition notEmpty = lock.newCondition();

    // Producer puts a message
    public void put(String message) {

        //Manually acquiring the lock before accessing Queue
        lock.lock();
        try {
            queue.addLast(message);
            notEmpty.signalAll(); //notifying all waiting consumers that a message is available
        } finally {
            lock.unlock();
        }
    }

    // Consumer takes a message (waits if empty)
    public String take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    notEmpty.await(); // if the queue is empty, consumer releases the lock, goes into waiting state. Like this the lock becomes free
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return queue.removeFirst();
        } finally {
            lock.unlock();
        }
    }
}

class MessageSender1 implements Runnable {
    private final MessageQueue1 queue;
    private final String senderName;

    public MessageSender1(MessageQueue1 queue, String senderName) {
        this.queue = queue;
        this.senderName = senderName;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            String msg = senderName + " | " + LocalDateTime.now() + " | Message " + i;
            queue.put(msg);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

class MessageReceiver1 implements Runnable {
    private final MessageQueue1 queue;
    private final String receiverName;

    public MessageReceiver1(MessageQueue1 queue, String receiverName) {
        this.queue = queue;
        this.receiverName = receiverName;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            String msg = queue.take();
            if (msg == null) return; // interrupted
            System.out.println(receiverName + " received: " + msg);
        }
    }
}

public class Advanced_Locking {
    public static void main(String[] args) {
        MessageQueue1 queue = new MessageQueue1();

        // Pool for senders
        ExecutorService senderPool = Executors.newFixedThreadPool(3);

        // Pool for receivers
        ExecutorService receiverPool = Executors.newFixedThreadPool(3);

        // Submit 3 senders
        senderPool.submit(new MessageSender1(queue, "Sender-1"));
        senderPool.submit(new MessageSender1(queue, "Sender-2"));
        senderPool.submit(new MessageSender1(queue, "Sender-3"));

        // Submit 3 receivers
        receiverPool.submit(new MessageReceiver1(queue, "Receiver-1"));
        receiverPool.submit(new MessageReceiver1(queue, "Receiver-2"));
        receiverPool.submit(new MessageReceiver1(queue, "Receiver-3"));

        // Shutdown and wait (graceful)
        shutdownAndWait(senderPool);
        shutdownAndWait(receiverPool);

        System.out.println("All tasks finished. Program ends.");
    }

    private static void shutdownAndWait(ExecutorService pool) {
        pool.shutdown(); // stop accepting new tasks
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // force stop if taking too long
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
