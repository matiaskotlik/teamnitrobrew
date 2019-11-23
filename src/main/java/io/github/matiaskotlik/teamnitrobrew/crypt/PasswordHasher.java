package io.github.matiaskotlik.teamnitrobrew.crypt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PasswordHasher {
	private static final int DEFAULT_COST = 16;
	private int cost;

	private static final int SIZE = 128;

	private static final SecureRandom random = new SecureRandom();
	private static SecretKeyFactory keyFactory;
	static {
		try {
			keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public PasswordHasher() {
		this(DEFAULT_COST);
	}

	public static int getIterations(int cost) {
		if (0 < cost && cost < 30) {
			return 1 << cost;
		}
		System.err.println("Invalid cost specified, defaulting to " + DEFAULT_COST);
		return 1 << DEFAULT_COST;
	}

	public PasswordHasher(int cost) {
		this.cost = cost;
	}

	public static byte[] genSalt() {
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	public HashedPassword getHashedPassword(String plaintext) {
		byte[] salt = genSalt();
		return getHashedPassword(plaintext, salt);
	}

	public HashedPassword getHashedPassword(String plaintext, byte[] salt) {
		KeySpec spec = new PBEKeySpec(plaintext.toCharArray(), salt, getIterations(cost), SIZE);
		byte[] hash;
		try {
			hash = keyFactory.generateSecret(spec).getEncoded();
		} catch (InvalidKeySpecException e) {
			System.err.println("Invalid key spec specified");
			throw new IllegalArgumentException("Most likely this plaintext is invalid: '" + plaintext + "'");
		}
		return new HashedPassword(salt, hash, cost);
	}

	public boolean check(String plaintext, HashedPassword hashedPassword) {
		return getHashedPassword(plaintext, hashedPassword.getSalt()).equals(hashedPassword);
	}
}
