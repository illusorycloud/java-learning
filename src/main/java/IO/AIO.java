package IO;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * AIO
 *
 * @author illusoryCloud
 */
public class AIO {
    @Test
    public void testAioFileChannel() throws IOException, ExecutionException, InterruptedException {
        Path path = Paths.get("D:\\test.txt");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path);
        // 一旦实例化完成，我们就可以着手准备将数据读入到 Buffer 中：
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        readBuffer.clear();
        readBuffer.flip();
        //异步文件通道的读操作和写操作都需要提供一个文件的开始位置，文件开始位置为 0
        Future<Integer> future = fileChannel.read(readBuffer, 0);
        Integer integer = future.get();
        System.out.println(integer);
    }

    @Test
    public void testServerSocketChannel() throws IOException {
        AsynchronousServerSocketChannel socketChannel = AsynchronousServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress("127.0.0.1", 8080));
        Attachment attachment = new Attachment();
        attachment.setServer(socketChannel);
        socketChannel.accept(attachment, new CompletionHandler<AsynchronousSocketChannel, Attachment>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Attachment attachment) {
                SocketAddress remoteAddress = null;
                try {
                    remoteAddress = result.getRemoteAddress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("收到新的连接： " + remoteAddress);

                attachment.getServer().accept(attachment, this);
                Attachment newAtt = new Attachment();
                newAtt.setServer(socketChannel);
                newAtt.setClient(result);
                newAtt.setReadMode(true);
                newAtt.setBuffer(ByteBuffer.allocate(2048));

                result.read(newAtt.getBuffer(), newAtt, new CompletionHandler<Integer, Attachment>() {
                    @Override
                    public void completed(Integer result, Attachment attachment) {
                        if (attachment.isReadMode()) {
                            //读取来自客户端的数据
                            ByteBuffer buffer = attachment.getBuffer();
                            buffer.flip();
                            byte[] bytes = new byte[buffer.limit()];
                            buffer.get(bytes);
                            String re = new String(bytes, StandardCharsets.UTF_8);
                            System.out.println("收到的数据： " + re);
                            //向客户端写数据 写数据到客户端也是异步
                            buffer.clear();
                            buffer.put("Response from Server".getBytes(StandardCharsets.UTF_8));
                            attachment.setReadMode(false);
                            buffer.flip();
                            attachment.getClient().write(buffer, attachment, this);
                        } else {
                            //到这里说明读数据将结束了 有两种选择
                            // 1. 继续等待数据
                            attachment.setReadMode(true);
                            attachment.getBuffer().clear();
                            attachment.getClient().read(attachment.getBuffer(), attachment, this);
//                            //2.断开连接
//                            try {
//                                attachment.getClient().close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                        }
                    }

                    @Override
                    public void failed(Throwable exc, Attachment attachment) {
                        System.out.println("disconnect");

                    }
                });

            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {
                System.out.println("connect failed");
            }
        });
        // 为了防止 main 线程退出
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
        }
    }

    class Attachment {
        private AsynchronousServerSocketChannel server;
        private AsynchronousSocketChannel client;
        private boolean isReadMode;
        private ByteBuffer buffer;
        // getter & setter

        public AsynchronousServerSocketChannel getServer() {
            return server;
        }

        public void setServer(AsynchronousServerSocketChannel server) {
            this.server = server;
        }

        public AsynchronousSocketChannel getClient() {
            return client;
        }

        public void setClient(AsynchronousSocketChannel client) {
            this.client = client;
        }

        public boolean isReadMode() {
            return isReadMode;
        }

        public void setReadMode(boolean readMode) {
            isReadMode = readMode;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

        public void setBuffer(ByteBuffer buffer) {
            this.buffer = buffer;
        }
    }

    @Test
    public void ASocketChannelTest() throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        Future<Void> future = socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        //阻塞在这里 等待连接
        future.get();

        Attachment attachment = new Attachment();
        attachment.setClient(socketChannel);
        attachment.setBuffer(ByteBuffer.allocate(2048));
        attachment.getBuffer().put("hello server".getBytes());
        attachment.getBuffer().flip();

        socketChannel.write(attachment.getBuffer(), attachment, new CompletionHandler<Integer, Attachment>() {
            @Override
            public void completed(Integer result, Attachment attachment) {
                ByteBuffer buffer = attachment.getBuffer();
                if (attachment.isReadMode()) {
                    //读取服务端发送过来的数据
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    String res = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("from server: " + res);
                    //数据读取完后  接下来有两种选择
                    //1.继续向服务端发送新的数据
                    attachment.setReadMode(false);
                    attachment.getBuffer().clear();
                    attachment.getBuffer().put("new message from client".getBytes());
                    attachment.getBuffer().flip();
                    attachment.getClient().write(attachment.getBuffer(), attachment, this);
                    //  2.关闭连接
//                    try {
//                        attachment.getClient().close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                } else {
                    //写操作完成后会进到这里
                    attachment.setReadMode(true);
                    buffer.clear();
                    attachment.getClient().read(attachment.getBuffer(), attachment, this);
                }
            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {
                System.out.println("服务器无响应");
            }
        });


    }
}
