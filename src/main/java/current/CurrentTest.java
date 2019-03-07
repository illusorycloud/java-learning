package current;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CurrentTest {
    @Test
    public void CyclicBarrier() {
        /**
         * cyclicBarrier
         * 参数10代表要10个线程准备好了才执行cyclicBarrier.await()后的代码
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + " is ready");
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " is finish");
        };
        // 创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        //开始执行
        for (int i = 0; i < 10; i++) {
            threadPool.execute(r);
        }
    }

    @Test
    public void CountDownLatch() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + " is finish");
            //每完成一个线程让countDownLatch减1 为0时 阻塞在countDownLatch.await();的线程才会继续执行
            countDownLatch.countDown();
        };
        // 创建线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(11, 11, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("mainThread" + " is wait");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("mainThread" + " is finish");
            }
        };
        poolExecutor.execute(runnable);
        //开始执行
        for (int i = 0; i < 10; i++) {
            poolExecutor.execute(r);
        }
    }
}
