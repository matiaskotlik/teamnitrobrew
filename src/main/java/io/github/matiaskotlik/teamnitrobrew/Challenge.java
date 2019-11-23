package io.github.matiaskotlik.teamnitrobrew;

public class Challenge {
	private String title;
	private String text;
	private Category category;
	private int points;

	public Challenge(String title, String text, Category category, int points) {
		this.title = title;
		this.text = text;
		this.category = category;
		this.points = points;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
