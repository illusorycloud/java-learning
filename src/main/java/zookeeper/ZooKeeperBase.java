package zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author illusory
 */
public class ZooKeeperBase {
    /**
     * ZooKeeper地址
     */
    static final String CONN_ADDR = "192.168.5.154:2181,192.168.5.155:2181,192.168.5.156:2181";
    /**
     * session超时时间ms
     */
    static final int SESSION_TIMEOUT = 5000;
    /**
     * wait for zk connect
     */
    static final CountDownLatch waitZooKeeperConnOne = new CountDownLatch(1);
    private ZooKeeper zooKeeper;


    @Before
    public void before() throws IOException {
        /**
         * zk客户端
         * 参数1 connectString 连接服务器列表，用逗号分隔
         * 参数2 sessionTimeout 心跳检测时间周期 毫秒
         * 参数3 watcher 事件处理通知器
         * 参数4 canBeReadOnly 标识当前会话是否支持只读
         * 参数5 6 sessionId sessionPassword通过这两个确定唯一一台客户端 目的是提供重复会话
         */
        zooKeeper = new ZooKeeper(CONN_ADDR, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //获取事件状态与类型
                Event.KeeperState state = watchedEvent.getState();
                Event.EventType type = watchedEvent.getType();
                //如果是建立连接成功
                if (Event.KeeperState.SyncConnected == state) {
                    //刚连接成功什么都没有所以是None
                    if (Event.EventType.None == type) {
                        //连接成功则发送信号 让程序继续执行
                        waitZooKeeperConnOne.countDown();
                        System.out.println("ZK 连接成功");
                    }
                }
            }
        });
    }

    @Test
    public void testCreate() throws IOException, InterruptedException, KeeperException {
        waitZooKeeperConnOne.await();
        System.out.println("zk start");
        //创建简介
        // 参数1 key
        // 参数2 value  参数3 一般就是ZooDefs.Ids.OPEN_ACL_UNSAFE
        // 参数4 为节点模式 有临时节点(本次会话有效，分布式锁就是基于临时节点)或者持久化节点
        // 返回值就是path 节点已存在则报错NodeExistsException

/**
 * 同步方式
 *
 * 参数1 path 可以看成是key  原生Api不能递归创建 不能在没父节点的情况下创建子节点的，会抛出异常
 *     框架封装也是通过if一层层判断的 如果父节点没有 就先给你创建出来 这样实现的递归创建
 * 参数2 data 可以看成是value 要求是字节数组 也就是说不支持序列化
 *      如果要序列化可以使用一些序列化框架 Hessian Kryo等
 * 参数3 节点权限 使用ZooDefs.Ids.OPEN_ACL_UNSAFE开放权限即可
 *      在权限没有太高要求的场景下 没必要关注
 * 参数4  节点类型 创建节点的类型 提供了多种类型
 *             CreateMode.PERSISTENT     持久节点
 *             CreateMode.PERSISTENT_SEQUENTIAL  持久顺序节点
 *             CreateMode.EPHEMERAL       临时节点
 *             CreateMode.EPHEMERAL_SEQUENTIAL   临时顺序节点
 *             CreateMode.CONTAINER
 *             CreateMode.PERSISTENT_WITH_TTL
 *             CreateMode.PERSISTENT_SEQUENTIAL_WITH_TTL
 */
//        String s = zooKeeper.create("/illusory", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //illusory
//        System.out.println(s);
        //原生Api不能递归创建 不能在没父节点的情况下创建子节点的
        //框架封装也是同过if判断的 如果父节点没有 就先给你创建出来 这样实现的递归创建
//        zooKeeper.create("/illusory/testz/zzz", "testzz".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println();
/**
 * 异步方式
 * 在同步基础上多加两个参数
 *
 * 参数5 注册一个回调函数 要实现AsyncCallback.Create2Callback()重写processResult(int rx, String path, Object ctx, String name, Stat stat)方法
 *   processResult参数1  int rx为服务端响应码 0表示调用成功 -4表示端口连接 -110表示指定节点存在 -112表示会话已过期
 *                参数2 String path 节点调用时传入Api的数据节点路径
 *                参数3 Object ctx 调用接口时传入的ctx值
 *                参数4 String name 实际在服务器创建节点的名称
 *                参数5 Stat stat 被创建的那个节点信息
 *
 */
        zooKeeper.create("/illusory/testz/zzz/zzz/aa", "testzz".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT
                , (rc, path, ctx, name, stat) -> {
                    System.out.println(stat.getAversion());
                    System.out.println(rc);
                    System.out.println(path);
                    System.out.println(ctx);
                }, "s");

        System.out.println("继续执行");

        Thread.sleep(1000);

        byte[] data = zooKeeper.getData("/illusory", false, null);
        System.out.println(new String(data));

    }

    @Test
    public void testGet() throws KeeperException, InterruptedException {
        waitZooKeeperConnOne.await();
//        zooKeeper.create("/illusory","root".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zooKeeper.create("/illusory/aaa","aaa".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zooKeeper.create("/illusory/bbb","aaa".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zooKeeper.create("/illusory/ccc","aaa".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //不支持递归 只能取下面的一层
        List<String> children = zooKeeper.getChildren("/illusory", false);
        for (String s : children) {
            //拼接绝对路径
            String realPath = "/illusory/" + s;
            byte[] data = zooKeeper.getData(realPath, false, null);
            System.out.println(new String(data));
        }

    }

    @Test
    public void testSet() throws KeeperException, InterruptedException {
        waitZooKeeperConnOne.await();
        zooKeeper.setData("/illusory/aaa", "new AAA".getBytes(), -1);
        zooKeeper.setData("/illusory/bbb", "new BBB".getBytes(), -1);
        zooKeeper.setData("/illusory/ccc", "new CCC".getBytes(), -1);
        testGet();
    }

    @Test
    public void testDelete() throws KeeperException, InterruptedException {
        waitZooKeeperConnOne.await();
        zooKeeper.delete("/illusory/aaa", -1);
        testGet();
    }

    @Test
    public void testExists() throws KeeperException, InterruptedException {
        waitZooKeeperConnOne.await();
        //判断节点是否存在 没有就是null 有的话会返回一长串12884901923,12884901933,1552027900801,1552028204414,1,0,0,0,7,0,12884901923
        Stat exists = zooKeeper.exists("/illusory/bbb", null);
        System.out.println(exists);
    }


}
