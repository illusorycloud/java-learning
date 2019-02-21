package io;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO
 * @author illusoryCloud
 */
public class NIO {
    @Test
    public void server() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress( 8080));
        //将channel设置为非阻塞的 只有非阻塞的channel才能注册到Selector上
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int select = selector.select();
            if (select <= 0) {
                continue;
            }
            //遍历
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey keys = iterator.next();
                iterator.remove();
                if (keys.isAcceptable()) {
                    //已经有新的连接到服务端了
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //有新连接不代表有数据了
                    //先注册到selector上，监听OP_READ
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (keys.isReadable()) {
                    //有数据可读了
                    SocketChannel channel = (SocketChannel) keys.channel();  //需要强转为SocketChannel
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int read = channel.read(readBuffer);
                    if (read > 0) {
                        //读到数据了

                        //读取到bytes数组并打印出来
                        byte[] bytes = new byte[read];
                        readBuffer.flip();
                        readBuffer.get(bytes);
                        String result = new String(bytes, StandardCharsets.UTF_8).trim();
                        System.out.println("from client: " + result);
                        //写回客户端
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put(bytes);
                        writeBuffer.flip();
                        channel.write(writeBuffer);
                    } else if (read == -1) {
                        //-1代表连接已经关闭了
                        channel.close();
                    }

                }

            }
        }
    }

    @Test
    public void client() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        //发送请求
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        writeBuffer.put("hello server".getBytes());
        writeBuffer.flip();
        socketChannel.write(writeBuffer);

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int num;
        if ((num = socketChannel.read(readBuffer)) > 0) {
            // 切换到读模式
            readBuffer.flip();
            byte[] bytes = new byte[num];
            readBuffer.get(bytes);

            String result = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("from server： " + result);
        }
        socketChannel.close();

    }
}
