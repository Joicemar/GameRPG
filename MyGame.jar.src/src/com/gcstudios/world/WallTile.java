package com.gcstudios.world;

import java.awt.image.BufferedImage;

public class WallTile extends Tile {
	public String id;

	public WallTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
