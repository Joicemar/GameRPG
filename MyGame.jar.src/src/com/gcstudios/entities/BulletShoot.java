package com.gcstudios.entities;

import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.world.Camera;
import com.gcstudios.world.Tile;
import com.gcstudios.world.WallTile;
import com.gcstudios.world.World;
import com.getstudios.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class BulletShoot extends Entity {
	private double dx;
	private double dy;
	public int speed = 4;
	private int curLife = 0;
	private int lifeDuration = 150;
	public int Damage = 15;
	private static BufferedImage[] efeitoPoder;
	private int poderAnim = 3;
	private int index = 0;
	private int maxframes = 10;
	private int frames = 0;
	private int maxIndex = 2;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;

		efeitoPoder = new BufferedImage[this.poderAnim];
		for (int i = 0; i < this.poderAnim; i++) {
			efeitoPoder[i] = Game.spritesPoder.getSprite(0 + i * 16, 0, 16, 16);
		}
	}

	public void tick() {
		this.x += this.dx * this.speed;
		this.y += this.dy * this.speed;

		this.curLife += 1;
		if (this.curLife == this.lifeDuration) {
			Game.bullet.remove(this);
			this.curLife = 0;
			return;
		}
		checkTileCollision();

		this.frames += 1;
		if (this.frames == this.maxframes) {
			this.frames = 0;
			this.index += 1;
			if (this.index > this.maxIndex) {
				this.index = 0;
			}
		}
	}

	public void checkTileCollision() {
		Tile[] arrayOfTile;
		int j = (arrayOfTile = World.tiles).length;
		for (int i = 0; i < j; i++) {
			Tile e = arrayOfTile[i];
			if (((e instanceof WallTile)) && (collisionCheck(e, this))) {
				Game.bullet.remove(this);
			}
		}
	}

	public void render(Graphics g) {
		g.drawImage(efeitoPoder[this.index], getX() - Camera.x, getY() - Camera.y, 12, 12, null);
	}

	public int getSpeed() {
		return this.speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
