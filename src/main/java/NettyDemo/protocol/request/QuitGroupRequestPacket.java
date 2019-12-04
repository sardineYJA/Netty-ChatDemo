package NettyDemo.protocol.request;

import NettyDemo.protocol.Packet;
import lombok.Data;

import static NettyDemo.protocol.command.Command.QUIT_GROUP_REQUEST;

@Data
public class QuitGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {

        return QUIT_GROUP_REQUEST;
    }
}
