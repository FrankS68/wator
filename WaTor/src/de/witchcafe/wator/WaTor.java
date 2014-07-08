package de.witchcafe.wator;

public class WaTor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int index = 0;index < 10;index++){
			new Thread(new Creature("tier_"+index)).start();
		}
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
