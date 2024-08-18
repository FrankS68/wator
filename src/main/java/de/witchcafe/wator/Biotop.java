package de.witchcafe.wator;

public class Biotop {

	Integer x,y;
	Creature creature;
	Double energy;
	
	public Biotop(Integer indexx, Integer indexy) {
		x = indexx;
		y = indexy;
		energy = (double) 1;
	}

	public void setCreature(Creature creature) {
		this.creature = creature;
		if (creature != null) creature.setBiotop(this);
	}

	public Creature getCreature() {
		return creature;
	}

	public Double getEnergy() {
		return energy;
	}

	public Double takeEnergy() {
		return takeEnergy(1);
	}

	public Double takeEnergy(Integer factor) {
		factor = Math.min(factor, 10);
		Double takenEnergy = energy / 10 * factor;
		energy -= takenEnergy;
		return energy;
	}

	public double distance(Biotop target) {
		// we use the taxicab distance here
		return Math.sqrt(Math.abs(x - target.x) + Math.abs(y - target.y));
	}
}
