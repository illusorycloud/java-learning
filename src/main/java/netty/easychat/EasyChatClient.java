package netty.easychat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 客户端
 *
 * @author illusoryCloud
 */
public class EasyChatClient {
    private final String host;
    private final int port;

    public EasyChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        try {
            new EasyChatClient("localhost",8080).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new EasyChatClientInitializer());
            //连接
            ChannelFuture f = b.connect(host, port).sync();
            Channel channel = f.channel();
            //从键盘获取输入 然后写入channel
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    channel.writeAndFlush(in.readLine() + "\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
