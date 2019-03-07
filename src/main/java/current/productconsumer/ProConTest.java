package current.productconsumer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者模式 测试
 * 基于ReentrantLock的实现
 * 详解http://ifeve.com/producers-and-consumers-mode/
 *
 * @author illusoryCloud
 */
public class ProConTest {
    public static void main(String[] args) {
        //ReentrantLock 重入锁
        ReentrantLock reentrantLock = new ReentrantLock();
        //Condition 队列为空时让消费者在这个Condition上等待
        Condition emptyWait = reentrantLock.newCondition();
        //Condition 队列满时时让生产者在这个Condition上等待
        Condition fullWait = reentrantLock.newCondition();
        //队列最大容量
        Integer Captial = 10;
        //存消息的队列
        Queue<Message> queue = new ConcurrentLinkedQueue<Message>();

        //生产者
        Product p = new Product(queue, Captial, reentrantLock, emptyWait, fullWait);
        //开启一个生产者线程
        Thread p1 = new Thread(p);
        p1.start();

        //消费者
        Consumer c = new Consumer(queue, reentrantLock, emptyWait, fullWait);
        //开启两个消费者线程
        Thread c1 = new Thread(c);
        Thread c2 = new Thread(c);
        Thread c3 = new Thread(c);
        c1.start();
        c2.start();
        c3.start();
    }

}