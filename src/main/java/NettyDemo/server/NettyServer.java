package NettyDemo.server;

import NettyDemo.codec.PacketCodecHandler;
import NettyDemo.codec.Spliter;
import NettyDemo.server.handler.AuthHandler;
import NettyDemo.server.handler.IMHandler;
import NettyDemo.server.handler.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

public class NettyServer {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();   // 表示监听端口
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(); // 表示处理每一条连接的数据读写的线程组

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)   // 指定 IO 模型，BIO为OioServerSocketChannel.class
                .option(ChannelOption.SO_BACKLOG, 1024) // 用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .childOption(ChannelOption.SO_KEEPALIVE, true)  // 开启TCP底层心跳机制
                .childOption(ChannelOption.TCP_NODELAY, true)  // 无延迟发送，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Spliter());

                        // ch.pipeline().addLast(new PacketDecoder());
                        // ch.pipeline().addLast(new PacketEncoder());  // 原本在最后
                        // 合并上面两个，使用单例模式，防止每次有新连接产生过多小对象
                        ch.pipeline().addLast(PacketCodecHandler.INSTANCE);

                        // SimpleChannelInboundHandler 自动匹配包类型，以下可换成单例模式
//                        ch.pipeline().addLast(new LoginRequestHandler()); // 登录请求处理器
//                        ch.pipeline().addLast(new AuthHandler());
//                        ch.pipeline().addLast(new MessageRequestHandler());          // 单聊消息请求处理器
//                        ch.pipeline().addLast(new CreateGroupRequestHandler());      // 创建群请求处理器
//                        ch.pipeline().addLast(new JoinGroupRequestHandler());        // 加群请求处理器
//                        ch.pipeline().addLast(new QuitGroupRequestHandler());        // 退群请求处理器
//                        ch.pipeline().addLast(new ListGroupMembersRequestHandler()); // 获取群成员请求处理器
//                        ch.pipeline().addLast(new GroupMessageRequestHandler());     // 群聊消息请求处理器
//                        ch.pipeline().addLast(new LogoutRequestHandler());           // 登出请求处理器

                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        ch.pipeline().addLast(AuthHandler.INSTANCE);
                        ch.pipeline().addLast(IMHandler.INSTANCE);

                    }
                });

        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
