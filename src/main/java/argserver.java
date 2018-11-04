import java.util.concurrent.*;

public class argserver {

    public static void main(String[] args) throws InterruptedException, ExecutionException {


        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(StartFactory);

        new StartSocket();

    }

    static Callable StartFactory = () -> {
        StoreInquiry storeInquiry = new StoreInquiry() ;
        storeInquiry.inquiryOrder();
        return true;
    };
}
