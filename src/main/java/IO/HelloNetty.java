package IO;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * Netty入门一
 *
 * @author illusoryCloud
 */
public class HelloNetty {
    private static final int PORT=33332;
    @Test
    public void test1() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<ServerChannel>() {
                        @Override
                        protected void initChannel(ServerChannel channel) throws Exception {
                            System.out.println("connect...client: " + channel.remoteAddress());
                            channel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            // 服务器异步创建绑定
            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println(HelloNetty.class + " started and listen on " + channelFuture.channel().localAddress());
            // 关闭服务器通道
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放线程池资源
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void client(){
        NioEventLoopGroup group=new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("127.0.0.1",PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println("connect...");
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            System.out.println("created");
            //异步连接服务器
            ChannelFuture channelFuture = bootstrap.connect().sync();
            System.out.println("connect..");

            //异步等待关闭channel
            channelFuture.channel().closeFuture().sync();
            System.out.println("closed...");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                //释放线程资源
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
