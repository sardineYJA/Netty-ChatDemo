package NettyDemo.client;

import NettyDemo.client.console.ConsoleCommandManager;
import NettyDemo.client.console.LoginConsoleCommand;
import NettyDemo.client.handler.*;
import NettyDemo.codec.PacketDecoder;
import NettyDemo.codec.PacketEncoder;
import NettyDemo.codec.Spliter;
import NettyDemo.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class NettyClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;


    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 表示连接的超时时间
                .option(ChannelOption.SO_KEEPALIVE, true)  // 开启 TCP 底层心跳机制
                .option(ChannelOption.TCP_NODELAY, true)  // 无延迟发送数据，需要减少发送次数减少网络交互为false
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new Spliter());           // 拆包
                        ch.pipeline().addLast(new PacketDecoder());     // 反序列化

                        // SimpleChannelInboundHandler 自动匹配包类型
                        ch.pipeline().addLast(new LoginResponseHandler());           // 登录响应处理器
                        ch.pipeline().addLast(new MessageResponseHandler());         // 收消息处理器
                        ch.pipeline().addLast(new CreateGroupResponseHandler());     // 创建群响应处理器
                        ch.pipeline().addLast(new JoinGroupResponseHandler());       // 加群响应处理器
                        ch.pipeline().addLast(new QuitGroupResponseHandler());       // 退群响应处理器
                        ch.pipeline().addLast(new ListGroupMembersResponseHandler());// 获取群成员响应处理器
                        ch.pipeline().addLast(new GroupMessageResponseHandler());    // 收群消息处理器
                        ch.pipeline().addLast(new LogoutResponseHandler());          // 登出响应处理器

                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;  // 1左移order位，每隔 1 、2 、4 、8 秒重连
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    loginConsoleCommand.exec(scanner, channel);
                } else {
                    consoleCommandManager.exec(scanner, channel);
                }
            }
        }).start();
    }
}
