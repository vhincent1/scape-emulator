package net.scapeemulator.util.crypto;

public final class ZeroStreamCipher implements StreamCipher {

	@Override
	public int nextInt() {
		return 0;
	}

}
