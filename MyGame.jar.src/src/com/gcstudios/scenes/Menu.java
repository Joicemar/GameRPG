package com.gcstudios.scenes;

import com.getstudios.main.Game;
import com.getstudios.main.SaveGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintStream;

public class Menu {
	private static boolean SaveSlots = false;
	public String[] options = { "Novo Jogo", "Carregar Jogo", "Sair" };
	public static boolean up = false;
	public static boolean down = false;
	public static boolean enter = false;
	public static boolean escape = false;
	private int currentIndexOptions = 1;
	private int maxOptions = this.options.length - 1;
	public static boolean pauseGame = false;
 
	public void tick() {
		/* Atualiza a seta que seleciona no menu e a opção escolhida */
		if (up) {
			up = false;
			this.currentIndexOptions -= 1;
			if (this.currentIndexOptions < 0) {
				this.currentIndexOptions = this.maxOptions;
			}
		}
		if (down) {
			down = false;
			this.currentIndexOptions += 1;
			if (this.currentIndexOptions > this.maxOptions) {
				this.currentIndexOptions = 0;
			}
		}
		if( Game.gameState == "menu" ) {
			// Se enter for teclado no estado menu:
			if (enter) {
				enter = false;
				// Novo Jogo ou Continuar
				if ((this.options[this.currentIndexOptions] == "Novo Jogo") || (this.options[this.currentIndexOptions] == "Continuar")) {
					Game.gameState = "normal";
					pauseGame = false;
					if (this.options[this.currentIndexOptions] == "Novo Jogo") {
						File file = new File("save.txt");
						file.delete();
					}
				}
				// Carregar Jogo, Load Game.
				if (this.options[this.currentIndexOptions] == "Carregar Jogo") {
					Game.gameState = "menuSlots";
				}
				// Sair do programa
				if (this.options[this.currentIndexOptions] == "Sair") {
					System.exit(1);
				}
			}
		}
		if(Game.gameState == "menuSlots") {
//			if(escape) {
//				Game.gameState = "menu";
//			}
			if(enter) {
				enter = false;
				if( this.options[this.currentIndexOptions] == "Slot 1") {
					
					if (new File(SaveGame.workinDir + "\\folder\\" + "save.txt").exists()) {
						String save = SaveGame.loadGame();
						SaveGame.applySave(save);
					} else {
						System.out.println("No exists");
					}
				}
				if( this.options[this.currentIndexOptions] == "Slot 2") {
					
					if (new File(SaveGame.workinDir + "\\folder\\" + "save2.txt").exists()) {
						String save = SaveGame.loadGame();
						SaveGame.applySave(save);
					} else {
						System.out.println("No exists");
					}
				}
				if( this.options[this.currentIndexOptions] == "Slot 3") {
					
					if (new File(SaveGame.workinDir + "\\folder\\" + "save3.txt").exists()) {
						String save = SaveGame.loadGame();
						SaveGame.applySave(save);
					} else {
						System.out.println("No exists");
					}
				}
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

		if(Game.gameState == "menuSlots")  {
			if (new File(SaveGame.workinDir + "\\folder\\" + "save.txt").exists()) {
				g.drawString("Slot 1"+" Salvo", Game.WIDTH * 3 / 2 - 100, 150);				
				this.options[0] = "Slot 1";
			}
			else {
				g.drawString("Slot 1", Game.WIDTH * 3 / 2 - 100, 150);
				this.options[0] = "Slot 1";
			}
			if (new File(SaveGame.workinDir + "\\folder\\" + "save2.txt").exists()) {
				g.drawString("Slot 2"+" Salvo", Game.WIDTH * 3 / 2 - 100, 150);				
				this.options[0] = "Slot 2";
			}
			else {
				g.drawString("Slot 2", Game.WIDTH * 3 / 2 - 100, 220);
				this.options[1] = "Slot 2";
			}
			if (new File(SaveGame.workinDir + "\\folder\\" + "save3.txt").exists()) {
				g.drawString("Slot 3"+" Salvo", Game.WIDTH * 3 / 2 - 100, 150);				
				this.options[0] = "Slot 3";
			}
			else {
				g.drawString("Slot 3", Game.WIDTH * 3 / 2 - 100, 290);
				this.options[2] = "Slot 3";
			}
		}
		else {

			if (pauseGame) {
				g.drawString("Continuar", Game.WIDTH * 3 / 2 - 100, 150);
				this.options[0] = "Continuar";
			} 
			else {
				g.drawString("Novo Jogo", Game.WIDTH * 3 / 2 - 100, 150);
				this.options[0] = "Novo Jogo";
			}
			g.drawString("Carregar Jogo", Game.WIDTH * 3 / 2 - 100, 220);
			this.options[1] = "Carregar Jogo";
			
			g.drawString("Sair", Game.WIDTH * 3 / 2 - 100, 290);
			this.options[2] = "Sair";
			
		}
		g.setFont(new Font("arial", 1, 26));
		g.setColor(Color.white);
		if (this.options[this.currentIndexOptions] == "Novo Jogo" || this.options[this.currentIndexOptions] == "Slot 1") {
			g.drawString(":) >", Game.WIDTH * 3 / 2 - 160, 150);
		} else if (this.options[this.currentIndexOptions] == "Carregar Jogo" || this.options[this.currentIndexOptions] == "Slot 2") {
			g.drawString(":D >", Game.WIDTH * 3 / 2 - 160, 220);
		} else if (this.options[this.currentIndexOptions] == "Sair" || this.options[this.currentIndexOptions] == "Slot 3") {
			g.drawString(":( >", Game.WIDTH * 3 / 2 - 160, 290);
		} else if (this.options[this.currentIndexOptions] == "Continuar") {
			g.drawString(":) >", Game.WIDTH * 3 / 2 - 160, 150);
		}
	}
}
