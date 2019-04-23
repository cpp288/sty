package com.cpp.netty.serialize.netty.java;

import com.cpp.netty.serialize.netty.SubscribeReq;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 客户端
 *
 * @author chenjian
 * @date 2019-04-23 10:04
 */
public class SubReqClient {

    public static void main(String[] args) throws InterruptedException {
        new SubReqClient().connect("127.0.0.1", 8080);
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
                            socketChannel.pipeline()
                                    // 禁止对类加载器进行缓存
                                    .addLast(new ObjectDecoder(1024,
                                            ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new SubReqClientHandler());
                        }
                    });

            ChannelFuture c = b.connect(host, port).sync();
            c.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private class SubReqClientHandler extends ChannelHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            for (int i = 0; i < 10; i++) {
                ctx.write(subRep(i));
            }
            ctx.flush();
        }

        private SubscribeReq subRep(int i) {
            SubscribeReq req = new SubscribeReq();
            req.setAddress("浙江省杭州市");
            req.setPhoneNumber("1538****322");
            req.setProductName("book");
            req.setSubReqId(i);
            req.setUserName("Hello");
            return req;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(String.format("Receive server response : [%s]", msg));
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
