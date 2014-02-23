import GUI.GUI;
import Host.Host;

public class Poker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GUI gui = new GUI();
		new Thread(gui).start();
		
		while(true){
			if(gui.isHost){
				//make host
				//make player
				
				break;
			}
			if(gui.isPlayer){
				//make player
				//player or spectator
				
				break;
			}
		}
		
	}

}
