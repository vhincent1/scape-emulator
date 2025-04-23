package net.scapeemulator.util.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class RsaKeyGenerator {

	public static void main(String[] args) {
		final int bits = 512;
		final SecureRandom random = new SecureRandom();

		BigInteger p, q, phi, modulus, publicKey, privateKey;

		do {
			p = BigInteger.probablePrime(bits / 2, random);
			q = BigInteger.probablePrime(bits / 2, random);
			phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

			modulus = p.multiply(q);
			publicKey = new BigInteger("65537");
			privateKey = publicKey.modInverse(phi);
		} while (modulus.bitLength() != bits || privateKey.bitLength() != bits || !phi.gcd(publicKey).equals(BigInteger.ONE));

		System.out.println("modulus: "  + modulus);
		System.out.println("public key: "  + publicKey);
		System.out.println("private key: "  + privateKey);
	}

}
