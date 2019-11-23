package io.github.matiaskotlik.teamnitrobrew;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PusherResponse {
	private Map<String, Channel> channels;

	public Map<String, Channel> getChannels() {
		return channels;
	}

	public void setChannels(Map<String, Channel> channels) {
		this.channels = channels;
	}
}

class Channel {
	@JsonProperty("user_count")
	private int userCount;

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
}
