package io.github.matiaskotlik.teamnitrobrew.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.matiaskotlik.teamnitrobrew.crypt.HashedPassword;

import java.util.ArrayList;
import java.util.UUID;

public class Account {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private String id;
	private String username;
	private HashedPassword hashedPassword;

	private ArrayList<Integer> ratings;
	private double funds = 0;

	public Account(String username, HashedPassword hashedPassword) {
		this(UUID.randomUUID().toString(), username, hashedPassword, -1, 0);
	}

	@JsonCreator
	public Account(@JsonProperty("id") String id,
	               @JsonProperty("username") String username,
	               @JsonProperty("hashedPassword") HashedPassword hashedPassword,
	               @JsonProperty("avgRating") double avgRating,
	               @JsonProperty("funds") double funds) {
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

	public double getAvgRating() {
		return ratings.stream().reduce(Integer::sum).orElse(0) / ((double) ratings.size());
	}

	public double getFunds() {
		return funds;
	}

	public void setFunds(double funds) {
		this.funds = funds;
	}

	public ArrayList<Integer> getRatings() {
		return ratings;
	}

	public void setRatings(ArrayList<Integer> ratings) {
		this.ratings = ratings;
	}

	public void updateFunds(double amt) {
		funds += amt;
	}

	public void updateAvgRating(int rating) {
		ratings.add(rating);
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
