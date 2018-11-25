import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;

import static java.lang.String.format;


class StoreInquiry extends Inquiry {

    static ArrayList<String[]> list;
    static boolean Start = false;
    static Map Users, Citys;
    private ScheduledExecutorService executor_inqury = Executors.newScheduledThreadPool(6);
    private StampedLock lock = new StampedLock();
    private  int interval_read = config.INTERVAL;

    void inquiryOrder() throws InterruptedException, ExecutionException, IOException {

        while (!Start) {
            System.out.println("Start");

            ReadUsers User = new ReadUsers(config.UserFile);
            Users = User.read_users();

            ReadUsers City = new ReadUsers(config.CodCity);
            Citys = City.read_users();

            ScheduledFuture<?> fut_init = executor_inqury.schedule(inquiryInit, config.INTERVAL, TimeUnit.SECONDS);
            Start = Boolean.parseBoolean(format("%b",fut_init.get()));
            while (Start) {
                list = new ArrayList<String[]>();
                System.out.println(Calendar.getInstance().getTime());

                ScheduledFuture<?> fut_line = executor_inqury.schedule(inquiryline, config.INTERVAL, TimeUnit.SECONDS);
                prepareLine(String.valueOf(fut_line.get()));

                prepareLine(String.valueOf(executor_inqury.schedule(next, config.INTERVAL, TimeUnit.SECONDS).get()));

                if (!Start) {
                    System.out.println("line - > " + Start);
                    break;
                }

                ScheduledFuture<?> fut_spd = executor_inqury.schedule(inquiryshpd, config.INTERVAL, TimeUnit.SECONDS);
                String sav = String.valueOf(fut_spd.get());

                prepareShpd(String.valueOf(executor_inqury.schedule(previous, config.INTERVAL, TimeUnit.SECONDS).get()));

                prepareShpd(sav);

                if (!Start) {
                    System.out.println("Shpd - > " + Start);
                    break;
                }
                ScheduledFuture<?> fut_send = executor_inqury.schedule(inquirySend, config.INTERVAL, TimeUnit.SECONDS);
                System.out.println("send-> " + fut_send.get());

                StartSocket.start_soc.notifyObs();
                Start = calendar_read.func(config.START, config.STOP);
                System.out.println("Состояние=" + Start + " Пауза=" + interval_read);
                TimeUnit.MINUTES.sleep( interval_read);
                System.out.println("Init=" + fut_init.isDone() + " line=" + fut_line.isDone() + " Shpd=" + fut_spd.isDone() + " Send=" + fut_send.isDone());
            }
            Start = shutdriver();
            System.out.println("Stop!");
            while (!calendar_read.func(config.START, config.STOP));

            StartSocket.his.clear_history();
        }
    }

    private Callable inquiryInit = this::init;
    private Callable inquiryline = this::ReadLine;
    private Callable next = this::ReadLine_Next;
    private Callable inquiryshpd = this::ReadShpd;
    private Callable previous = this::ReadShpd_today;

    private Callable inquirySend = () -> {
        long stamp = lock.writeLock();
        System.out.print("lock " + stamp);
        try {
            listout = StoreInquiry.list;
        } finally {
            lock.unlockWrite(stamp);
        }
        System.out.println(" unlock " + stamp);
        return stamp;
    };

    private test<Boolean, Integer> calendar_read = (t1, t2) -> {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) > t1 & calendar.get(Calendar.HOUR_OF_DAY) < t2 ;
    };
}
