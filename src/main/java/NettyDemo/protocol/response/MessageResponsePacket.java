package NettyDemo.protocol.response;

import NettyDemo.protocol.Packet;
import lombok.Data;

import static NettyDemo.protocol.command.Command.MESSAGE_RESPONSE;


@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}
