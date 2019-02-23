package netty.websocket;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int len;

    public FixedLengthFrameDecoder(int len) {
        this.len = len;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= len) {
            ByteBuf byteBuf1 = byteBuf.readBytes(len);
            list.add(byteBuf);
        }
    }
}
