package io.github.matiaskotlik.teamnitrobrew.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.matiaskotlik.teamnitrobrew.crypt.HashedPassword;

import java.util.UUID;

public class Account {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private String id;
	private String username;
	private HashedPassword hashedPassword;

	public Account(String username, HashedPassword hashedPassword) {
		this(UUID.randomUUID().toString(), username, hashedPassword);
	}

	@JsonCreator
	public Account(@JsonProperty("id") String id,
	               @JsonProperty("username") String username,
	               @JsonProperty("hashedPassword") HashedPassword hashedPassword) {
		this.id = id;
		this.username = username;
		this.hashedPassword = hashedPassword;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public HashedPassword getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(HashedPassword hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	@Override
	public String toString() {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return super.toString();
		}
	}
}
