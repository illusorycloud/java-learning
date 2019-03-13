package jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jedis.domain.User;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.*;

/**
 * Jedis测试
 *
 * @author illusoryCloud
 */
public class JedisTest {
    @Test
    public void jedisTestOne() {
        Jedis jedis = new Jedis("192.168.1.111", 6379);

        //-------------string----------------
        String set = jedis.set("name", "illusorycloud");
        //ok
        System.out.println(set);
        String name = jedis.get("name");
        //illusorycloud
        System.out.println(name);
        //-------------hash----------------
        Map<String, String> map = new HashMap<>();
        map.put("name", "illusory");
        map.put("age", "30");
        map.put("address", "cq");
        jedis.hmset("user", map);

        JedisPool pool = jedisPoolTest();
        //从连接池中获取一个Jedis实例
        Jedis j = pool.getResource();
        j.set("test", "111");
    }

    /**
     * 方法描述 构建redis连接池
     * 池子中存放着多个jedis实例
     */
    public JedisPool jedisPoolTest() {

        JedisPool pool = null;
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxTotal(50);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；单位毫秒
            //小于零:阻塞不确定的时间,  默认-1
            config.setMaxWaitMillis(1000 * 100);
            //在borrow(引入)一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            //return 一个jedis实例给pool时，是否检查连接可用性（ping()）
            config.setTestOnReturn(true);
            //connectionTimeout 连接超时（默认2000ms）
            //soTimeout 响应超时（默认2000ms）
            pool = new JedisPool(config, "127.0.0.1", 6379, 2000, "619868");
        }
        return pool;
    }


    /**
     * User对象 数据量很大且查询频繁 需要把user表中的数据都放入缓存
     */
    @Test
    public void jedisTestTwo() {
        Jedis jedis = new Jedis("192.168.5.154", 6379);

        //1.做放入操作
        Map<String, String> map = new HashMap<>();
        String uid1 = "illusory" + UUID.randomUUID().toString();
        User u1 = new User(uid1, "illusoryCloud", 22, "man");
        map.put(uid1, JSON.toJSONString(u1));

        String uid2 = "illusory" + UUID.randomUUID().toString();
        User u2 = new User(uid2, "Java", 23, "women");
        map.put(uid2, JSON.toJSONString(u2));
        String uid3 = "illusory" + UUID.randomUUID().toString();
        User u3 = new User(uid3, "Android", 24, "man");
        map.put(uid3, JSON.toJSONString(u3));

        String uid4 = "illusory" + UUID.randomUUID().toString();
        User u4 = new User(uid4, "iOS", 25, "women");
        map.put(uid4, JSON.toJSONString(u4));

        String uid5 = "illusory" + UUID.randomUUID().toString();
        User u5 = new User(uid5, "Python", 26, "man");
        map.put(uid5, JSON.toJSONString(u5));
        jedis.hmset("SYS_USER_TABLE", map);
        //假如这里放入了1000W条数据
        //如何按条件查询
        //select * from user where set='women'
        //select * from user where set='women' and age=25
        //很明显是做不到的
        //一般持久化时都是多种数据类型配合使用 hash+set
        //详情见jedsiTestThree();

    }

    //指定业务 查询业务 SYS_USER_SEL_AGE_25
    //指定业务 查询业务 SYS_USER_SEL_SEX_MAN
    //指定业务 查询业务 SYS_USER_SEL_SEX_WOMEN
    private final String SYS_USER_TABLE = "SYS_USER_TABLE";
    private final String SYS_USER_SEL_AGE_25 = "SYS_USER_SEL_AGE_25";
    private final String SYS_USER_SEL_SEX_MAN = "SYS_USER_SEL_SEX_MAN";
    private final String SYS_USER_SEL_SEX_WOMEN = "SYS_USER_SEL_SEX_WOMEN";

    @Test
    public void jedsiTestThree() {
        Jedis jedis = new Jedis("192.168.5.154", 6379);
        //写入数据时往多个集合中写
        //1.做放入操作
        Map<String, String> map = new HashMap<>();
        String uid1 = "illusory" + UUID.randomUUID().toString();
        User u1 = new User(uid1, "illusoryCloud", 22, "man");
        //这里满足多种条件时 每个集合都存一次
        map.put(uid1, JSON.toJSONString(u1));
        jedis.sadd(SYS_USER_SEL_SEX_MAN, uid1);

        String uid2 = "illusory" + UUID.randomUUID().toString();
        User u2 = new User(uid2, "Java", 23, "women");
        map.put(uid2, JSON.toJSONString(u2));
        jedis.sadd(SYS_USER_SEL_SEX_WOMEN, uid2);

        String uid3 = "illusory" + UUID.randomUUID().toString();
        User u3 = new User(uid3, "Android", 24, "man");
        map.put(uid3, JSON.toJSONString(u3));
        jedis.sadd(SYS_USER_SEL_SEX_MAN, uid3);

        String uid4 = "illusory" + UUID.randomUUID().toString();
        User u4 = new User(uid4, "iOS", 25, "women");
        map.put(uid4, JSON.toJSONString(u4));
        jedis.sadd(SYS_USER_SEL_SEX_WOMEN, uid4);
        jedis.sadd(SYS_USER_SEL_AGE_25, uid4);

        String uid5 = "illusory" + UUID.randomUUID().toString();
        User u5 = new User(uid5, "Python", 26, "man");
        map.put(uid5, JSON.toJSONString(u5));
        jedis.hmset(SYS_USER_TABLE, map);
        jedis.sadd(SYS_USER_SEL_SEX_MAN, uid5);
        //select * from user where set='women'
        //select * from user where set='women' and age=25
    }

    @Test
    public void select() {
        Jedis jedis = new Jedis("192.168.5.154", 6379);
        //select * from user where set='women'
        System.out.println("------------select * from user where set='women'--------");

        //查出所有women的id
        Set<String> userWomenId = jedis.smembers(SYS_USER_SEL_SEX_WOMEN);
        //再通过id查询user
        for (Iterator iterator = userWomenId.iterator(); iterator.hasNext(); ) {
            String next = (String) iterator.next();
            String userString = jedis.hget(SYS_USER_TABLE, next);
            //string-->json-->user
            JSON userJson = (JSON) JSONObject.parse(userString);
            User user = JSON.toJavaObject(userJson, User.class);
            System.out.println("userName: " + user.getName());
            System.out.println(userJson);
        }
        System.out.println("------------select * from user where set='women' and age=25--------");
        //select * from user where set='women' and age=25
        Set<String> sinter = jedis.sinter(SYS_USER_SEL_AGE_25, SYS_USER_SEL_SEX_WOMEN);
        for (Iterator iterator = sinter.iterator(); iterator.hasNext(); ) {
            String next = (String) iterator.next();
            String userJson = jedis.hget(SYS_USER_TABLE, next);
            System.out.println(userJson);
        }
    }

    @Test
    public void jedisClusterTest() {
        String host = "192.168.5.154";
        Set<HostAndPort> jedisClusterNode = new HashSet<>();
        jedisClusterNode.add(new HostAndPort(host, 7001));
        jedisClusterNode.add(new HostAndPort(host, 7002));
        jedisClusterNode.add(new HostAndPort(host, 7003));
        jedisClusterNode.add(new HostAndPort(host, 7004));
        jedisClusterNode.add(new HostAndPort(host, 7005));
        jedisClusterNode.add(new HostAndPort(host, 7006));
        //jedsi连接池配置
        JedisPoolConfig cfg = new JedisPoolConfig();
        //最大实例数
        cfg.setMaxTotal(100);
        //最大空闲数
        cfg.setMaxIdle(20);
        //最大等待时间 -1 无限
        cfg.setMaxWaitMillis(-1);
        cfg.setTestOnBorrow(true);
        JedisCluster jc = new JedisCluster(jedisClusterNode, 6000, 100, cfg);
        //向单机操作一样 会自动从连接池中拿出一个实例来操作。
        jc.set("name", "illusory");
        jc.set("age", "22");
        jc.set("sex", "man");
        jc.set("addr", "cq");

        System.out.println(jc.set("name", "illusory"));
        System.out.println(jc.set("age", "22"));
        System.out.println(jc.set("sex", "man"));
        System.out.println(jc.set("addr", "cq"));

        System.out.println(jc.get("name"));
    }
}
