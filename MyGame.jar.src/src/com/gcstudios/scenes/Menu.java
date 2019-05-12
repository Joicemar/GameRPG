package com.gcstudios.scenes;

import com.getstudios.main.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintStream;

public class Menu {
	public String[] options = { "Novo Jogo", "Carregar Jogo", "Sair" };
	public static boolean up = false;
	public static boolean down = false;
	public static boolean enter = false;
	private int currentOptions = 0;
	private int maxOptions = this.options.length - 1;
	public static boolean pauseGame = false;

	public void tick() {

		if (up) {
			up = false;
			this.currentOptions -= 1;
			if (this.currentOptions < 0) {
				this.currentOptions = this.maxOptions;
			}
		}
		if (down) {
			down = false;
			this.currentOptions += 1;
			if (this.currentOptions > this.maxOptions) {
				this.currentOptions = 0;
			}
		}
		if (enter) {
			enter = false;
			if ((this.options[this.currentOptions] == "Novo Jogo")
					|| (this.options[this.currentOptions] == "Continuar")) {
				Game.gameState = "normal";
				pauseGame = false;
				if (this.options[this.currentOptions] == "Novo Jogo") {
					File file = new File("save.txt");
					file.delete();
				}
			}
			if (this.options[this.currentOptions] == "Carregar Jogo") {
				if (new File(SaveGame.workinDir + "\\folder\\"+ "save.txt").exists()) {
					String save = SaveGame.loadGame( );
					System.out.println(save);
					SaveGame.applySave(save);
				} else {
					System.out.println("No exists");
				}
			}
			if (this.options[this.currentOptions] == "Sair") {
				System.exit(1);
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH * 3, Game.HEIGHT * 3);

		g.setColor(Color.RED);
		g.setFont(new Font("arial", 0, 56));

		g.drawString("10.000 A.C", Game.WIDTH * 3 / 2 - 140, 70);

		g.setFont(new Font("arial", 1, 36));
		g.setColor(Color.white);
		if (pauseGame) {
			g.drawString("Continuar", Game.WIDTH * 3 / 2 - 100, 150);
			this.options[0] = "Continuar";
		} else {
			g.drawString("Novo Jogo", Game.WIDTH * 3 / 2 - 100, 150);
			this.options[0] = "Novo Jogo";
		}
		g.drawString("Carregar Jogo", Game.WIDTH * 3 / 2 - 100, 220);

		g.drawString("Sair", Game.WIDTH * 3 / 2 - 100, 290);

		g.setFont(new Font("arial", 1, 26));
		g.setColor(Color.white);
		if (this.options[this.currentOptions] == "Novo Jogo") {
			g.drawString(":) >", Game.WIDTH * 3 / 2 - 160, 150);
		} else if (this.options[this.currentOptions] == "Carregar Jogo") {
			g.drawString(":D >", Game.WIDTH * 3 / 2 - 160, 220);
		} else if (this.options[this.currentOptions] == "Sair") {
			g.drawString(":( >", Game.WIDTH * 3 / 2 - 160, 290);
		} else if (this.options[this.currentOptions] == "Continuar") {
			g.drawString(":) >", Game.WIDTH * 3 / 2 - 160, 150);
		}
	}
}
