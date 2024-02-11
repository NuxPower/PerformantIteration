import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class PerformantIteration {
    private static int theSum = 0;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) {
        String jvmVersion = ManagementFactory.getRuntimeMXBean().getVmVersion();
        System.out.println("JVM Version : " + jvmVersion);
        System.out.println(ANSI_BLUE + "Starting micro-benchmark test");

        List<Integer> nums = new ArrayList<Integer>();
        for (int i = 0; i < 50000; i++) {
            nums.add(i);
        }

        System.out.println(ANSI_BLUE + "Warming up ...");
        for (int i = 0; i < 10; i++) {
            iterateWithConstantSize(nums);
            iterateWithDynamicSize(nums);
        }

        System.out.println(ANSI_RED + "Starting the actual test");

        long startTime = System.nanoTime();
        long constantSizeBenchmark = iterateWithConstantSize(nums);
        long endTime = System.nanoTime();
        long constantSizeDuration = endTime - startTime;

        startTime = System.nanoTime();
        long dynamicSizeBenchmark = iterateWithDynamicSize(nums);
        endTime = System.nanoTime();
        long dynamicSizeDuration = endTime - startTime;

        System.out.println(ANSI_BLUE + "Test completed... printing results");

        System.out.println(ANSI_YELLOW + "constantSizeBenchmark : " + constantSizeDuration + " ns");
        System.out.println(ANSI_YELLOW + "dynamicSizeBenchmark : " + dynamicSizeDuration + " ns");
        System.out.println(ANSI_RED + "dynamicSizeBenchmark/constantSizeBenchmark : " +
                ((double) dynamicSizeDuration / (double) constantSizeDuration) + ANSI_RESET);
    }

    private static long iterateWithDynamicSize(List<Integer> nums) {
        int sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < nums.size(); i++) {
            sum += nums.get(i);
        }
        long end = System.nanoTime();
        setSum(sum);
        return end - start;
    }

    private static long iterateWithConstantSize(List<Integer> nums) {
        int count = nums.size();
        int sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            sum += nums.get(i);
        }
        long end = System.nanoTime();
        setSum(sum);
        return end - start;
    }

    // Invocations to this method simply exist to fool the VM into
    // thinkinxg that we are doing something useful in the loop
    private static void setSum(int sum) {
        theSum = sum++;
    }
}
