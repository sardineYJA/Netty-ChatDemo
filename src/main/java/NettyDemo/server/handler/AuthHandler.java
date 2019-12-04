package NettyDemo.server.handler;

import NettyDemo.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class AuthHandler extends ChannelInboundHandlerAdapter {
    // 单例模式
    public static final AuthHandler INSTANCE = new AuthHandler();
    private AuthHandler() {}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!SessionUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();           // 未登录则关闭channel
        } else {
            ctx.pipeline().remove(this);     // 已登录则删除验证的handler
            super.channelRead(ctx, msg);     // 传递到下一个handler
        }
    }
}
