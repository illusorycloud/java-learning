package IO;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 阻塞式IO Demo
 *
 * @author illusoryCloud
 */
public class BadIO {
    /**
     *  服务端
     *
     * @throws IOException
     */
    @Test
    public void server() throws IOException {
        //打开channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress( 8080));

        while (true) {
            //会一直阻塞 知道有请求到来
            SocketChannel socketChannel = serverSocketChannel.accept();
            //每次请求来都开一个新的线程
            SocketHandler socketHandler = new SocketHandler(socketChannel);
            new Thread(socketHandler).start();
        }
    }


    class SocketHandler implements Runnable {
        private SocketChannel socketChannel;

        public SocketHandler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            readBuffer.clear();
            try {

                int num;
                while ((num = socketChannel.read(readBuffer)) > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[num];
                    // 取出buffer中的内容
                    readBuffer.get(bytes);
                    String result = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("收到请求： " + result);

                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                    writeBuffer.clear();
                    writeBuffer.put(("from client： 已经收到请求： " + result).getBytes());
                    writeBuffer.flip();
                    socketChannel.write(writeBuffer);
                    //每次读完清空readBuffer
                    readBuffer.clear();

                }
            } catch (IOException exceptione) {
                exceptione.printStackTrace();
            }

        }
    }

    /**
     *  客户端
     *
     * @throws IOException
     */
    @Test
    public void client() throws IOException {
        //打开channel
        SocketChannel socketChannel = SocketChannel.open();
        //连接到服务端
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
