package com.cpp.base.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * AIO时间客户端
 *
 * @author chenjian
 * @date 2019-04-19 14:45
 */
public class AsyncTimeClient implements Runnable, CompletionHandler<Void, AsyncTimeClient> {

    private String host;
    private int port;
    private CountDownLatch latch;

    private AsynchronousSocketChannel client;

    public static void main(String[] args) {
        new Thread(new AsyncTimeClient("127.0.0.1", 8080), "AIO-AsyncTimeClient-001").start();
    }

    public AsyncTimeClient(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;

        try {
            this.client = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.latch = new CountDownLatch(1);
        // 通过 connect 发起异步操作
        // A attachment：AsynchronousSocketChannel 的附件，用户回调通知时作为入参被传递
        // CompletionHandler<Void,? super A> handler：异步操作回调通知接口
        this.client.connect(new InetSocketAddress(host, port), this, this);

        try {
            latch.await();
            this.client.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncTimeClient attachment) {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        this.client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                // 还有未发送的数据，则继续发送
                if (buffer.hasRemaining()) {
                    client.write(buffer, buffer, this);
                }
                // 发送完成，则异步读取响应数据
                else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            buffer.flip();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);
                            String body = new String(bytes, StandardCharsets.UTF_8);
                            System.out.println("Now is : " + body);
                            latch.countDown();
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                latch.countDown();
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }  finally {
                    latch.countDown();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClient attachment) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            latch.countDown();
        }
    }
}
