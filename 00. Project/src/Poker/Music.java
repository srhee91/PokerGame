package Poker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public abstract class Music {
	static AudioStream ring=null;
	static FileInputStream file=null;
	
	public static void start(){
		try{
			file=new FileInputStream(new File("D:\\whole.wav"));
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
	
	
	public static void main(String[] args){
		new musics().start();
	}
	
}

class musics extends Thread{
	public void run(){
		int time=3000;//3*60000+34000;
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