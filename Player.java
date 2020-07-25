import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
public class Player implements Runnable {

	private int id;							// player id [0 or 1]
	private GameData gameData;				// shared object
	private int totalNumbersFound;    		// total numbers found
	private final static int MAXNO = 10;	// maximum numbers on player ticket
	private int[] ticket = new int[MAXNO];  // stores the numbers on the player ticket
	ArrayList<Integer> checkNumbers = new ArrayList<Integer>();
	
	public Player(GameData gameData, int id) { 
		
		this.id = id; 		
		this.gameData = gameData;	
		this.totalNumbersFound = 0;
		
		// randomly generate ten numbers and store them in the lstTicket
		for(int i = 0; i < MAXNO; i++) {
			int p = randInt(i*5 + 1, (i+1) * 5);
			ticket[i] = p;
		}
		System.out.println("Ticket Numbers of Player:" + (id+1) +" "+Arrays.toString(ticket));}
	
	
	private static int randInt(int min, int max) {	//method to generate random numbers
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;}
	
	public void run() {
		/* STEP-12: write code to take a lock on gameData using lock1 */ 
		// take a lock on the instance of SharedData using lock1
		
		synchronized(gameData.lock1) {			
			
			/* STEP-13: Specify condition */
			// both players execute while the game is not complete
			
			while(!gameData.gameCompleteFlag) {
			
				// STEP-14: both players should wait using lock1 of GameData until a number 
				// is announced by the dealer or its not the chance of the player  
								  
				while(!gameData.noAnnouncedFlag || gameData.playerChanceFlag[id]) {
					try {
						gameData.lock1.wait();}
					catch (InterruptedException e) {
						e.printStackTrace();}}
				
				// its important to check this condition again because it is possible that
				// one player may have found all the numbers when the other was waiting
				if(!gameData.gameCompleteFlag) {					
					
					// STEP-15: Check if the announced number is on the player's ticket
					// if the number is found, the player increments the totalNumbersFound
										
					for(int i = 0; i < MAXNO; i++) {						
						if(gameData.announcedNumber == ticket[i]) {
							if(!checkNumbers.contains(gameData.announcedNumber)) {
								
									checkNumbers.add(gameData.announcedNumber);
									this.totalNumbersFound++;							
									break;
								}
							}
							
							
						}
					}
					
					// STEP-16: player checks if it has won the game i.e., it has found all numbers
					// then it should report success
					
					if(this.totalNumbersFound == 3) {
						// player set the success flag 
						gameData.playerSuccessFlag[this.id] = true;						
					}
					
					// player sets its chance flag 
					gameData.playerChanceFlag[id] = true;
					
					// STEP-17: notify all others waiting on lock1 of GameData
					
					gameData.lock1.notifyAll();
				}}}}
