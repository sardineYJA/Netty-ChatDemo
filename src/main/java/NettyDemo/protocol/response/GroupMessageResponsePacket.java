package NettyDemo.protocol.response;

import NettyDemo.protocol.Packet;
import NettyDemo.session.Session;
import lombok.Data;

import static NettyDemo.protocol.command.Command.GROUP_MESSAGE_RESPONSE;

@Data
public class GroupMessageResponsePacket extends Packet {
    private String fromGroupId;
    private Session fromUser;
    private String message;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_RESPONSE;
    }
}
