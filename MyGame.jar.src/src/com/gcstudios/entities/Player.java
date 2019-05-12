package com.gcstudios.entities;

import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.world.Camera;
import com.gcstudios.world.World;
import com.getstudios.main.Game;
import com.getstudios.main.Sound;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class Player extends Entity {
	Sound sound;
	private int soundLimite = 30;
	private int soundContTime = 0;
	public boolean right;
	public boolean up;
	public boolean left;
	public boolean down;
	public double speed = 1.3D;
	private int right_dir = 0;
	private int left_dir = 1;
	public int dir = this.right_dir;
	private int frames = 0;
	private int maxframes = 5;
	private int index = 0;
	private int maxIndex = 4;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	public BufferedImage playerDamage;
	public boolean isDamage = false;
	public int life = 80;
	public int maxLife = 80;
	public int maxEnergia = 100;
	public int energia = 100;
	private int framesAnim = 0;
	private boolean armado;
	public boolean shoot = false;
	public boolean mouseShoot = false;
	public int mouseX;
	public int mouseY;
	private int arrayAnim = 5;
	private int poderAnim = 2;
	private BufferedImage playerStaticRight;
	private BufferedImage playerStaticLeft;
	private BufferedImage[] efeitoPoder;
	
	private int timeEnergiaRecovery = 0,  startFrameTime = 0, stopFrameTime = 60;
	//private int maxTimeRecovery ; // Não esta sendo usada no momento
	
	private boolean canRecovery = false;
	TimerClock time;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.sound = new Sound("/hurt.wav");
		this.rightPlayer = new BufferedImage[this.arrayAnim];
		this.leftPlayer = new BufferedImage[this.arrayAnim];
		time = new TimerClock();
		this.playerDamage = Game.spritesPlayer.getSprite(0, 0, 16, 20);
		this.playerStaticRight = Game.spritesPlayer.getSprite(0, 0, 16, 20);
		this.playerStaticLeft = Game.spritesPlayer.getSprite(80, 20, 16, 20);
		for (int i = 0; i < this.arrayAnim; i++) {
			this.rightPlayer[i] = Game.spritesPlayer.getSprite(16 + i * 16, 0, 16, 20);
			this.leftPlayer[i] = Game.spritesPlayer.getSprite(this.arrayAnim * 16 - 16 - i * 16, 20, 16, 20);
		}
	}

	public void tick() {
		this.moved = false;
		if ((this.right) && (World.isFree((int) (this.x + this.speed), getY()))) {
			this.moved = true;
			this.dir = this.right_dir;
			this.x += this.speed;
		} else if ((this.left) && (World.isFree((int) (this.x - this.speed), getY()))) {
			this.moved = true;
			this.dir = this.left_dir;
			this.x -= this.speed;
		}
		if ((this.up) && (World.isFree(getX(), (int) (this.y - this.speed)))) {
			this.moved = true;
			this.y -= this.speed;
		} else if ((this.down) && (World.isFree(getX(), (int) (this.y + this.speed)))) {
			this.moved = true;
			this.y += this.speed;
		}
		if (this.moved) {
			this.frames += 1;
			if (this.frames == this.maxframes) {
				this.frames = 0;
				this.index += 1;
				if (this.index > this.maxIndex) {
					this.index = 0;
				}
			}
		}
		checkGenericCollision();
		if (this.isDamage) {
			this.framesAnim += 1;
			if (this.framesAnim >= 10) {
				this.isDamage = false;
				this.framesAnim = 0;
			}
		}
		if (this.life <= 0) {
			Game.gameState = "gameOver";
		}
		if ((this.shoot) && (this.armado) && (this.energia > 0)) {
			this.energia -= 1;
			this.shoot = false;
			int dx = 0;
			int posX = 0;
			int posY = 0;
			if (this.dir == this.left_dir) {
				dx = -1;
			} else {
				dx = 1;
			}
			if (this.dir == this.left_dir) {
				BulletShoot b = new BulletShoot(getX() - 8, getY() + 6, 3, 2, null, dx, 0.0D);
				Game.bullet.add(b);
			}
			if (this.dir == this.right_dir) {
				BulletShoot b = new BulletShoot(getX() + 17, getY() + 6, 3, 2, null, dx, 0.0D);
				Game.bullet.add(b);
			}
		}
		if ((this.mouseShoot) && (this.armado) && (this.energia > 0)) {
			this.energia -= 1;
			this.mouseShoot = false;
			int dx = 0;
			int dy = 6;
			double angle = 0.0D;
			if (this.dir == this.left_dir) {
				dx = -8;
				angle = Math.atan2(this.mouseY - (getY() + dy - Camera.y), this.mouseX - (getX() + dx - Camera.x));
			} else {
				dx = 17;
				angle = Math.atan2(this.mouseY - (getY() + dy - Camera.y), this.mouseX - (getX() + dx - Camera.x));
			}
			double targetX = Math.cos(angle);
			double targetY = Math.sin(angle);

			BulletShoot b = new BulletShoot(getX() + dx, getY() + dy, 16, 16, null, targetX, targetY);
			b.setMask(b.getX() + 5, b.getY() + 5, 6, 6);
			b.speed = 1;
			Game.bullet.add(b);
		}
		if (this.isDamage) {
			this.soundContTime += 1;
			if (this.soundContTime >= this.soundLimite) {
				this.soundContTime = 0;
			}
		}
		/** Recuperação de energia e vida. Classe que conta o tempo é usada */
		TimerClock.countSeconds(3);
		timeEnergiaRecovery = TimerClock.countSeconds(3);
		
		if( timeEnergiaRecovery >= 3 ) {
			startFrameTime++;
			if( startFrameTime == stopFrameTime && !this.isDamage) {
				
				startFrameTime = 0;
				if(energia < maxEnergia) {
					energia ++;
				}
			}
		}
		
		updateCamera();
	}

	public void updateCamera() {
		Camera.x = Camera.clamp(getX() - Game.WIDTH / 2, 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(getY() - Game.HEIGHT / 2, 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void checkGenericCollision() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = (Entity) Game.entities.get(i);
			if (((atual instanceof Weapon)) && (Entity.collisionCheck(this, atual))) {
				this.armado = true;
				Game.entities.remove(atual);
			}
			if ((atual.getId() == "bullet") && (Entity.collisionCheck(this, atual))) {
				this.energia += 102;
				Game.entities.remove(atual);
			}
			if ((atual.getId() == "life") && (Entity.collisionCheck(this, atual))) {
				this.life += 10;
				if (this.life > this.maxLife) {
					this.life = this.maxLife;
				}
				Game.entities.remove(atual);
			}
		}
	}

	int the_16 = 16;

	public void render(Graphics g) {
		//Animação sem receber dano
		if (!this.isDamage) {
			if (!this.moved) {
				if (this.dir == this.right_dir) {
					g.drawImage(this.playerStaticRight, getX() - Camera.x, getY() - Camera.y, this.the_16, this.the_16,
							null);
					if (this.armado) {
						g.drawImage(Entity.GUN_RIGTH, getX() + 8 - Camera.x, getY() - Camera.y, this.the_16,
								this.the_16, null);
					}
				} else {
					g.drawImage(this.playerStaticLeft, getX() - Camera.x, getY() - Camera.y, this.the_16, this.the_16,
							null);
					if (this.armado) {
						g.drawImage(Entity.GUN_LEFT, getX() - 8 - Camera.x, getY() - Camera.y, this.the_16, this.the_16,
								null);
					}
				}
			} else if (this.dir == this.right_dir) {
				g.drawImage(this.rightPlayer[this.index], getX() - Camera.x, getY() - Camera.y, this.the_16,
						this.the_16, null);
				if (this.armado) {
					g.drawImage(Entity.GUN_RIGTH, getX() + 8 - Camera.x, getY() - Camera.y, this.the_16, this.the_16,
							null);
				}
			} else if (this.dir == this.left_dir) {
				g.drawImage(this.leftPlayer[this.index], getX() - Camera.x, getY() - Camera.y, this.the_16, this.the_16,
						null);
				if (this.armado) {
					g.drawImage(Entity.GUN_LEFT, getX() - 8 - Camera.x, getY() - Camera.y, this.the_16, this.the_16,
							null);
				}
			}
		} else {
			if (this.soundContTime == 29) {
				this.sound.play();
			}
			// Animação recebendo dano
			g.drawImage(this.playerDamage, getX() - Camera.x, getY() - Camera.y, this.the_16, this.the_16, null);
			if (this.armado) {
				if (this.dir == this.right_dir) {
					g.drawImage(Entity.GUN_RIGTH, getX() + 8 - Camera.x, getY() - Camera.y, this.the_16, this.the_16,
							null);
				} else {
					g.drawImage(Entity.GUN_LEFT, getX() - 8 - Camera.x, getY() - Camera.y, this.the_16, this.the_16,
							null);
				}
			}
		}
	}
}
