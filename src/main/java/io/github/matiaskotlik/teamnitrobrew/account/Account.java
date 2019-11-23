package io.github.matiaskotlik.teamnitrobrew.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.matiaskotlik.teamnitrobrew.Blog;
import io.github.matiaskotlik.teamnitrobrew.crypt.HashedPassword;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private String id;
	private String username;
	private HashedPassword hashedPassword;

	private String fullname;
	private String desc;
	private String img;
	private List<Integer> ratings;
	private List<Blog> blogs;
	private double funds;

	@JsonCreator
	public Account(@JsonProperty("id") String id,
	               @JsonProperty("username") String username,
	               @JsonProperty("hashedPassword") HashedPassword hashedPassword,
	               @JsonProperty("fullname") String fullname,
	               @JsonProperty("desc") String desc,
	               @JsonProperty("img") String img,
	               @JsonProperty("ratings") List<Integer> ratings,
	               @JsonProperty("blogs") List<Blog> blogs,
	               @JsonProperty("funds") double funds) {
		this.id = id;
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.fullname = fullname;
		this.desc = desc;
		this.img = img;
		this.ratings = ratings;
		this.blogs = blogs;
		this.funds = funds;
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

	public List<Integer> getRatings() {
		return ratings;
	}

	public void setRatings(List<Integer> ratings) {
		this.ratings = ratings;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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
