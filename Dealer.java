import java.util.ArrayList;
import java.util.Random;

public class Dealer implements Runnable {
	
	private GameData gameData; //shared data   
	ArrayList<Integer> dealerBoardNumbers = new ArrayList<Integer>();
	
	
	private static Dealer dealer = null;
	private Dealer(GameData gameData) {
		this.gameData = gameData;	
		for(int i = 0; i < 10; i++) {
			Random rand = new Random();
			dealerBoardNumbers.add(rand.nextInt(49)+1);}}
	
	public static Dealer getInstance(){ 
		if(dealer == null) {
            throw new AssertionError("You have to call init first");}
		return dealer;}
	
	public synchronized static Dealer init(GameData gameData) {
        if (dealer != null)
        {
            // in my opinion this is optional, but for the purists it ensures
            // that you only ever get the same instance when you call getInstance
            throw new AssertionError("You already initialized one object");
        }

        dealer = new Dealer(gameData);
        return dealer;
    }


	
	public void run() {
		
		/* STEP-1: write code to take a lock on gameData using lock1*/ 
		int i=0;
		synchronized(gameData.lock1) {			
			
			/* STEP-2: specify condition for player1 and specify condition for player2 */
			// dealer executes until either (or both) players sets their playerSuccessFlag 
			
			while (!gameData.playerSuccessFlag[0] && !gameData.playerSuccessFlag[1] && i<10) {
				
				// set number announced flag to false before announcing the number
				gameData.noAnnouncedFlag = false;
				
				// set checked flag of both players as false before the number is announced
				gameData.playerChanceFlag[0] = false;
				gameData.playerChanceFlag[1] = false;
				 
				try {
				    Thread.sleep(1000);
				} catch (Exception e) {
				    e.printStackTrace();
				}
				gameData.announcedNumber = dealerBoardNumbers.get(i);
				i++;
				System.out.println("Moderator Generated:" + gameData.announcedNumber);
				// STEP-5: reset the announced number
				// reset the announced number
				
				// STEP-6: communicate to the players that the number is announced
				// using one of the variables in GameData 
				// set number announced to true on GameData for waiting players
				
				gameData.noAnnouncedFlag = true;
				
				// STEP-7: notify all the players waiting for the number to be announced 
				// by the dealer using lock1 of GameData
				// Notify all the players waiting for the number to be announced by the dealer
				
				gameData.lock1.notifyAll();
				
				// STEP-8: wait using lock1 of GameData while the players haven't checked 
				// the numbers 								
				// wait while the players haven't checked the numbers
				
				while(!gameData.playerChanceFlag[0] || !gameData.playerChanceFlag[1]) {
					try {
						//Dealer is waiting for both the players to finish searching the 
						//announced number
						gameData.lock1.wait(); 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}				
			}
			
			
			// STEP-9: Specify Condition to Check if Player1 has won
			
			if(gameData.playerSuccessFlag[0] && !gameData.playerSuccessFlag[1]) { 
				System.out.println("The winner is: Player-1");				
			} 
			// STEP-10: Specify Condition to Check if Player1 has won
			
			else if(gameData.playerSuccessFlag[1] && !gameData.playerSuccessFlag[0]){ 
				System.out.println("The winner is: Player-2");				
			} 
			// STEP-11: Specify Condition to Check if Player1 has won
			
			else if(gameData.playerSuccessFlag[0] && gameData.playerSuccessFlag[1]) {
				System.out.println("Both Player-1 and Player-2 are winners");				
			}
			else if(!gameData.playerSuccessFlag[0] && !gameData.playerSuccessFlag[1]) {
				System.out.println("The winner is: No one");				
			}
			Context context = new Context();
			StopState stopState = new StopState();
			context.setState(stopState);
			context.doAction();
			
			gameData.gameCompleteFlag = true; // Set the complete flag to true 
			
			gameData.lock1.notifyAll(); // If at all any player is waiting		
			
		}		
	}
}
