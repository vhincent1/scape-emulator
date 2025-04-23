package net.scapeemulator.asm.bundler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Resource {

	public static final Resource NULL = new Resource(new int[20], 0, 0);

	public static Resource create(File file) throws NoSuchAlgorithmException, IOException {
		int[] checksum = sha1(file);
		int len = (int) file.length();
		return new Resource(checksum, len, len);
	}

	private static int[] sha1(File file) throws NoSuchAlgorithmException, IOException {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		try (InputStream is = new FileInputStream(file)) {
			byte[] buf = new byte[4096];
			for (;;) {
				int len = is.read(buf, 0, buf.length);
				if (len == -1) {
					byte[] bytes = digest.digest();

					int[] ints = new int[bytes.length];
					for (int i = 0; i < bytes.length; i++)
						ints[i] = bytes[i];
					return ints;
				}

				digest.update(buf, 0, len);
			}
		}
	}

	private final int[] checksum;
	private final int uncompressedSize, compressedSize;

	public Resource(int[] checksum, int uncompressedSize, int compressedSize) {
		if (checksum.length != 20)
			throw new IllegalArgumentException();

		this.checksum = checksum;
		this.uncompressedSize = uncompressedSize;
		this.compressedSize = compressedSize;
	}

	public int[] getChecksum() {
		return checksum;
	}

	public int getUncompressedSize() {
		return uncompressedSize;
	}

	public int getCompressedSize() {
		return compressedSize;
	}

}
