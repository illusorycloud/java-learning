package netty.heartbeatproduct;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * 生产级心跳机制
 * 重连检测狗
 * 当发现当前的链路不稳定关闭之后，进行最多12次重连
 * 用在客户端
 *
 * @author illusoryCloud
 */
@ChannelHandler.Sharable
public abstract class BaseConnectionWatchdog extends ChannelHandlerAdapter implements TimerTask, ChannelHandlerHolder {
    private final Bootstrap bootstrap;
    private final Timer timer;
    private final int port;

    private final String host;

    private volatile boolean reconnect = true;
    /**
     * 最大重连次数
     */
    private static final int MAX_TRY_TIMES = 12;
    /**
     * 当前已尝试重连次数
     * 重连成功后置0
     */
    private int tryTimes;

    public BaseConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port, String host, boolean reconnect) {
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新☞ 0
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("当前链路已经激活了，重连尝试次数重新置为0");

        tryTimes = 0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("链接关闭");
        if (reconnect) {
            System.out.println("链接关闭，将进行重连");
            if (tryTimes < MAX_TRY_TIMES) {
                tryTimes++;
                //重连的间隔时间会越来越长
                int timeout = 2 << tryTimes;
                timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);
            }
        }
        ctx.fireChannelInactive();
    }

    /**
     * 定时任务
     * 做的事情就是重连的工作
     *
     * @param timeout 重连超时时间
     * @throws Exception ex
     */
    @Override
    public void run(Timeout timeout) throws Exception {

        ChannelFuture future;
        //bootstrap已经初始化好了，只需要将handler填入就可以了
        synchronized (bootstrap) {
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(handlers());
                    ChannelHandler[] handlers = handlers();
                    System.out.println(handlers.length);
                }
            });
            future = bootstrap.connect(host, port);
        }
        //future对象
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                //如果重连失败，则调用ChannelInactive方法，再次出发重连事件
                // 一直尝试12次，如果失败则不再重连
                if (!f.isSuccess()) {
                    System.out.println("重连失败");
                    //重连失败后 再次触发ChannelInactive() 继续重连
                    f.channel().pipeline().fireChannelInactive();
                } else {
                    System.out.println("重连成功");
                }
            }
        });

    }
}
