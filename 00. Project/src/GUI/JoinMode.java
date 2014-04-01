package GUI;

import java.util.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import GuiActionThreads.JoinHostThread;
import Network.HostSearcher;

public class JoinMode extends Mode {

	private final String[] joinListColumnNames = {"HOST", "IP ADDRESS"};//, "PLAYERS"};
	private final int[] joinListColumnWidths = {220, 180};//, 120};
	
	private final String failedJoinString = "Selected game lobby is no longer availabe.";
	
		
	private Image background;
	private JoinList joinList;
	
	private PopupMessageOneButton popupFailedJoin;
	
	protected List<String[]> gamesInfo;
	protected List<Boolean>  gamesJoinable;
	
	protected String playerName;	// passed from StartMode
	
	
	// status flags
	public boolean joinHostSuccess_flag;
	public boolean joinHostError_flag;
		
	
	@Override
	public void init(GameContainer container, final StateBasedGame game)
			throws SlickException {
		
		super.init(container, game);
		
		background = new Image(GUI.RESOURCES_PATH+"background.png");
		
				
				
		popupFailedJoin = new PopupMessageOneButton(container, failedJoinString,
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {	// ok action
						updateGamesList();
						joinList.setVisible(true);
					}
				});
		
		
		joinList = new JoinList(container, true,
				joinListColumnNames, joinListColumnWidths,
				new JoinList.IndexedComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source,
							int index){								// join action
						
						// JOIN GAME
						String ipString = gamesInfo.get(index)[1];
						JoinHostThread jht = new JoinHostThread(ipString, playerName);
						jht.start();
						
						showPopupLoading(source);
					}
				},
				new ComponentListener() {	
					@Override
					public void componentActivated(AbstractComponent arg0) {	// refresh action
						
						System.out.println("refresh games list!");
						
						// UPDATE GAMES LIST
						updateGamesList();
					}
				},
				new ComponentListener() {			// cancel action
					
					@Override
					public void componentActivated(AbstractComponent arg0) {
						game.enterState(1);
					}
				});
		
		
		gamesInfo = new ArrayList<String[]>();
		gamesJoinable = new ArrayList<Boolean>();
		/*
		String[] data = {"host_name", "192.128.234.122", "3/8"};
		for (int i=0; i<20; ++i) {
			gamesInfo.add(data);
			gamesJoinable.add(7854%(i+1)==0);
		}*/
		joinList.setRowsData(gamesInfo);
		joinList.setRowsJoinable(gamesJoinable);
	}
	
	
	
	public void updateGamesList() {
		
		gamesInfo = HostSearcher.getValidNamesAndIps();
		gamesJoinable.clear();
		for (int i=0; i<gamesInfo.size(); ++i)
			gamesJoinable.add(true);
		joinList.setRowsData(gamesInfo);
		joinList.setRowsJoinable(gamesJoinable);
	}
	
	
	private void showPopupLoading(AbstractComponent source) {
		joinList.setVisible(false);
		popupLoading.setVisible(source);
	}
	private void showPopupFailedJoin(AbstractComponent source) {
		joinList.setVisible(false);
		popupFailedJoin.setVisible(source);
	}
	
	
	
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_1))
			game.enterState(1);
		else if (container.getInput().isKeyPressed(Input.KEY_3))
			game.enterState(3);
		else if (container.getInput().isKeyPressed(Input.KEY_4))
			game.enterState(4);
		
		
		// if HostSearcher is not running, start it
		if (!HostSearcher.isRunning())
			HostSearcher.start(4320);
		
		// if we're trying to join a host, check the status of it
		if (popupLoading.isVisible()) {
			if (joinHostSuccess_flag){
				popupLoading.setInvisible();
				joinList.setVisible(true);
				// get game state before going to lobby mode?
				System.out.println("connection to host established... going to lobby");
				game.enterState(3);
				
			} else if (joinHostError_flag) {
				popupLoading.setInvisible();
				showPopupFailedJoin(popupLoading.getPopupSource());
			}
		}
		
		/*
		if (container.getInput().isKeyPressed(Input.KEY_F)) {
			if (!gamesInfo.isEmpty()) {
				gamesInfo.remove(0);
				gamesJoinable.remove(0);
			}
			joinList.setRowsData(gamesInfo);
			joinList.setRowsJoinable(gamesJoinable);
		}*/
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		background.draw(0, 0, container.getWidth(), container.getHeight());
		
		joinList.render(container, g);
		
		popupLoading.render(container, g);
		popupFailedJoin.render(container, g);
	}

	

	@Override
	public int getID() {
		return 2;
	}
	
	
}
