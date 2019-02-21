package netty.easychat;


import java.net.SocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * SimpleChannelInboundHandler 实现了ChannelInboundHandlerAdapter并添加了一些功能
 * @author illusoryCloud
 */
public class EasyChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 把所有连上来的客户端channel都存在里面
     * 有人发送消息时循环遍历 给每个客户端都发一遍
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 有新客户端连接时回调
     *
     * @param ctx ChannelHandlerContext代表了ChannelPipeline和ChannelHandler之间的关联，是ChannelHandler之间信息传递的桥梁。
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        channels.writeAndFlush("[SERVER] - " + socketAddress + " 进入聊天室\n");
        channels.add(channel);
    }

    /**
     * 客户端断开连接时回调
     *
     * @param ctx 同上
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        channels.writeAndFlush("[SERVER] - " + socketAddress + " 退出聊天室\n");
        channels.remove(channel);
    }

    /**
     * 服务端监听到客户端-活动-时回调
     *
     * @param ctx 同上
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println(socketAddress + "在线\n");

    }

    /**
     * 服务端监听到客户端-不活动-时回调
     *
     * @param ctx 同上
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println(socketAddress + "离线\n");
    }

    /**
     * 出现异常时回调
     *
     * @param ctx   同上
     * @param cause 异常信息
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println(socketAddress + "异常\n");
        ctx.close();
        cause.printStackTrace();
    }

    /**
     * 服务端读到客户端写入信息时回调
     * 将消息转发给其他客户端
     *
     * @param channelHandlerContext 同时
     * @param s                     收到的消息
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel channel = channelHandlerContext.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        //循环遍历转发消息给所有的客户端
        for (Channel c : channels) {
            //如果是自己发的消息就显示你说了什么什么
            if (c == channel) {
                c.writeAndFlush("you：" + s + "\n");
                //如果不是就显示 XXX(地址)说了什么
            } else {
                c.writeAndFlush(socketAddress + ": " + s + "\n");
            }
        }
    }
}