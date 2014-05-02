

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ZequnLi
 *         Date: 14-4-3
 */
public class MemoryMonitor {
    private final long max = Runtime.getRuntime().maxMemory();
    private  final long interval;
    private long counter= 0;
    private AtomicReference<Boolean> flag = new AtomicReference<Boolean>();
    private long inputNum;
    private final Object lock = new Object();
    public MemoryMonitor(long interval) throws SQLException {
        this.interval = interval;

        flag.set(true);
    }


    public void inputRateCheck(){
        synchronized(lock){
            inputNum++;
        }
    }

    public void start(){
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (flag.get()){
                long free = Runtime.getRuntime().freeMemory();
                System.out.println("Max Heap Size: " + B2M.byte2M(max));
                System.out.println("Current Free size: "+B2M.byte2M(free));
                counter++;

                synchronized (lock){
                    System.out.println("input rate: "+ (double)inputNum/(interval/1000));
                    inputNum =0;
                }

                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void flushLog(){
        flag.set(false);

    }
}
