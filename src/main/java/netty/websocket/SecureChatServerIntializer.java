package netty.websocket;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

public class SecureChatServerIntializer extends ChatServerInitializer {
    private final SslContext sslContext;

    public SecureChatServerIntializer(ChannelGroup group, SslContext sslContext) {
        super(group);
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        super.initChannel(channel);
        SSLEngine sslEngine = sslContext.newEngine(channel.alloc());
        sslEngine.setUseClientMode(false);
        channel.pipeline().addLast(new SslHandler(sslEngine));
    }
}
