package io.noctin.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class HttpServer implements Runnable {

    public static final SslContext DEFAULT_SSL_CONTEXT;
    static {
        SslContext ctx;
        try {
            SslProvider provider = OpenSsl.isAlpnSupported() ? SslProvider.OPENSSL : SslProvider.JDK;
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            ctx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } catch (CertificateException | SSLException e) {
            ctx = null;
        }
        DEFAULT_SSL_CONTEXT = ctx;
    }

    private int port = 80;
    private SslContext sslContext = DEFAULT_SSL_CONTEXT;
    private long backlog = 1024;

    public HttpServer() {
    }

    public HttpServer(int port, SslContext sslContext, long backlog) {
        this.port = port;
        this.sslContext = sslContext;
        this.backlog = backlog;
    }

    public HttpServer(int port, long backlog) {
        this.port = port;
        this.backlog = backlog;
    }

    public HttpServer(int port, SslContext sslContext) {
        this.port = port;
        this.sslContext = sslContext;
    }

    public HttpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer(this.sslContext, server));

            Channel channel = bootstrap.bind(this.port).sync().channel();

            System.out.println(String.format("Server started on port %s", this.port));

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        HttpServer server = new HttpServer(10177, null);
        server.run();
    }
}
