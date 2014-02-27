package Host;

import GameState.TableInfo;

//library of game algorithm
public class GameSystem {
	public GameSystem(){
		Deck obj=new Deck();
		obj.shuffle();
		obj.drawcardsToPlayer(3);
		TableInfo table=new TableInfo(null);
		if(table.flopTurnRiverState==3){
			CalculateRank obj1=new CalculateRank();
			obj1.merge();
		}
	}
}
