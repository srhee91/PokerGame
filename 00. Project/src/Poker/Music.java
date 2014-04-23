package Poker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public abstract class Music {
	static AudioStream ring=null;
	static FileInputStream file=null;
	
	public static void oneChipSound(){
		AudioStream cardSound=null;
		try {
			cardSound=new AudioStream(new FileInputStream(new File("OneChip.wav")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		AudioPlayer.player.start(cardSound);
	}
	
	public static void manyChipSound(){
		AudioStream cardSound=null;
		try {
			cardSound=new AudioStream(new FileInputStream(new File("ManyChips.wav")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		AudioPlayer.player.start(cardSound);
	}
	
	public static void twoCardsSound(){
		AudioStream cardSound=null;
		try {
			cardSound=new AudioStream(new FileInputStream(new File("2Cards.wav")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		AudioPlayer.player.start(cardSound);
	}
	
	public static void manyCardsSound(){
		AudioStream cardSound=null;
		try {
			cardSound=new AudioStream(new FileInputStream(new File("manyCards.wav")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		AudioPlayer.player.start(cardSound);
	}
	
	public static void start(){
		try{
			file=new FileInputStream(new File("whole.wav"));
			ring=new AudioStream(file);  
		}catch(Exception e){
			e.printStackTrace();
		}
		AudioPlayer.player.start(ring);
	}
	
	public static void stop(){
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

class musics extends Thread{
	public void run(){
		int time=3*60000+34000;
		while(true){
			Music.start();
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Music.stop();	
		}
	}
}