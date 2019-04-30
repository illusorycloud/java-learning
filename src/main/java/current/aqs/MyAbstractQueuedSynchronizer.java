package current.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 自定义同步器
 */
public class MyAbstractQueuedSynchronizer extends AbstractQueuedSynchronizer {
    /**
     * 该线程是否正在独占资源。只有用到condition才需要去实现它。
     *
     * @return
     */
    @Override
    protected boolean isHeldExclusively() {
        return super.isHeldExclusively();
    }

    /**
     * 独占方式。尝试获取资源，成功则返回true，失败则返回false。
     *
     * @param arg
     * @return
     */
    @Override
    protected boolean tryAcquire(int arg) {
        return super.tryAcquire(arg);
    }

    /**
     * 独占方式。尝试释放资源，成功则返回true，失败则返回false。
     *
     * @param arg
     * @return
     */
    @Override
    protected boolean tryRelease(int arg) {
        return super.tryRelease(arg);
    }

    /**
     * 共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
     *
     * @param arg
     * @return
     */
    @Override
    protected int tryAcquireShared(int arg) {
        return super.tryAcquireShared(arg);
    }

    /**
     * 共享方式。尝试释放资源，成功则返回true，失败则返回false。
     *
     * @param arg
     * @return
     */
    @Override
    protected boolean tryReleaseShared(int arg) {
        return super.tryReleaseShared(arg);
    }


}
