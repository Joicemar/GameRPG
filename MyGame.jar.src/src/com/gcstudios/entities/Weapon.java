package com.gcstudios.entities;

import java.awt.image.BufferedImage;

public class Weapon extends Entity {
	public String id;

	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.id = "weapon";
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
