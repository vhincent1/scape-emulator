package net.scapeemulator.util.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class LoginFrameEncoder extends MessageToByteEncoder<LoginFrame> {

	@Override
	public void encode(ChannelHandlerContext ctx, LoginFrame frame, ByteBuf buf) throws Exception {
		ByteBuf payload = frame.getPayload();
		buf.writeByte(frame.getOpcode());
		buf.writeShort(payload.readableBytes());
		buf.writeBytes(payload);
	}

}
