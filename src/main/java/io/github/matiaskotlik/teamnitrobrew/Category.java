package io.github.matiaskotlik.teamnitrobrew;

public enum Category {
	CRYPTOGRAPHY, WEB, EXAMPLE;

	private String title;

	Category() {
		title = toString().toLowerCase();
		String first = title.substring(0,1);
		title = title.replaceFirst(first, first.toUpperCase());
	}

	Category(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
