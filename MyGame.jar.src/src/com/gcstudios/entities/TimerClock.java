package com.gcstudios.entities;
/**
 * Esta classe serve apenas para mostrar a que tempo o programa está sendo
 * executado.
 */
public class TimerClock {

	static double timer = System.currentTimeMillis();
	static int segundos = 0;
	static int minutos = 0;
	static int horas = 0;

	public static void time() {
		
		if (System.currentTimeMillis() - timer >= 1000) {
			//System.currentTimeMillis() Retorna a hora atual em milissegundos
			if (segundos == 60) {
				minutos ++;
				segundos = 0;
			}
			if ( minutos == 60) {
				horas ++;
				minutos = 0;
			}
			System.out.println("tempo corrido: " + horas + " :" + minutos + " :" + segundos++);
			timer = System.currentTimeMillis();
			
		}
	}

	public static int countSeconds(int MaxSeconds) {
		
		if (System.currentTimeMillis() - timer >= 1000) {
			//System.currentTimeMillis() Retorna a hora atual em milissegundos
			if (segundos == MaxSeconds) {
				minutos ++;
				segundos = 0;
			}
			if ( minutos == 60) {
				horas ++;
				minutos = 0;
			}
			System.out.println("tempo corrido: " + horas + " :" + minutos + " :" + segundos++);
			timer = System.currentTimeMillis();
			
		}
		return segundos;
	}
	
	public int getSegundos() {
		
		return segundos;
	}

}
