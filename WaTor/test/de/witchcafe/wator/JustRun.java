package de.witchcafe.wator;

import static org.junit.Assert.*;

import org.junit.Test;

public class JustRun {

	@Test
	public void seedCreatures() {
		int seeds = 2;
		Thread[] thread = new Thread[seeds];
		for (int index = 0;index < seeds;index++){
			thread[index] = new Thread(new Creature("tier_"+index));
			thread[index].start();
		}
		try {
			thread[seeds - 1].join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("huston we've got a problem");
		}
	}

}
