package de.witchcafe.wator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class Creature {

	private static final double CARRION_SEEK_THRESHOLD = 1.5;

	protected static final Random RANDOM = new Random();

	protected Biotop biotop;
	protected WaTor wator;
	protected Species species;
	protected Double energy;
	protected Double mass;
	protected boolean alive = true;
	protected int lastDx;
	protected int lastDy;

	public Creature(WaTor wator, Species species) {
		this(wator, species, species.startEnergy(), species.startMass());
	}

	private Creature(WaTor wator, Species species, double startEnergy, double startMass) {
		this.wator = wator;
		this.species = species;
		this.energy = startEnergy;
		this.mass = startMass;
		int[] initialDirection = randomDirection();
		this.lastDx = initialDirection[0];
		this.lastDy = initialDirection[1];
	}

	private static int[] randomDirection() {
		int dx;
		int dy;
		do {
			dx = RANDOM.nextInt(3) - 1;
			dy = RANDOM.nextInt(3) - 1;
		} while (dx == 0 && dy == 0);
		return new int[] { dx, dy };
	}

	public Species getSpecies() {
		return species;
	}

	public double getEnergy() {
		return energy;
	}

	public Biotop getBiotop() {
		return biotop;
	}

	public void setBiotop(Biotop biotop) {
		this.biotop = biotop;
	}

	public boolean isAlive() {
		return alive;
	}

	public final void move() {
		for (int step = 0; step < species.speed() && alive; step++) {
			singleStep();
		}
	}

	private void singleStep() {
		energy -= species.metabolism();
		if (species.grazes()) {
			energy += biotop.takeEnergy();
		}

		Biotop target = decideTarget();
		if (target != null) {
			Creature occupant = target.getCreature();
			Biotop previous = biotop;
			energy -= wator.toroidalDistance(biotop, target) * mass;
			if (occupant != null && isPrey(occupant)) {
				occupant.die();
				energy += species.eatEnergyGain();
			}
			biotop.setCreature(null);
			target.setCreature(this);
			lastDx = wator.directionDx(previous, target);
			lastDy = wator.directionDy(previous, target);
		}

		if (energy <= 0) {
			die();
			return;
		}
		adjustMass();
		reproduceIfReady();
	}

	private Biotop decideTarget() {
		List<Biotop> freeNeighbours = getFreeMooreNeighbours();
		Biotop prey = findNearestVisible(this::isPrey);
		if (prey != null && getMooreNeighbours().contains(prey)) {
			return prey;
		}
		if (freeNeighbours.isEmpty()) {
			return null;
		}
		if (prey != null) {
			return pickClosestTo(freeNeighbours, prey);
		}
		Biotop threat = findNearestVisible(this::isThreat);
		if (threat != null) {
			return pickFarthestFrom(freeNeighbours, threat);
		}
		if (species.eatsCarrion()) {
			Biotop carrion = findRichestVisibleBiotop();
			if (carrion != null && carrion.getEnergy() > biotop.getEnergy() * CARRION_SEEK_THRESHOLD) {
				return pickClosestTo(freeNeighbours, carrion);
			}
		}
		return freeNeighbours.get(RANDOM.nextInt(freeNeighbours.size()));
	}

	private boolean isPrey(Creature other) {
		return species.preyNames().contains(other.species.name());
	}

	private boolean isThreat(Creature other) {
		return species.threatNames().contains(other.species.name());
	}

	private void adjustMass() {
		if (energy > 10) {
			mass += 0.1;
			energy -= 1;
		}
		if (energy < 5) {
			mass -= 0.1;
			energy += 0.9;
		}
	}

	private void reproduceIfReady() {
		if (mass < species.reproduceMass()) {
			return;
		}
		List<Biotop> nursery = getFreeMooreNeighbours();
		if (nursery.isEmpty()) {
			return;
		}
		Biotop spot = nursery.get(RANDOM.nextInt(nursery.size()));
		double childMass = mass / 2;
		double childEnergy = energy / 2;
		mass -= childMass;
		energy -= childEnergy;

		Creature child = new Creature(wator, species, childEnergy, childMass);
		spot.setCreature(child);
		wator.addCreature(child);
	}

	private void die() {
		alive = false;
		if (energy > 0) {
			biotop.addEnergy(energy);
		}
		biotop.setCreature(null);
		wator.removeCreature(this);
	}

	private List<Biotop> getMooreNeighbours() {
		List<Biotop> neighbours = new ArrayList<>();
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				if (dx != 0 || dy != 0) {
					neighbours.add(wator.biotopAt(biotop.x + dx, biotop.y + dy));
				}
			}
		}
		return neighbours;
	}

	private List<Biotop> getFreeMooreNeighbours() {
		List<Biotop> free = new ArrayList<>();
		for (Biotop candidate : getMooreNeighbours()) {
			if (candidate.getCreature() == null) {
				free.add(candidate);
			}
		}
		return free;
	}

	private Biotop findNearestVisible(Predicate<Creature> matcher) {
		Biotop nearest = null;
		int nearestDistance = Integer.MAX_VALUE;
		int radius = species.sightRadius();
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				if (dx == 0 && dy == 0) {
					continue;
				}
				if (!isWithinFieldOfView(dx, dy)) {
					continue;
				}
				Biotop candidate = wator.biotopAt(biotop.x + dx, biotop.y + dy);
				Creature occupant = candidate.getCreature();
				if (occupant != null && matcher.test(occupant)) {
					int distance = Math.max(Math.abs(dx), Math.abs(dy));
					if (distance < nearestDistance) {
						nearestDistance = distance;
						nearest = candidate;
					}
				}
			}
		}
		return nearest;
	}

	private boolean isWithinFieldOfView(int dx, int dy) {
		int fieldOfView = species.fieldOfViewDegrees();
		if (fieldOfView >= 360) {
			return true;
		}
		if (lastDx == 0 && lastDy == 0) {
			return true;
		}
		double dot = dx * lastDx + dy * lastDy;
		double magnitudeCandidate = Math.sqrt(dx * dx + dy * dy);
		double magnitudeHeading = Math.sqrt(lastDx * lastDx + lastDy * lastDy);
		double cosAngle = dot / (magnitudeCandidate * magnitudeHeading);
		cosAngle = Math.max(-1, Math.min(1, cosAngle));
		double angle = Math.toDegrees(Math.acos(cosAngle));
		return angle <= fieldOfView / 2.0;
	}

	private Biotop findRichestVisibleBiotop() {
		Biotop richest = null;
		double richestEnergy = Double.MIN_VALUE;
		int radius = species.sightRadius();
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				if (dx == 0 && dy == 0) {
					continue;
				}
				if (!isWithinFieldOfView(dx, dy)) {
					continue;
				}
				Biotop candidate = wator.biotopAt(biotop.x + dx, biotop.y + dy);
				if (candidate.getEnergy() > richestEnergy) {
					richestEnergy = candidate.getEnergy();
					richest = candidate;
				}
			}
		}
		return richest;
	}

	private Biotop pickClosestTo(List<Biotop> candidates, Biotop reference) {
		return pickBest(candidates, reference, true);
	}

	private Biotop pickFarthestFrom(List<Biotop> candidates, Biotop reference) {
		return pickBest(candidates, reference, false);
	}

	private Biotop pickBest(List<Biotop> candidates, Biotop reference, boolean minimize) {
		Biotop best = null;
		double bestScore = minimize ? Double.MAX_VALUE : Double.MIN_VALUE;
		for (Biotop candidate : candidates) {
			double score = wator.toroidalDistance(candidate, reference);
			if ((minimize && score < bestScore) || (!minimize && score > bestScore)) {
				bestScore = score;
				best = candidate;
			}
		}
		return best;
	}
}
