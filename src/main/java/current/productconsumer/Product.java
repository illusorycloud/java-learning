package current.productconsumer;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者模式 生产者
 * @author illusoryCloud
 */
public class Product implements Runnable {
    /**
     * 队列
     */
    private Queue<Message> queue;
    /**
     * 队列最大容量
     */
    private Integer queueCapacity;
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

    public Product(Queue<Message> queue, Integer capaitly, ReentrantLock reentrantLock, Condition emptyWait, Condition fullWait) {
        this.queue = queue;
        queueCapacity = capaitly;
        this.reentrantLock = reentrantLock;
        this.emptyWait = emptyWait;
        this.fullWait = fullWait;
    }

    @Override
    public void run() {
        //无限循环
        while (true) {
            try {
                //每300ms执行一次 即生产一个消息
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //获取锁
            reentrantLock.lock();
            try {
                //生产前先判断一下队列是否满了
                if (queue.size() == queueCapacity) {
                    try {
                        //满了就让生产者线程在fullWait上等待 直到被消费者唤醒
                        fullWait.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //没满就继续生产消息
                Message message = Message.newBuilder()
                        .setTitle("start")
                        .setContent("no content")
                        .setTail("end")
                        .setTime(new Date())
                        .Build();
                //放入队列
                queue.add(message);
                System.out.println(Thread.currentThread().getName() + "生产   " + "现有消息数：" + queue.size());
                //生产消息后唤醒在emptyWait上等待的线程(即因为队列为空而等待的消费者线程)
                emptyWait.signalAll();
            } finally {
                //在finally中释放锁 确保出异常后也能释放锁
                reentrantLock.unlock();
            }

        }
    }

}