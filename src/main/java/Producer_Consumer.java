import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

class MessageQueue{

    //queue to store the messages
    private final Deque<String> queue = new ArrayDeque<>();

    //used synchronized so that only one thread can put message at one time
    public synchronized void put(String message){
        queue.addLast(message);
        notifyAll();
    }

    public synchronized String take(){

        //if queue is empty, consumer must wait
        while(queue.isEmpty()){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return null;
            }
        }

        //if queue is not empty, remove the first message
        return queue.removeFirst();
    }
}

class MessageSender implements Runnable{
    private final MessageQueue queue;
    private final String senderName;

    public MessageSender(MessageQueue queue, String senderName){
        this.queue = queue;
        this.senderName = senderName;
    }

    @Override
    public void run() {

        //each producer creates 5 unique messages
        for(int i=0;i<=5;i++){
            String message = senderName + " | " + LocalDateTime.now() + " | Message " + i;
            queue.put(message);
            try {
                Thread.sleep(100);  //slowing down for clarity
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

class MessageReceiver implements Runnable{
    private final MessageQueue queue;
    private final String receiverName;

    public MessageReceiver(MessageQueue queue, String receiverName){
        this.queue = queue;
        this.receiverName = receiverName;
    }

    @Override
    public void run() {

        //Read the first 5 messages from the queue
        for (int i = 1; i <= 5; i++) {
            String msg = queue.take();
            if (msg == null) return; // interrupted
            System.out.println(receiverName + " received: " + msg);
        }
    }
}

public class Producer_Consumer {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue();

        // 3 producers (So each producer produces 5 messages)
        Thread s1 = new Thread(new MessageSender(queue, "Sender-1"));
        Thread s2 = new Thread(new MessageSender(queue, "Sender-2"));
        Thread s3 = new Thread(new MessageSender(queue, "Sender-3"));

        // 3 consumers (So each consumer consumes 5 messages each and prints the messages)
        Thread r1 = new Thread(new MessageReceiver(queue, "Receiver-1"));
        Thread r2 = new Thread(new MessageReceiver(queue, "Receiver-2"));
        Thread r3 = new Thread(new MessageReceiver(queue, "Receiver-3"));

        // start all
        s1.start(); s2.start(); s3.start();
        r1.start(); r2.start(); r3.start();
    }
}
