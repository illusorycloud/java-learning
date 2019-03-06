package jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
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
    @Test
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
}
