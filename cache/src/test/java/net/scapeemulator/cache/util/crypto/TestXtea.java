//package net.scapeemulator.cache.util.crypto;
//
//import org.junit.Test;
//
//import java.nio.ByteBuffer;
//
//import static org.junit.Assert.assertArrayEquals;
//
//public final class TestXtea {
//
//	private static final String[][] TEST_VECTORS = {
//		{ "000102030405060708090a0b0c0d0e0f", "4142434445464748", "497df3d072612cb5" },
//		{ "000102030405060708090a0b0c0d0e0f", "4141414141414141", "e78f2d13744341d8" },
//		{ "000102030405060708090a0b0c0d0e0f", "5a5b6e278948d77f", "4141414141414141" },
//		{ "00000000000000000000000000000000", "4142434445464748", "a0390589f8b8efa5" },
//		{ "00000000000000000000000000000000", "4141414141414141", "ed23375a821a8c2d" },
//		{ "00000000000000000000000000000000", "70e1225d6e4e7655", "4141414141414141" }
//	};
//
//	@Test
//	public void testEncipher() {
//		for (String[] vector : TEST_VECTORS) {
//			int[] key = new int[4];
//			for (int i = 0; i < key.length; i++) {
//				String hex = vector[0].substring(i * 8, (i + 1) * 8);
//				key[i] = Integer.parseInt(hex, 16);
//			}
//
//			byte[] plaintext = new byte[8];
//			for (int i = 0; i < plaintext.length; i++) {
//				String hex = vector[1].substring(i * 2, (i + 1) * 2);
//				plaintext[i] = (byte) Integer.parseInt(hex, 16);
//			}
//
//			byte[] ciphertext = new byte[8];
//			for (int i = 0; i < plaintext.length; i++) {
//				String hex = vector[2].substring(i * 2, (i + 1) * 2);
//				ciphertext[i] = (byte) Integer.parseInt(hex, 16);
//			}
//
//			ByteBuffer buf = ByteBuffer.wrap(plaintext);
//			Xtea.encipher(buf, 0, 8, key);
//			assertArrayEquals(ciphertext, buf.array());
//		}
//	}
//
//	@Test
//	public void testDecipher() {
//		for (String[] vector : TEST_VECTORS) {
//			int[] key = new int[4];
//			for (int i = 0; i < key.length; i++) {
//				String hex = vector[0].substring(i * 8, (i + 1) * 8);
//				key[i] = Integer.parseInt(hex, 16);
//			}
//
//			byte[] plaintext = new byte[8];
//			for (int i = 0; i < plaintext.length; i++) {
//				String hex = vector[1].substring(i * 2, (i + 1) * 2);
//				plaintext[i] = (byte) Integer.parseInt(hex, 16);
//			}
//
//			byte[] ciphertext = new byte[8];
//			for (int i = 0; i < plaintext.length; i++) {
//				String hex = vector[2].substring(i * 2, (i + 1) * 2);
//				ciphertext[i] = (byte) Integer.parseInt(hex, 16);
//			}
//
//			ByteBuffer buf = ByteBuffer.wrap(ciphertext);
//			Xtea.decipher(buf, 0, 8, key);
//			assertArrayEquals(plaintext, buf.array());
//		}
//	}
//
//}
