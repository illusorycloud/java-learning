package netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.util.Date;

/**
 * 心跳机制 客户端
 * 主要是写空闲超时后发送一个心跳包
 * 没有向服务端写数据会导致写超时 具体时间由自己设置  这里就是4秒
 * ch.pipeline().addLast("ping", new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
 * @author illusoryCloud
 */
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 不释放资源，读取后
     */
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
            CharsetUtil.UTF_8));

    /**
     * 客户端下线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("激活时间是：" + new Date());
        System.out.println("HeartBeatClientHandler channelActive");
        super.channelActive(ctx);
    }

    /**
     * 客户端上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("停止时间是：" + new Date());
        System.out.println("HeartBeatClientHandler channelInactive");
        super.channelInactive(ctx);
    }

    /**
     * 出现异常直接关闭
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 收到服务端发送过来的消息
     * 内部判断一下消息类型
     * 是心跳消息还是业务数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //打印一下收到的消息
        String message = (String) msg;
        System.out.println(message);
        //如果是心跳包就向服务器发送消息表示收到了
        if (message.equals("Heartbeat")) {
            ctx.write("client has read Heartbeat from server");
            ctx.flush();
        }
        super.channelRead(ctx, msg);
    }

    /**
     * 在出现超时事件时会被触发，包括读空闲超时或者写空闲超时；
     * 这里是客户端 检测的时写超时 即客户端没有向服务端发送消息导致的超时
     * 如果客户端没有发送数据的话 每过4秒就会触发一次这个方法 然后发送一个心跳包给服务器
     * 每次触发超时后发送一个心跳包给服务器
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断是不是超时类型
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            //客户端这里是写超时
            if (state == IdleState.WRITER_IDLE) {
                //每次超时都发送一个心跳包给服务器
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }
}