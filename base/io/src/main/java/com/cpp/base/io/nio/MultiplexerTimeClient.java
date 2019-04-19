package com.cpp.base.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用时间客户端
 *
 * @author chenjian
 * @date 2019-04-18 19:28
 */
public class MultiplexerTimeClient implements Runnable {

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public static void main(String[] args) {
        new Thread(new MultiplexerTimeClient("127.0.0.1", 8080), "NIO-MultiplexerTimeClient-001").start();
    }

    public MultiplexerTimeClient(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;

        try {
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open();
            this.socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!this.stop) {
            try {
                this.selector.select(1000);
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();

                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
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
                System.exit(1);
            }
        }

        if (this.selector != null) {
            try {
                // 释放 selector，由于在其注册的 channel 可能是成千上万的，一一释放显然不合适
                // 因此，JDK底层会自动释放所有跟此 selector 相关联的资源
                this.selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            // 如果处于连接状态，说明服务端已经返回ACK应答消息
            if (key.isConnectable()) {
                // 说明连接成功，注册成 SelectionKey.OP_READ，通过 doWrite 发送
                if (sc.finishConnect()) {
                    sc.register(this.selector, SelectionKey.OP_READ);
                    doWrite(sc);
                } else {
                    System.exit(1);
                }
            }
            // 判断是否收到了服务端的应答消息，如果是，则 SocketChannel 是可读的
            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("Now is : " + body);
                    this.stop = true;
                } else if (readBytes < 0) {
                    key.channel();
                    sc.close();
                }
            }
        }
    }

    /**
     * 连接
     *
     * @throws IOException
     */
    private void doConnect() throws IOException {
        // 如果连接成功，将 Channel 注册到 selector 上，进行请求写操作
        if (this.socketChannel.connect(new InetSocketAddress(this.host, this.port))) {
            this.socketChannel.register(this.selector, SelectionKey.OP_READ);
            doWrite(this.socketChannel);
        }
        // 如果没有连接成功，不代表连接失败，注册成 SelectionKey.OP_CONNECT，当服务端返回TCP syn-ack消息后，
        // selector就能轮询到这个 SocketChannel 处于连接就绪状态
        else {
            this.socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    /**
     * 发送数据
     *
     * @param channel
     * @throws IOException
     */
    private void doWrite(SocketChannel channel) throws IOException {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        channel.write(writeBuffer);
        // 通过 writeBuffer.hasRemaining() 进行判断是否消息全部发送完毕
        if (!writeBuffer.hasRemaining()) {
            System.out.println("Send order 2 server succeed.");
        }
    }
}
