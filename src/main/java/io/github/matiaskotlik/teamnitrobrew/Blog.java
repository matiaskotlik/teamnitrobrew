package io.github.matiaskotlik.teamnitrobrew;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Blog {
	private String meta;
	private String title;
	private String text;
	private String img;

	// for jackson
	public Blog() {}

	public Blog(String meta, String title, String text, String img) {
		this.meta = meta;
		this.title = title;
		this.text = text;
		this.img = img;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}
