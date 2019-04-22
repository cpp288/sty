package com.cpp.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 时间服务端
 *
 * @author chenjian
 * @date 2019-04-19 15:38
 */
public class TimeServer {

    public static void main(String[] args) throws InterruptedException {
        new TimeServer().bind(8080);
    }

    public void bind(int port) throws InterruptedException {
        // 用于服务端接收客户端的连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用于进行 SocketChannel 的网络读写
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 设置创建的 Channel 为 NioServerSocketChannel，它的功能对应 JDK NIO 类库中的 ServerSocketChannel 类
                    .channel(NioServerSocketChannel.class)
                    // 配置TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 绑定IO事件处理类，主要用于处理网络IO事件（例如记录之日、对消息进行编解码等）
                    .childHandler(new ChildChannelHandler());
            // 绑定端口，同步等待成功，返回 ChannelFuture，类似于JDK的 jdk.util.concurrent.Future
            ChannelFuture f = b.bind(port).sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }

    private class TimeServerHandler extends ChannelHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 转换为 Netty 的 ByteBuf 对象，类似于JDK中的 ByteBuffer，不过它提供了更加强大灵活的功能
            ByteBuf buf = (ByteBuf) msg;
            // 通过 buf.readableBytes() 可以获取缓冲区可读的字节数
            byte[] req = new byte[buf.readableBytes()];
            // 将缓冲区中的字节数组复制到新建的byte数组中
            buf.readBytes(req);

            String body = new String(req, StandardCharsets.UTF_8);
            System.out.println("The time server receive order : " + body);

            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                    ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
            ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
            ctx.write(resp);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            // 将消息发送队列中的消息写入到 SocketChannel 中发送给对方
            // 从性能角度考虑，为了防止频繁唤醒selector进行消息发送，Netty的write方法并不直接将消息写入 SocketChannel 中
            // 调用write方法只是把待发送的消息放到发送缓冲数组中，再通过flush方法将缓冲区中的消息全部写到 SocketChannel 中
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            // 发生异常释放资源
            ctx.close();
        }
    }
}
