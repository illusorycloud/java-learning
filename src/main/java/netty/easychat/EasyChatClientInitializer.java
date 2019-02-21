package netty.easychat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 用来增加多个的处理类到 ChannelPipeline 上，包括编码、解码、EasyChatServerHandler 等
 *
 * @author illusoryCloud
 */
public class EasyChatClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter())
                , new StringEncoder()
                , new StringDecoder()
                , new EasyChatServerHandler());
    }
}
