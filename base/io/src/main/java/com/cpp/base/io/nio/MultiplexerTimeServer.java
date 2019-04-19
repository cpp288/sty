package com.cpp.base.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用时间服务端
 *
 * @author chenjian
 * @date 2019-04-18 19:05
 */
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;

    public static void main(String[] args) {
        new Thread(new MultiplexerTimeServer(8080), "NIO-MultiplexerTimeServer-001").start();
    }

    /**
     * 初始化多路复用器、绑定监听端口
     *
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            // 创建多路复用器
            this.selector = Selector.open();
            // 创建通道，并设置成非阻塞模式，绑定监听端口，最后注册到多路复用器（监听 SelectionKey.OP_ACCEPT）
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!this.stop) {
            try {
                this.selector.select(1000);
                // 当有处于就绪状态的 Channel 时，selector 将返回就绪状态的 Channel 的 SelectionKey 集合
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();

                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        // 进行网络的异步读写操作
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理客户端请求
     *
     * @param key
     * @throws IOException
     */
    private void handleInput(SelectionKey key) throws IOException {
        // 通过 SelectionKey 的操作位进行判断即可获知网络事件的类型
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                // 接受客户端的连接请求并创建 SocketChannel 实例，相当于完成了TCP的三次握手
                SocketChannel sc = ssc.accept();
                // 设置为非阻塞
                sc.configureBlocking(false);
                // 注册到 selector，并设置为读操作
                sc.register(this.selector, SelectionKey.OP_READ);
            }

            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                // 创建一个 ByteBuffer，由于无法得知客户端发送的大小，这里开辟一个1K的缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                // SocketChannel 读取缓冲区数据
                int readBytes = sc.read(readBuffer);
                // 由于 SocketChannel 设置为非阻塞的，因此read操作也是非阻塞的，需要通过返回值判断读到的字节数
                // 大于0：读到了字节；等于0：没有读到字节；小于0：链路已关闭，需要释放相关资源；
                if (readBytes > 0) {
                    // 将缓冲区当前的limit设置为position，position设置为0，用于后续会缓冲区的读取操作
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("The time server receive order : " + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                            ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    doWrite(sc, currentTime);
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    /**
     * 应答消息异步发送给客户端
     *
     * @param channel
     * @param response
     * @throws IOException
     */
    private void doWrite(SocketChannel channel, String response) throws IOException {
        if (response == null || response.length() <= 0) {
            return;
        }
        byte[] bytes = response.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        // 将字节数据复制到缓冲区
        writeBuffer.put(bytes);
        writeBuffer.flip();
        // 将缓冲区中的字节数组发送出去
        // 由于 SocketChannel 的write方法是异步非阻塞的，不保证一次能够发送完，会出现"写半包"的问题
        // 需要注册写操作，不断轮询 selector 将没有发送完的 ByteBuffer 发送完毕
        // 可以通过 ByteBuffer 的 hasRemaining 方法判断消息是否发送完成，这里没演示
        channel.write(writeBuffer);
    }
}
