package com.gcstudios.entities;

import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.world.Camera;
import com.gcstudios.world.Tile;
import com.getstudios.main.Game;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(96, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(0, 48, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(96, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(112, 16, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(0, 48, 16, 16);
	public static BufferedImage GUN_RIGTH = Game.spritesheet.getSprite(16, 48, 16, 16);
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected BufferedImage sprite;
	private int maskx;
	private int masky;
	private int whidthMask;
	private int heightMask;
	public String id;

	public void setMask(int maskX, int maskY, int whidthMask, int heightMask) {
		this.maskx = maskX;
		this.masky = maskY;
		this.whidthMask = whidthMask;
		this.heightMask = heightMask;
	}

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		if (sprite != null) {
			this.sprite = sprite;
		} else {
			this.sprite = null;
		}
		this.maskx = 0;
		this.masky = 0;
		this.whidthMask = width;
		this.heightMask = height;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void tick() {
	}

	public void render(Graphics g) {
		g.drawImage(this.sprite, getX() - Camera.x, getY() - Camera.y, null);
	}

	public static boolean collisionCheck(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.heightMask, e1.whidthMask);
		Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.heightMask, e2.whidthMask);

		return e1Mask.intersects(e2Mask);
	}

	public static boolean collisionCheck(Tile e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.heightMask, e1.whidthMask);
		Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.heightMask, e2.whidthMask);

		return e2Mask.intersects(e1Mask);
	}
}
