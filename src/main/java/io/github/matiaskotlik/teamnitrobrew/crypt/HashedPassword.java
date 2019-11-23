package io.github.matiaskotlik.teamnitrobrew.crypt;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@JsonDeserialize(using = HashedPassword.HashedPasswordDeserializer.class)
public class HashedPassword {
	private byte[] salt;
	private byte[] hash;
	private int cost;

	private static final String sepChar = "$";
	private static final String sepRegex = "\\$";

	public HashedPassword(byte[] salt, byte[] hash, int cost) {
		this.salt = salt;
		this.hash = hash;
		this.cost = cost;
	}

	private static final String[] hexes = new String[]{
			"00","01","02","03","04","05","06","07","08","09","0a","0b","0c","0d","0e","0f",
			"10","11","12","13","14","15","16","17","18","19","1a","1b","1c","1d","1e","1f",
			"20","21","22","23","24","25","26","27","28","29","2a","2b","2c","2d","2e","2f",
			"30","31","32","33","34","35","36","37","38","39","3a","3b","3c","3d","3e","3f",
			"40","41","42","43","44","45","46","47","48","49","4a","4b","4c","4d","4e","4f",
			"50","51","52","53","54","55","56","57","58","59","5a","5b","5c","5d","5e","5f",
			"60","61","62","63","64","65","66","67","68","69","6a","6b","6c","6d","6e","6f",
			"70","71","72","73","74","75","76","77","78","79","7a","7b","7c","7d","7e","7f",
			"80","81","82","83","84","85","86","87","88","89","8a","8b","8c","8d","8e","8f",
			"90","91","92","93","94","95","96","97","98","99","9a","9b","9c","9d","9e","9f",
			"a0","a1","a2","a3","a4","a5","a6","a7","a8","a9","aa","ab","ac","ad","ae","af",
			"b0","b1","b2","b3","b4","b5","b6","b7","b8","b9","ba","bb","bc","bd","be","bf",
			"c0","c1","c2","c3","c4","c5","c6","c7","c8","c9","ca","cb","cc","cd","ce","cf",
			"d0","d1","d2","d3","d4","d5","d6","d7","d8","d9","da","db","dc","dd","de","df",
			"e0","e1","e2","e3","e4","e5","e6","e7","e8","e9","ea","eb","ec","ed","ee","ef",
			"f0","f1","f2","f3","f4","f5","f6","f7","f8","f9","fa","fb","fc","fd","fe","ff"
	};

	@Override
	public String toString() {
		return "HashedPassword("
				+ "\n\t\tsalt = " + arrToString(salt)
				+ "\n\t\thash = " + arrToString(hash)
				+ "\n\t\tcost = " + cost + " (" + PasswordHasher.getIterations(cost) + " iterations)"
				+ "\n\t\tencoded = '" + encode() + "')";
	}

	public static String arrToString(byte[] arr) {
		StringBuilder builder = new StringBuilder("[");
		boolean first = true;
		for (byte byt : arr) {
			if (first) {
				first = false;
			} else {
				builder.append(" ");
			}
			builder.append("0x").append(hexes[Byte.toUnsignedInt(byt)]);
		}
		return builder.append("]").toString();
	}

	@JsonValue
	public String encode() {
		return Base64.getEncoder().encodeToString(salt)
				+ sepChar + Base64.getEncoder().encodeToString(hash)
				+ sepChar + cost;
	}

	public static HashedPassword decode(String encoded) {
		String[] parts = encoded.split(sepRegex);
		byte[] salt = Base64.getDecoder().decode(parts[0]);
		byte[] hash = Base64.getDecoder().decode(parts[1]);
		int cost = Integer.valueOf(parts[2]);
		return new HashedPassword(salt, hash, cost);
	}

	public byte[] getSalt() {
		return salt;
	}

	public byte[] getHash() {
		return hash;
	}

	public int getCost() {
		return cost;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HashedPassword) {
			HashedPassword hashedPassword = ((HashedPassword) obj);
			return Arrays.equals(hashedPassword.getSalt(), salt)
					&& Arrays.equals(hashedPassword.getHash(), hash);
		}
		return false;
	}

	public static class HashedPasswordDeserializer extends StdDeserializer<HashedPassword> {
		public HashedPasswordDeserializer() {
			this(null);
		}

		public HashedPasswordDeserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public HashedPassword deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JsonNode node = jp.getCodec().readTree(jp);
			String encoded = node.asText();
			return HashedPassword.decode(encoded);
		}
	}
}
