package io.github.matiaskotlik.teamnitrobrew.crypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.annotation.JsonCreator;
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

public class HashedPassword {
	private static final int COST = 16;

	private String hash;

	@JsonCreator
	public HashedPassword() {}

	public HashedPassword(String password) {
		this.hash = BCrypt.withDefaults().hashToString(COST, password.toCharArray());
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public boolean verify(String password) {
		return BCrypt.verifyer().verify(password.toCharArray(),hash).verified;
	}
}
