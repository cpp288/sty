package com.cpp.netty.protocol.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import java.util.Date;

/**
 * server
 *
 * @author chenjian
 * @date 2019-04-23 11:22
 */
public class WebSocketServer {

    public static void main(String[] args) throws InterruptedException {
        new WebSocketServer().run(8080);
    }

    public void run(int port) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    // 将请求和应答消息编码或者解码为HTTP消息
                                    .addLast("http-codec", new HttpServerCodec())
                                    // 将HTTP消息的多个部分组合成一个完整的HTTP消息
                                    .addLast("aggregator", new HttpObjectAggregator(65535))
                                    // 向客户端发送HTML5文件，主要用于支持浏览器和服务端进行WebSocket通信
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    // 增加服务端handler
                                    .addLast("handler", new WebSocketServerHandler());
                        }
                    });

            Channel channel = b.bind(port).sync().channel();
            System.out.println(String.format("Web socket server started at port : %d", port));
            System.out.println(String.format("Open your browser and navigate to http://localhost:%d/", port));
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

        private WebSocketServerHandshaker handshaker;

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 普通HTTP接入，第一次接入是通过HTTP
            if (msg instanceof FullHttpRequest) {
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            }
            // WebSocket接入
            else if (msg instanceof WebSocketFrame) {
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
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

        private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
            // 判断是否header里是否有Upgrade为websocket
            if (!req.decoderResult().isSuccess()
                    || (!"websocket".contentEquals(req.headers().get("Upgrade")))) {
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
                return;
            }

            // 建立websocket连接
            WebSocketServerHandshakerFactory wsFactory =
                    new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
            handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), req);
            }
        }

        private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse resp) {
            // 返回客户端应答
            if (resp.status().code() != 200) {
                ByteBuf buf = Unpooled.copiedBuffer(resp.status().toString(), CharsetUtil.UTF_8);
                resp.content().writeBytes(buf);
                buf.release();
                HttpHeaderUtil.setContentLength(resp, resp.content().readableBytes());
            }

            ChannelFuture f = ctx.channel().writeAndFlush(resp);
            if (!HttpHeaderUtil.isKeepAlive(req) || resp.status().code() != 200) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }

        private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
            // 是否是关闭链路的命令
            if (frame instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
                return;
            }
            // 是否是Ping消息
            if (frame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
                return;
            }
            // 本demo仅支持文本消息，不支持二进制消息
            if (!(frame instanceof TextWebSocketFrame)) {
                throw new UnsupportedOperationException(
                        String.format("%s frame types not supported", frame.getClass().getName()));
            }

            // 返回应答消息
            String request = ((TextWebSocketFrame) frame).text();
            System.out.println(String.format("%s received %s", ctx.channel(), request));

            ctx.channel().write(new TextWebSocketFrame(
                    String.format("%s , 欢迎使用Netty WebSocket服务，现在时刻：%s", request, new Date().toString())));
        }
    }
}
