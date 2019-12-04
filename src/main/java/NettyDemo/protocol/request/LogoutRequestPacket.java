package NettyDemo.protocol.request;

import NettyDemo.protocol.Packet;
import lombok.Data;

import static NettyDemo.protocol.command.Command.LOGOUT_REQUEST;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {

        return LOGOUT_REQUEST;
    }
}
