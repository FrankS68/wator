package de.witchcafe.wator;

public class Biotop {

	Integer x, y;
	Creature creature;
	Double energy;
	private final double grazeFraction;

	public Biotop(Integer indexx, Integer indexy, double startEnergy, double grazeFraction) {
		x = indexx;
		y = indexy;
		energy = startEnergy;
		this.grazeFraction = grazeFraction;
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

	public void addEnergy(double amount) {
		energy += amount;
	}

	public Double takeEnergy() {
		return takeEnergy(1);
	}

	public Double takeEnergy(Integer factor) {
		factor = Math.min(factor, 10);
		double fraction = Math.min(1.0, grazeFraction * factor);
		Double takenEnergy = energy * fraction;
		energy -= takenEnergy;
		return energy;
	}
}
