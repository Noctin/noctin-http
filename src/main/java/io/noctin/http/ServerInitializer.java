package io.noctin.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.ssl.SslContext;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;
    private final HttpServer server;

    public ServerInitializer(SslContext sslContext, HttpServer server) {
        this.sslContext = sslContext;
        this.server = server;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (sslContext != null){
            pipeline.addLast(sslContext.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(new BaseHandler(server));
    }
}
