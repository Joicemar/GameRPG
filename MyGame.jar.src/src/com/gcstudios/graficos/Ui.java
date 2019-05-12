package com.gcstudios.graficos;

import com.gcstudios.entities.Player;
import com.getstudios.main.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Ui {
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(8, 5, 100, 10);
		g.setColor(Color.GREEN);
		g.fillRect(8, 5, Game.player.life * 100 / Game.player.maxLife, 10);
		g.setColor(Color.white);
		g.setFont(new Font("arial", 1, 10));
		g.drawString(Game.player.life + " / " + Game.player.maxLife, 37, 14);
	}
}
