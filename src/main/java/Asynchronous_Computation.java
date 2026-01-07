import java.util.concurrent.*;

class sum implements Callable<Integer>{
    private final int n;

    public sum(int n){
        this.n = n;
    }

    @Override
    public Integer call() throws Exception {

        System.out.println("Callable task started");

        //artificial delay to show blocking
        Thread.sleep(3000);

        int sum = 0;
        for(int i = 0; i < n; i++){
            sum += i;
        }

        System.out.println("Callable task ended");

        return sum;
    }
}

public class Asynchronous_Computation
{
    public static void main(String[] args) throws Exception {

        //Initialized an Executor Service
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //Submitting the request to find sum from 1 to 10 to Executor Service
        Future<Integer> future = executorService.submit(new sum(10));

        System.out.println("Main thread before calling get()");

        //Everything is BLOCKED from here until the task finishes
        //Retrieving the result from Future
        Integer result = future.get();

        System.out.println("Main thread after calling get()");

        //shutting down the executor
        executorService.shutdown();

        System.out.println("Sum from 1 to 10 : " + result);
    }
}
