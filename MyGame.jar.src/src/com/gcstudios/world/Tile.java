package com.gcstudios.world;

import com.gcstudios.graficos.Spritesheet;
import com.getstudios.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	public static BufferedImage TILE_TRILHA = Game.spritesheet.getSprite(32, 32, 16, 16);
	private BufferedImage sprite;
	private int x;
	private int y;
	public int heightMask;
	public int whidthMask;

	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;

		this.whidthMask = 16;
		this.heightMask = 16;
	}

	public void render(Graphics g) {
		g.drawImage(this.sprite, this.x - Camera.x, this.y - Camera.y, null);
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
