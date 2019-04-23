package com.cpp.netty.protocol.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * server
 *
 * @author chenjian
 * @date 2019-04-23 19:45
 */
public class ChineseProverbServer {

    public static void main(String[] args) throws InterruptedException {
        new ChineseProverbServer().run(8080);
    }

    public void run(int port) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    // UDP通信，使用 NioDatagramChannel 来创建
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    // UDP不存在客户端和服务端的实际连接，因此不需要为连接（ChannelPipeline）设置 handler
                    .handler(new ChineseProverbServerHandler());

            b.bind(port).sync().channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        private static final String[] DICTIONARY = {
                "只要功夫深，铁杵磨成针",
                "旧时王谢堂前燕，飞入寻常百姓家",
                "洛阳亲友如想问，一片冰心在玉壶",
                "一寸光阴一寸金，寸金难买寸光阴"
        };

        private String nextQuote() {
            int index = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
            return DICTIONARY[index];
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
            String req = packet.content().toString(CharsetUtil.UTF_8);
            System.out.println(req);

            if ("谚语字典查询?".equals(req)) {
                ctx.writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer("谚语查询结果：" + nextQuote(), CharsetUtil.UTF_8), packet.sender()));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
