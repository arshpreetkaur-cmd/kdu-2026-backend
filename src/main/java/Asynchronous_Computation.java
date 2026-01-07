import java.util.concurrent.*;

class sum implements Callable<Integer>{
    private final int n;

    public sum(int n){
        this.n = n;
    }

    @Override
    public Integer call(){
        int sum = 0;
        for(int i = 0; i < n; i++){
            sum += i;
        }
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

        //Everything is BLOCKED from here until the task finishes
        //Retrieving the result from Future
        Integer result = future.get();

        //shutting down the executor
        executorService.shutdown();

        System.out.println("Sum from 1 to 10 : " + result);
    }
}
