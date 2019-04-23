package com.cpp.netty.serialize.netty.java;

import com.cpp.netty.serialize.netty.SubscribeReq;
import com.cpp.netty.serialize.netty.SubscribeResp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 服务端
 *
 * @author chenjian
 * @date 2019-04-23 09:48
 */
public class SubReqServer {

    public static void main(String[] args) throws InterruptedException {
        new SubReqServer().bind(8080);
    }

    public void bind(int port) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    // 创建一个 ObjectDecoder，负责对实现 Serializable 的POJO对象进行解码
                                    // 这是使用 weakCachingConcurrentResolver 创建线程安全的 WeakReferenceMap 对类加载器进行缓存
                                    // 它支持多线程并发访问，当虚拟机内存不足时，会释放缓存中的内存，防止内存泄露
                                    .addLast(new ObjectDecoder(1024 * 1024,
                                            ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                                    // 创建 ObjectEncoder，可以在消息发送的时候自动将实现 Serializable 的POJO对象进行编码
                                    .addLast(new ObjectEncoder())
                            .addLast(new SubReqServerHandler());
                        }
                    });

            ChannelFuture c = b.bind(port).sync();
            c.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class SubReqServerHandler extends ChannelHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 已经通过 ObjectDecoder 解码，可以直接强转为对象
            SubscribeReq req = (SubscribeReq) msg;
            if ("Hello".equalsIgnoreCase(req.getUserName())) {
                System.out.println(String.format("Service accept client subscribe req : [%s]", req.toString()));
                ctx.writeAndFlush(resp(req.getSubReqId()));
            }
        }

        private SubscribeResp resp(int subReqId) {
            SubscribeResp resp = new SubscribeResp();
            resp.setSubReqId(subReqId);
            resp.setRespCode(0);
            resp.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
            return resp;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
