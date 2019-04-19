package com.cpp.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * 时间客户端
 *
 * @author chenjian
 * @date 2019-04-19 16:22
 */
public class TimeClient {

    public static void main(String[] args) throws InterruptedException {
        new TimeClient().connect("127.0.0.1", 8080);
    }

    public void connect(String host, int port) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private class TimeClientHandler extends ChannelHandlerAdapter {

        private final ByteBuf firstMessage;

        public TimeClientHandler() {
            byte[] req = "QUERY TIME ORDER".getBytes();
            this.firstMessage = Unpooled.buffer(req.length);
            this.firstMessage.writeBytes(req);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // 当客户端和服务端TCP链路建立成功之后，Netty的NIO线程会调用 channelActive 方法，发送数据
            ctx.writeAndFlush(firstMessage);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] resp = new byte[buf.readableBytes()];
            buf.readBytes(resp);

            String body = new String(resp, StandardCharsets.UTF_8);
            System.out.println("Now is : " + body);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.err.println("Unexpected exception from downstream : " + cause.getMessage());
            ctx.close();
        }
    }
}
