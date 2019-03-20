package zookeeper;

import org.I0Itec.zkclient.*;
import org.apache.zookeeper.Watcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * zkClient测试
 *
 * @author illusoryCloud
 */
public class zkCLientTest {
    /**
     * ZooKeeper地址
     */
//    static final String CONN_ADDR = "192.168.5.154:2181,192.168.5.155:2181,192.168.5.156:2181";
    static final String CONN_ADDR = "192.168.1.111:2181,192.168.1.112:2181,192.168.1.113:2181";
    /**
     * session超时时间ms
     */
    static final int SESSION_TIMEOUT = 5000;
    private ZkClient zkClient;

    @Before
    public void before() {
        zkClient = new ZkClient(new ZkConnection(CONN_ADDR, SESSION_TIMEOUT));
    }

    @After
    public void after() {
        zkClient.close();
    }

    @Test
    public void testOne() {
        zkClient.createEphemeral("/test", true);
        //可以递归创建 只能创建key 不能直接设置value
        zkClient.createPersistent("/super/c1", true);
        zkClient.writeData("/super/c1", "c1");
        String o = zkClient.readData("/super/c1");
        System.out.println(o);
        zkClient.writeData("/super/c1", "新的内容");
        System.out.println(zkClient.exists("/super/c1"));
        System.out.println(zkClient.readData("/super/c1").toString());
    }

    @Test
    public void testWatch() throws InterruptedException {
        //监听/super节点的子节点增加或删除的变化
        zkClient.subscribeChildChanges("/super", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> childs) throws Exception {
                System.out.println("parent path: " + parentPath);
                System.out.println("childs : " + childs);
            }
        });

        zkClient.createEphemeral("/super/c2", "c2");
        zkClient.createEphemeral("/super/c3", "c3");
        zkClient.createEphemeral("/super/c4", "c4");

        List<String> children = zkClient.getChildren("/super");
        for (String s : children
        ) {
            System.out.println(zkClient.readData("/super/" + s).toString());
        }
        //监听/super节点的数据变化 数据变化和节点被删除都走这个
        zkClient.subscribeDataChanges("/super", new IZkDataListener() {
            @Override
            public void handleDataChange(String path, Object o) throws Exception {
                System.out.println("变更的节点为：" + path + "变更的内容为：" + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("删除的节点为：" + s);

            }
        });
        Thread.sleep(1000);
        zkClient.writeData("/super", "new super", -1);
//        zkClient.delete("/super/c1");
//        zkClient.delete("/super/c2");
//        zkClient.delete("/super/c3");
//        zkClient.delete("/super/c4");
//        zkClient.delete("/super");

        //订阅节点连接及状态的变化情况
        zkClient.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                System.out.println("节点连接及状态变化：" + keeperState.name());
            }

            @Override
            public void handleNewSession() throws Exception {
                System.out.println("节点Session变化。。。");
            }

            @Override
            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {

            }
        });
    }

}
