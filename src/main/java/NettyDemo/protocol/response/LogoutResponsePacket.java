package NettyDemo.protocol.response;

import NettyDemo.protocol.Packet;
import lombok.Data;

import static NettyDemo.protocol.command.Command.LOGOUT_RESPONSE;

@Data
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {

        return LOGOUT_RESPONSE;
    }
}
