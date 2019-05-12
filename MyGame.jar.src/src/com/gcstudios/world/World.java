package com.gcstudios.world;

import com.gcstudios.entities.Bullet;
import com.gcstudios.entities.Enemy;
import com.gcstudios.entities.Entity;
import com.gcstudios.entities.LifePack;
import com.gcstudios.entities.Player;
import com.gcstudios.entities.Weapon;
import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.graficos.Ui;
import com.getstudios.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class World {
	public static Tile[] tiles;
	private static Enemy enemy;
	public static int WIDTH;
	public static int HEIGHT;
	public static final int TILE_SIZE = 16;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];

			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int yy = 0; yy < map.getHeight(); yy++) {
				for (int xx = 0; xx < map.getWidth(); xx++) {
					int pixelAtual = pixels[(xx + yy * map.getWidth())];
					tiles[(xx + yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (pixelAtual == -16777216) {
						tiles[(xx + yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					}
					if (pixelAtual == -8933586) {
						tiles[(xx + yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_TRILHA);
					} else if (pixelAtual == -1) {
						WallTile wallTile = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
						wallTile.setId("solid");
						tiles[(xx + yy * WIDTH)] = wallTile;
					} else if (pixelAtual == -6250336) {
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (pixelAtual == -65536) {
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.enemies.add(en);
						Game.entities.add(en);
					} else if (pixelAtual == -38400) {
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
					} else if (pixelAtual == -16767233) {
						LifePack pack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN);
						pack.setId("life");
						Game.entities.add(pack);
					} else if (pixelAtual == 0xFFFFD800) {
						Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void changeScene(String map) {
		Game.entities = new ArrayList();
		Game.enemies = new ArrayList();

		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Spritesheet spritPlayer = new Spritesheet("/2p00.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 0, 16, 16));

		Game.entities.add(Game.player);

		Game.world = new World("/" + map);
		Game.ui = new Ui();
	}

	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / 16;
		int y1 = ynext / 16;

		int x2 = (xnext + 16 - 1) / 16;
		int y2 = ynext / 16;

		int x3 = xnext / 16;
		int y3 = (ynext + 16 - 1) / 16;

		int x4 = (xnext + 16 - 1) / 16;
		int y4 = (ynext + 16 - 1) / 16;

		return (!(tiles[(x1 + y1 * WIDTH)] instanceof WallTile)) 
				&& (!(tiles[(x2 + y2 * WIDTH)] instanceof WallTile))
				&& (!(tiles[(x3 + y3 * WIDTH)] instanceof WallTile))
				&& (!(tiles[(x4 + y4 * WIDTH)] instanceof WallTile));
	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;

		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if ((xx >= 0) && (yy >= 0) && (xx < WIDTH) && (yy < HEIGHT)) {
					Tile tile = tiles[(xx + yy * WIDTH)];
					tile.render(g);
				}
			}
		}
	}
}
