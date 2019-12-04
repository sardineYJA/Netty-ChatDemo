package NettyDemo.codec;


import NettyDemo.protocol.Packet;
import NettyDemo.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

// 加上注解标识，表明该 handler 是可以多个 channel 共享的
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    // 单例模式
    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();
    private PacketCodecHandler() {}

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> list) throws Exception {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodec.INSTANCE.encode(byteBuf, packet);
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        list.add(PacketCodec.INSTANCE.decode(byteBuf));  // 反序列化成对象
    }
}
