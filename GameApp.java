public class GameApp {

	public static void main(String[] args) {
		final GameData game  = new GameData();
		Context context = new Context();
		StartState startState = new StartState();
	    AnnouncementState AnnouncementState = new AnnouncementState();
		
	    final Dealer dealer  = Dealer.init(game);
	    context.setState(startState);
	    context.doAction();
	    final Player player1 = new Player(game, 0);
		final Player player2 = new Player(game, 1);
		
		Thread dealerThread  = new Thread(dealer);
		context.setState(AnnouncementState);
	    context.doAction();
		Thread player1Thread = new Thread(player1);
		Thread player2Thread = new Thread(player2);
		
		dealerThread.start();
		player1Thread.start();
		player2Thread.start();
		
		}}
