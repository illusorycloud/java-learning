package zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class ZKWatcher implements Watcher {
    /**
     * Zookeeper
     */
    private ZooKeeper zk = null;
    /**
     * 父节点path
     */
    static final String PARENT_PATH = "/super";
    /**
     * 信号量设置 用于zk连接上后 通知程序继续执行
     */
    private CountDownLatch waitConn = new CountDownLatch(1);
    private List<String> cowaList = new ArrayList<>();
    /**
     * ZooKeeper地址
     */
    static final String CONN_ADDR = "192.168.5.154:2181,192.168.5.155:2181,192.168.5.156:2181";
    /**
     * session超时时间ms
     */
    static final int SESSION_TIMEOUT = 30000;

    public ZKWatcher(String s) throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper(CONN_ADDR, SESSION_TIMEOUT, this);
        System.out.println("开始连接ZooKeeper");
        waitConn.await();
        zk.getChildren(PARENT_PATH, this);
    }

    public ZKWatcher() {
    }

    @Override
    public void process(WatchedEvent event) {
        // 事件类型
        Event.EventType type = event.getType();
        // 连接状态
        Event.KeeperState state = event.getState();
        // 受影响的path
        String path = event.getPath();
        switch (state) {
            case SyncConnected:
                System.out.println("state: SyncConnected");
                System.out.println("path: " + path);
                waitConn.countDown();
                break;
            case Disconnected:
                System.out.println("state: Disconnected");
                System.out.println("path: " + path);
                break;
            case AuthFailed:
                System.out.println("state: AuthFailed");
                System.out.println("path: " + path);
                break;
            case Expired:
                System.out.println("state: Expired");
                System.out.println("path: " + path);
                break;
            default:
                System.out.println("state: default");
        }
        System.out.println("------------------------");
        switch (type) {
            case None:
                System.out.println("type: None");
                System.out.println("path: " + path);
                break;
            case NodeCreated:
                System.out.println("type: NodeCreated");
                System.out.println("path: " + path);
                break;
            case NodeDataChanged:
                System.out.println("type: NodeDataChanged");
                System.out.println("path: " + path);
                break;
            case DataWatchRemoved:
                System.out.println("type: DataWatchRemoved");
                System.out.println("path: " + path);
                break;
            case ChildWatchRemoved:
                System.out.println("type:child watch被移除");
                System.out.println("path: " + path);
                break;
            case NodeChildrenChanged:
                System.out.println("type: NodeChildrenChanged");
                System.out.println("path: " + path);
                break;
            case NodeDeleted:
                System.out.println("type: NodeDeleted");
                System.out.println("path: " + path);
                break;
            default:
                System.out.println("type: default");
        }
        System.out.println("------------------------");
    }


}
