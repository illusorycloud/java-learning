package current;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程a负责掷骰子，每次掷一个骰子，
 * 线程b负责把每次骰子的数字都输出到屏幕上
 * 如果骰子是连续的3个6，则线程a与线程b都退出
 *
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/15
 */
public class ThreadTest {
    private static Logger logger = LoggerFactory.getLogger(ThreadTest.class);
    //每次骰子的点数
    private static int number;
    //掷骰子总次数
    private static AtomicInteger times = new AtomicInteger();
    //目标点数
    private static final int TARGET_NUMBER = 60;
    //骰子点数最大范围 默认为6
    private static final Integer MAX_SIZE = 100;

    public static void main(String[] args) {
        //创建一个临界资源o
        Object o = new Object();
        //创建一个Integer集合，每次掷出来的骰子都存放在这个集合里。
        List<Integer> list = new ArrayList<>();
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                //对o这个临界资源加锁
                synchronized (o) {
                    //因为不知道要掷多少次骰子，才能达到标准，所以使用while(true)
                    while (true) {
                        //唤醒所有等待的线程，让等待的线程进入就绪状态 这里唤醒的是线程B
                        o.notifyAll();
                        //设置随机数 并添加到集合中
                        Random random = new Random();
                        number = random.nextInt(MAX_SIZE) + 1;
                        list.add(number);
                        //判断是否连续3个6 满足则退出
                        if (list.size() > 3 && list.get(list.size() - 1) == TARGET_NUMBER && list.get(list.size() - 2) == TARGET_NUMBER && list.get(list.size() - 3) == TARGET_NUMBER) {
                            return;
                        }
                        //否则等待
                        else {
                            try {
                                o.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                //对o这个临界资源加锁
                synchronized (o) {
                    //因为不知道要掷多少次骰子，才能达到标准，所以使用while(true)
                    while (true) {
                        //唤醒所有等待的线程，让等待的线程进入就绪状态 这里唤醒的是线程A
                        o.notifyAll();
                        logger.info(String.format("number: %s times: %s", number, times.incrementAndGet()));
                        //判断是否连续3个6 满足则退出
                        if (list.size() > 3 && list.get(list.size() - 1) == TARGET_NUMBER && list.get(list.size() - 2) == TARGET_NUMBER && list.get(list.size() - 3) == TARGET_NUMBER) {
                            return;
                        }
                        //否则等待
                        else {
                            try {
                                o.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        //开启线程
        threadA.start();
        threadB.start();
    }
}
