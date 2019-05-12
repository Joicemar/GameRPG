package com.getstudios.main;

import com.gcstudios.entities.BulletShoot;
import com.gcstudios.entities.Enemy;
import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Player;
import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.graficos.Ui;
import com.gcstudios.scenes.Menu;
import com.gcstudios.scenes.SaveGame;
import com.gcstudios.scenes.Scenes;
import com.gcstudios.world.World;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private Thread thread;
	private boolean isRunning = true;
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	public static int WIDTH = 240;
	public static int HEIGHT = 160;
	public static final int SCALE = 3;
	private int Num_Map = 1;
	private int Max_Map = 4;
	private BufferedImage image;
	private boolean messageGameOver = false;
	private int messageFrames = 0;
	private boolean restartGame = false;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullet;
	public static Spritesheet spritesheet;
	public static Spritesheet spritesPlayer;
	public static Spritesheet spritesPoder;
	public static World world;
	public static Player player;
	public static Random rand;
	public static Ui ui;
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("contrast.ttf");
	public Font font;
	public static Scenes scenes;
	public static String gameState = "menu";
	public Menu menu;
	public Sound sound;
	private boolean saveGame;

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH * 3, HEIGHT * 3));
		initFrame();
		this.image = new BufferedImage(WIDTH, HEIGHT, 1);

		entities = new ArrayList();

		enemies = new ArrayList();

		bullet = new ArrayList();

		spritesheet = new Spritesheet("/spritesheet.png");
		spritesPlayer = new Spritesheet("/PlayerSprites.png");
		spritesPoder = new Spritesheet("/Efeitos.png");
		player = new Player(0, 0, 16, 20, spritesPlayer.getSprite(0, 0, 16, 20));

		entities.add(player);

		world = new World("/map1.png");
		ui = new Ui();
		this.menu = new Menu();
		try {
			this.font = Font.createFont(0, this.stream).deriveFont(56.f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}

	public synchronized void start() {
		this.thread = new Thread(this);
		this.isRunning = true;
		this.thread.start();
	}

	public synchronized void stop() {
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		if (gameState == "normal") {
			if (this.saveGame) {
				this.saveGame = false;
				String[] opt1 = { "map" };
				int[] opt2 = { this.Num_Map };
				SaveGame.save(opt1, opt2);
				System.out.println("Jogo salvo!");
			}
			for (int i = 0; i < entities.size(); i++) {
				Entity e = (Entity) entities.get(i);
				e.tick();
			}
			for (int i = 0; i < bullet.size(); i++) {
				((BulletShoot) bullet.get(i)).tick();
			}
			if (enemies.size() == 0) {
				this.Num_Map += 1;
				if (this.Num_Map > this.Max_Map) {
					this.Num_Map = 1;
				}
				String newMap = "map" + this.Num_Map + ".png";
				World.changeScene(newMap);
			}
		} else if (gameState == "gameOver") {
			this.messageFrames += 1;
			if (this.messageFrames == 25) {
				this.messageFrames = 0;
				if (this.messageGameOver) {
					this.messageGameOver = false;
				} else {
					this.messageGameOver = true;
				}
			}
			if (this.restartGame) {
				this.restartGame = false;
				gameState = "normal";
				String newMap = "map" + this.Num_Map + ".png";
				World.changeScene(newMap);
			}
		} else if (gameState == "menu") {
			this.menu.tick();
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = this.image.createGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			((Entity) entities.get(i)).render(g);
		}
		for (int i = 0; i < bullet.size(); i++) {
			((BulletShoot) bullet.get(i)).render(g);
		}
		ui.render(g);
		g.dispose();
		/*É obrigatorio o graphics(g) receber o bs.getDrawGraphics() para imprimir os textos,
		 * fazer isso somente depois de renderizar os tipos graphicos*/
		g = bs.getDrawGraphics();
		g.drawImage(this.image, 0, 0, WIDTH * 3, HEIGHT * 3, null);
		g.setFont(new Font("arial", 1, 25));
		g.setColor(Color.white);
		g.drawString("Energia: " + player.energia, 23, 75);
//		g.setFont(this.font);
//		g.drawString("Teste Nova Fonte", 150, 120);
		
		if (gameState == "gameOver") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * 3, HEIGHT * 3);
			g.setFont( font );
			g.setColor(Color.white);
			g.drawString("GAME OVER ", (WIDTH - 130) / 2, HEIGHT / 2);
			if (this.messageGameOver) {
				g.setFont(new Font("arial", 1, 16));
				g.drawString("Pressione Enter para reiniciar ", (WIDTH - 170) / 2, (HEIGHT + 35) / 2);
			}
		} 
		else if (gameState == "menu") {
			this.menu.render(g);
		}
		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0D;
		double ns = 1.0E9D / amountOfTicks;
		double delta = 0.0D;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (this.isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0D) {
				tick();
				render();
				frames++;
				delta -= 1.0D;
			}
			if (!this.isRunning) {

				stop();
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == 39) || (e.getKeyCode() == 68)) {
			player.right = true;
		} else if ((e.getKeyCode() == 37) || (e.getKeyCode() == 65)) {
			player.left = true;
		}
		if ((e.getKeyCode() == 38) || (e.getKeyCode() == 87)) {
			player.up = true;
		} else if ((e.getKeyCode() == 40) || (e.getKeyCode() == 83)) {
			player.down = true;
		}
		if (e.getKeyCode() == 32) {
			player.shoot = true;
		}
		if (e.getKeyCode() == 10) {
			if (gameState == "gameOver") {
				this.restartGame = true;
			}
			if (gameState == "menu") {
				Menu.enter = true;
				Menu.pauseGame = false;
			}
		}
		if (e.getKeyCode() == 27) {
			if( gameState == "menuSlots" ) {
				gameState = "menu";
				//Menu.pauseGame = false;
			}
			/*Caso somente se o jogo estiver rodando ira pausar ou voltar*/
			if( gameState == "normal" && Menu.pauseGame == false) {
				gameState = "menu";
				Menu.pauseGame = true;
			}
			else if( gameState == "menu" && Menu.pauseGame == true) {
				gameState = "normal";
				Menu.pauseGame = false;
			}
		}
		if ((e.getKeyCode() == KeyEvent.VK_F) && (gameState == "normal")) {
			System.out.println("salvo");
			this.saveGame = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_D) || (e.getKeyCode() == KeyEvent.VK_LEFT)) {
			player.right = false;
		} else if ((e.getKeyCode() == 37) || (e.getKeyCode() == 65)) {
			player.left = false;
		}
		if ((e.getKeyCode() == 38) || (e.getKeyCode() == 87)) {
			player.up = false;
			Menu.up = true;
		} else if ((e.getKeyCode() == 40) || (e.getKeyCode() == 83)) {
			player.down = false;
			Menu.down = true;
		}
		if (e.getKeyCode() == 32) {
			player.shoot = false;
		}
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mouseX = (e.getX() / 3);
		player.mouseY = (e.getY() / 3);
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
