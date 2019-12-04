package NettyDemo.protocol.response;

import NettyDemo.protocol.Packet;
import NettyDemo.session.Session;
import lombok.Data;

import java.util.List;

import static NettyDemo.protocol.command.Command.LIST_GROUP_MEMBERS_RESPONSE;

@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {

        return LIST_GROUP_MEMBERS_RESPONSE;
    }
}
