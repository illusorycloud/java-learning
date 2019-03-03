package netty.heartbeatproduct;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 生产级 心跳机制
 * 空闲状态触发器
 *
 * @author illusoryCloud
 */
@ChannelHandler.Sharable
public class AcceptorIdleStateTrigger extends ChannelHandlerAdapter {
    /**
     * 读超时后直接抛出异常 不关闭channel
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                throw new Exception("idle exception");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

//    /**
//     * 不释放资源，读取后
//     */
//    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
//            CharsetUtil.UTF_8));
//    /**
//     * 当前超时次数
//     */
//    private int lossConnectTime = 0;
//    /**
//     * 超时次数限制 3次
//     * 超过3次则判断客户端为不活跃状态
//     */
//    private static final int TIME_OUT = 3;
//    /**
//     * 在出现超时事件时会被触发，包括读空闲超时或者写空闲超时；
//     * 这里是服务端判断的是读超时
//     *
//     * @param ctx
//     * @param evt
//     * @throws Exception
//     */
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        //先判断evt是不是IdleStateEvent中几种类型
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            IdleState state = event.state();
//            //这里是服务端判断的是读超时 客户端没发消息过来导致读超时
//            if (state == IdleState.READER_IDLE) {
//                lossConnectTime++;
//                System.out.println("3秒没收到客户端的消息了 " + ctx.channel().remoteAddress() + "超时次数 : " + lossConnectTime);
//                //连续超时次数满足TIME_OUT则判断为不活跃
//                if (lossConnectTime >= TIME_OUT) {
//                    System.out.println("关闭这个不活跃的channel " + ctx.channel().remoteAddress());
//                    ctx.channel().close();
//                }
//            } else {
//                super.userEventTriggered(ctx, evt);
//            }
//        }
//    }
}
