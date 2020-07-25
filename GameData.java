public class GameData {
	public int announcedNumber = 0;	
	public boolean gameCompleteFlag = false;	
	public boolean noAnnouncedFlag = false;
	public boolean[] playerSuccessFlag = new boolean[2];
	public boolean[] playerChanceFlag = new boolean[2];
	
	public Object lock1 = new Object();
}
