package com.cpp.netty.protocol.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;


/**
 * server
 * telnet localhost 8080
 * 输入文件路径
 * -----------------------------------------------------
 * Netty有多种方式可以实现文件的传输，本案例只是通用的实现方式
 * 在进行大文件传输时，如果都映射到内存中，可能导致内存溢出，Netty提供过了 ChunkedWriteHandler 来解决
 *
 * @author chenjian
 * @date 2019-04-23 20:29
 */
public class FileServer {

    public static void main(String[] args) throws InterruptedException {
        new FileServer().run(8080);
    }

    public void run(int port) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new StringEncoder(CharsetUtil.UTF_8),
                                    // 按照回车换行符对数据进行解码
                                    new LineBasedFrameDecoder(1024),
                                    new StringDecoder(CharsetUtil.UTF_8),
                                    new FileServerHandler()
                            );
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Start file server at port : " + port);
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class FileServerHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
            File file = new File(msg);

            if (file.exists()) {
                if (!file.isFile()) {
                    ctx.writeAndFlush("Not a file : " + file + HttpConstants.CR);
                    return;
                }

                ctx.write(file + " " + file.length());
                // 使用 RandomAccessFile 已只读的方式打开文件
                RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "r");
                // 通过 Netty 提供的 DefaultFileRegion 进行文件传输，参数：
                // FileChannel：文件通道，用于文件读写操作
                // position：文件操作的指针位置，读取或写入的起始点
                // count：操作的总字节数
                DefaultFileRegion region =
                        new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length());
                ctx.write(region);
                ctx.writeAndFlush(HttpConstants.CR);
                randomAccessFile.close();
            } else {
                ctx.writeAndFlush("File not found : " + file + HttpConstants.CR);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
