package com.gcstudios.entities;

import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.world.Camera;
import com.gcstudios.world.World;
import com.getstudios.main.Game;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

public class Enemy extends Entity {
	private double speed = 0.2D;
	private int maskX = 5;
	private int maskY = 5;
	private int maskW = 8;
	private int maskH = 8;
	private int frames = 0;
	private int maxframes = 25;
	private int index = 0;
	private int maxIndex = 1;
	private BufferedImage[] anim;
	private boolean moved;
	private int life = 10;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.anim = new BufferedImage[2];
		this.anim[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		this.anim[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	}

	public void tick() {
		if (!isCollidingWithPlayer()) {
			if (((int) this.x < Game.player.getX()) && (World.isFree((int) (this.x + this.speed), getY()))
					&& (!isColliding((int) (this.x + this.speed), getY()))) {
				this.x += this.speed;
			} else if (((int) this.x > Game.player.getX()) && (World.isFree((int) (this.x - this.speed), getY()))
					&& (!isColliding((int) (this.x - this.speed), getY()))) {
				this.x -= this.speed;
			} else if (((int) this.y < Game.player.getY()) && (World.isFree(getX(), (int) (this.y + this.speed)))
					&& (!isColliding(getX(), (int) (this.y + this.speed)))) {
				this.y += this.speed;
			} else if (((int) this.y > Game.player.getY()) && (World.isFree(getX(), (int) (this.y - this.speed)))
					&& (!isColliding(getX(), (int) (this.y - this.speed)))) {
				this.y -= this.speed;
			}
		} else if (Game.rand.nextInt(100) < 11) {
			Game.player.life -= 1;
			Game.player.isDamage = true;
		}
		this.frames += 1;
		if (this.frames == this.maxframes) {
			this.frames = 0;
			this.index += 1;
			if (this.index > this.maxIndex) {
				this.index = 0;
			}
		}
		collisionBullets();
	}

	public void collisionBullets() {
		for (int i = 0; i < Game.bullet.size(); i++) {
			BulletShoot e = (BulletShoot) Game.bullet.get(i);
			if (Entity.collisionCheck(this, e)) {
				this.life -= e.Damage;
				Game.bullet.remove(i);
				if (this.life <= 0) {
					Game.enemies.remove(this);
					Game.entities.remove(this);
				}
			}
		}
	}

	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(getX() + this.maskX, getY() + this.maskY, this.maskW, this.maskH);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemyCurrent.intersects(player);
	}

	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + this.maskX, ynext + this.maskY, this.maskW, this.maskH);
		for (Entity e : Game.enemies) {
			if (e != this) {
				Rectangle targetEnemy = new Rectangle(e.getX() + this.maskX, e.getY() + this.maskY, this.maskW,
						this.maskH);
				if (enemyCurrent.intersects(targetEnemy)) {
					return true;
				}
			}
		}
		return false;
	}

	public void render(Graphics g) {
		g.drawImage(this.anim[this.index], getX() - Camera.x, getY() - Camera.y, null);
	}
}
