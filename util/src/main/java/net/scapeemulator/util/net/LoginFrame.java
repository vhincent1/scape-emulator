package net.scapeemulator.util.net;

import io.netty.buffer.ByteBuf;

public final class LoginFrame {

	private final int opcode;
	private final ByteBuf payload;

	public LoginFrame(int opcode, ByteBuf payload) {
		this.opcode = opcode;
		this.payload = payload;
	}

	public int getOpcode() {
		return opcode;
	}

	public ByteBuf getPayload() {
		return payload;
	}

}
