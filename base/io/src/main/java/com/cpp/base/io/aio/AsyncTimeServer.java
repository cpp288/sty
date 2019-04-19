package com.cpp.base.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * AIO时间服务端
 *
 * @author chenjian
 * @date 2019-04-19 14:04
 */
public class AsyncTimeServer implements Runnable {

    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public static void main(String[] args) {
        new Thread(new AsyncTimeServer(8080), "AIO-AsyncTimeServer-001").start();
    }

    public AsyncTimeServer(int port) {
        try {
            // 创建 AsynchronousServerSocketChannel，并绑定端口
            this.asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            this.asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 初始化 CountDownLatch 对象，作用是在完成一组正在执行的操作之前，允许当前线程一直阻塞
        this.latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收客户端的请求
     */
    private void doAccept() {
        // 由于是异步操作，可以通过 CompletionHandler 实例来接收 accept 操作成功的通知消息
        this.asynchronousServerSocketChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel, AsyncTimeServer>() {

            @Override
            public void completed(AsynchronousSocketChannel channel, AsyncTimeServer asyncTimeServer) {
                // 为什么还要再次调用 accept 方法呢？
                // 当我们调用 AsynchronousServerSocketChannel 的 accept 方法后，如果有新的客户端接入，系统将回调我们传入的 CompletionHandler 实例的 completed 方法
                // 表示新的客户端已经接入成功，因为一个 AsynchronousServerSocketChannel 可以接收成千上万个客户端，所以需要继续调用它的 accept 方法，
                // 接收其它客户端连接，最终形成一个循环，每当接收一个客户端连接成功后，再异步接收新的客户端连接
                asyncTimeServer.asynchronousServerSocketChannel.accept(asyncTimeServer, this);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 进行异步读操作，参数详解：
                // ByteBuffer dst：接收缓冲区，用于从异步 Channel 中读取数据包
                // A attachment：异步 Channel 携带的附件，通知回调的时候作为入参使用
                // CompletionHandler<Integer,? super A> handler：接收通知回调的业务handler
                channel.read(buffer, buffer, new ReadCompletionHandler(channel));
            }

            @Override
            public void failed(Throwable exc, AsyncTimeServer attachment) {
                exc.printStackTrace();
                attachment.latch.countDown();
            }
        });
    }

    private class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private AsynchronousSocketChannel channel;

        public ReadCompletionHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);

            String req = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("The time server receive order : " + req);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req)
                    ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
            doWrite(currentTime);
        }

        private void doWrite(String response) {
            if (response == null || response.length() <= 0) {
                return;
            }
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            this.channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    // 如果没有发送完成，继续发送
                    if (buffer.hasRemaining()) {
                        channel.write(buffer, buffer, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer buffer) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            try {
                this.channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
