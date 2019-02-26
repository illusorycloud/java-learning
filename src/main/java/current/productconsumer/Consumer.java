package current.productconsumer;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者模式 消费者
 *
 * @author illusoryCloud
 */
public class Consumer implements Runnable {
    /**
     * 队列
     */
    private Queue<Message> queue;
    /**
     * 可重入锁 生产者与消费者共用同一把锁
     */
    private ReentrantLock reentrantLock;
    /**
     * 条件变量 队列为空时
     */
    private Condition emptyWait;
    /**
     * 条件变量 队列满时
     */
    private Condition fullWait;

    public Consumer(Queue<Message> queue, ReentrantLock reentrantLock, Condition emptyWait, Condition fullWait) {
        this.queue = queue;
        this.reentrantLock = reentrantLock;
        this.emptyWait = emptyWait;
        this.fullWait = fullWait;
    }

    @Override
    public void run() {
        //无限循环
        while (true) {
            try {
                //每1000ms执行一次 即消费一个消息
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //获取锁
            reentrantLock.lock();
            try {
                //消费前先判断一下队列是否为空
                if (queue.isEmpty()) {
                    try {
                        //为空就让消费者线程在emptyWait上等待 直到被生产者唤醒
                        emptyWait.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //poll 移除队列头部元素 即消费一个消息
                Message poll = queue.poll();
                System.out.println(Thread.currentThread().getName() + "消费   " + "剩余消息数：" + queue.size());
                //消费消息后唤醒在fullWait上等待的线程(即因为队列满了而等待的消费者线程)
                fullWait.signalAll();
            } finally {
                //在finally中释放锁 确保出异常后也能释放锁
                reentrantLock.unlock();
            }

        }
    }


}

