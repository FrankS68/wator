package de.witchcafe.wator;

import java.util.ArrayList;
import java.util.Random;

public class Creature {

	protected Biotop biotop;
	protected WaTor wator;
	protected Double energy;
	protected Double mass;

	public Creature(WaTor w) {
		wator = w;
		energy = Double.valueOf(10.0);
		mass = Double.valueOf(1.0);
	}
	
	public Biotop getBiotop() {
		return biotop;
	}

	public void setBiotop(Biotop biotop) {
		this.biotop = biotop;
	}
	
	protected ArrayList<Biotop> getAvailableTargets() {
		ArrayList<Biotop> availableTargets = new ArrayList<Biotop>();
		for (Integer x = biotop.x -1;x < biotop.x + 2;x++) {
			for (Integer y = biotop.y -1;y < biotop.y + 2;y++) {
				if (x != 0 || y != 0) {
					Biotop b = wator.biotopArray
							[(x + wator.biotopArray.length) % wator.biotopArray.length]
							[(y + wator.biotopArray[0].length) % wator.biotopArray[0].length];
					if (b.getCreature() == null) {
						availableTargets.add(b);
					}
				}
			}
		}
		return availableTargets;
	}
	
	public void move() {
		Random random = new Random();
		energy += biotop.takeEnergy();
		ArrayList<Biotop> availableTargets = getAvailableTargets();
		Integer selectIndex = random.nextInt(availableTargets.size());
		Biotop target = availableTargets.get(selectIndex);
		double distance = biotop.distance(target);
		energy -= distance * mass;
		biotop.setCreature(null);
		target.setCreature(this);
		
		if (energy > 10) {
			mass += .1;
			energy -= 1;
		}
		if (energy < 5)  {
			mass -= .1;
			energy += .9;
		}
		
		System.out.println(String.format("[%d;%d] -> [%d;%d](e:%s,m:%s)",biotop.x,biotop.y,target.x,target.y,energy,mass));
	}

}
