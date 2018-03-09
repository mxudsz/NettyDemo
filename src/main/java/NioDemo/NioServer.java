package NioDemo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    private Selector selector;

    public void initServer(int port) throws Exception {

        //打开serversocketchannel，监听客户端
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //设置通道为非阻塞
        ssc.configureBlocking(false);

        ssc.socket().bind(new InetSocketAddress(port));

        //获得管道管理器
        this.selector = Selector.open();

        ssc.register(selector, SelectionKey.OP_ACCEPT);

    }

    public void listen() throws Exception {

        System.out.println("服务器启动成功");
        while (true) {

            selector.select();

            Set<SelectionKey>set= this.selector.selectedKeys();
            Iterator<SelectionKey>it=set.iterator();
            while (it.hasNext()) {

                SelectionKey sk = it.next();
               // this.selector.selectedKeys().iterator().remove();
                it.remove();
                hander(sk);


            }

        }
    }

    public void hander(SelectionKey sk)throws Exception {

        if (sk.isAcceptable()) {
            handerAccept(sk);
        } else if (sk.isReadable()) {
            handerRead(sk);
        }
    }

    public void handerAccept(SelectionKey sk )throws Exception{

        ServerSocketChannel server=(ServerSocketChannel) sk.channel();
        SocketChannel socketChannel=server.accept();

        socketChannel.configureBlocking(false);

        System.out.println("新的客户端已连接");

        socketChannel.register(this.selector,SelectionKey.OP_READ);



    }

    public void handerRead(SelectionKey sk )throws Exception{
        SocketChannel sc=(SocketChannel) sk.channel();

        ByteBuffer bb=ByteBuffer.allocate(1024);

        sc.read(bb);
        String string=new String(bb.array()).trim();

        System.out.println(string);

        ByteBuffer byteBuffer=ByteBuffer.wrap("hello".getBytes());
        sc.write(byteBuffer);



    }

    public static void main(String[] args) throws Exception{
        NioServer nioServer=new NioServer();
        nioServer.initServer(11111);
        nioServer.listen();
    }
}

class Hander {
}