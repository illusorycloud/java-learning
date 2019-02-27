package netty.heartbeat;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 心跳机制 客户端启动器
 * @author illusoryCloud
 */
public class HeartBeatClient {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {
                port = 8080;
            }
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    new HeartBeatClient().connect("127.0.0.1", 8080);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //开启多个客户端
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        Thread t3 = new Thread(r);

        t1.start();
        t2.start();
        t3.start();

    }

    private void connect(String host, int port) throws InterruptedException {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        ChannelFuture future = null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 设置超时时间
                            ch.pipeline().addLast("ping", new IdleStateHandler(0, 20, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new HeartBeatClientHandler());
                        }
                    });
            future = b.connect(host, port).sync();
            //添加一个监听 连接成功则做出提示
            future.addListener(future1 -> {
                if (future1.isSuccess()) {
                    System.out.println("连接成功~");
                }
            });
            //正常情况下会一直阻塞在这里 如果出现异常就会执行finally代码块
            //这里不再finally中关闭group 而是尝试重连
            future.channel().closeFuture().sync();
        } finally {
            //不关闭 尝试重连
//            loopGroup.shutdownGracefully();
            if (null != future) {
                if (future.channel() != null && future.channel().isOpen()) {
                    future.channel().close();
                }
            }
            System.out.println("开始重连~");
            //重新连接
            connect(host, port);

        }
    }
}
