package NettyHelloDemo;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.security.acl.Group;

public class NettyServerDemo {
    private int port;

    public NettyServerDemo(int port) {
        this.port = port;
    }

    public void start()throws Exception {

        //通过nio方式来接受和处理连接
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();


        try {

            //引导辅助程序
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //设置nio类型的channel

            serverBootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class)

                    //绑定端口
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel channel) throws Exception {

                            //有连接时创建一个channnel，并创建管道
                            channel.pipeline().addLast(new ServerHandler());
                        }
                    });

            // 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture=serverBootstrap.bind().sync();

            // 应用程序会一直等待，直到channel关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();

        }


    }

    public static void main(String[] args) throws Exception{
        NettyServerDemo nettyServerDemo=new NettyServerDemo(11113);
        nettyServerDemo.start();

    }
}

