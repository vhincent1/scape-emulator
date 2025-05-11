package net.scapeemulator.util.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.MessageBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public final class LoginFrameDecoder extends ByteToMessageDecoder {

	private enum State {
		READ_OPCODE, READ_LENGTH, READ_PAYLOAD
	}

	private State state = State.READ_OPCODE;
	private int opcode, len;
	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buffer, MessageBuf<Object> out) {
		if (state == State.READ_OPCODE) {
			if (!buffer.isReadable())
				return;

			state = State.READ_LENGTH;
			opcode = buffer.readUnsignedByte();
		}

		if (state == State.READ_LENGTH) {
			if (buffer.readableBytes() < 2)
				return;

			state = State.READ_PAYLOAD;
			len = buffer.readUnsignedShort();
		}

		if (state == State.READ_PAYLOAD) {
			if (buffer.readableBytes() < len)
				return;

			state = State.READ_OPCODE;
			ByteBuf payload = buffer.readBytes(len);
			out.add(new LoginFrame(opcode, payload));
		}
	}

}
