# Netty-ChatDemo

使用 Netty 实现通信的案例 

## 指令

```sh
# 单聊
sendToUser: toUserId message

# 退出登录
logout

# 创建群聊
createGroup

# 查看群聊人员 
listGroupMembers: groupId

# 加入群聊
joinGroup: groupId

# 退出群聊
quitGroup: groupId

# 发送群消息
sendToGroup: groupId message
```

## 增加新功能

1. Command 增加新协议指令
2. 增加相关的 RequestPacket 和 ResponsePacket
3. PacketCodec 的 Map 增加相关 Packet 序列化
4. 创建 RequestPacketHandler 和 ResponsePacketHandler 类
5. Server 增加 RequestPacketHandler 和 Client 增加 ResponsePacketHandler

## reference

https://www.jianshu.com/p/a4e03835921a
