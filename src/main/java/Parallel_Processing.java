import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parallel_Processing {
    public static void main(String[] args) throws Exception {

        //Generating a list of 1,000,000 integers
        List<Integer> numbers = IntStream.rangeClosed(1, 1_000_000).
                boxed().
                collect(Collectors.toList());

        //Computing sum using Sequential Stream and also finding the time
        long startSeq = System.nanoTime();
        long seqSum = numbers.stream()
                .mapToLong(Integer::longValue)
                .sum();
        long endSeq = System.nanoTime();

        //Computing sum using Parallel Stream and also finding the time
        long startPar = System.nanoTime();
        long parSum = numbers.parallelStream()
                .mapToLong(Integer::longValue)
                .sum();
        long endPar = System.nanoTime();

        // Convert nano to ms
        long sequentialTime = (endSeq - startSeq) / 1_000_000;
        long partialTime = (endPar - startPar) / 1_000_000;

        // Print results
        System.out.println("Sequential sum = " + seqSum + ", time = " + sequentialTime + " ms");
        System.out.println("Parallel   sum = " + parSum + ", time = " + partialTime + " ms");
    }
}
