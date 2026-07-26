package de.witchcafe.wator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WaTor {
	protected Biotop[][] biotopArray;
	protected List<Creature> creatures;
	protected List<Species> speciesOrder;
	protected BiotopSettings biotopSettings;

	public WaTor(Integer width, Integer height, Map<Species, Integer> population, BiotopSettings biotopSettings) {
		this.biotopSettings = biotopSettings;
		initBiotop(width, height);
		this.speciesOrder = new ArrayList<Species>(population.keySet());
		initCreatures(population);
	}

	protected void initBiotop(Integer x, Integer y) {
		biotopArray = new Biotop[x][y];
		for (Integer indexx = 0; indexx < x; indexx++) {
			for (Integer indexy = 0; indexy < y; indexy++) {
				biotopArray[indexx][indexy] = new Biotop(indexx, indexy, biotopSettings.startEnergy(),
						biotopSettings.grazeFraction());
			}
		}
	}

	protected void initCreatures(Map<Species, Integer> population) {
		creatures = new ArrayList<Creature>();
		for (Map.Entry<Species, Integer> entry : population.entrySet()) {
			for (int index = 0; index < entry.getValue(); index++) {
				addAtRandomPosition(new Creature(this, entry.getKey()));
			}
		}
	}

	private void addAtRandomPosition(Creature creature) {
		getRandomAvailablePosition().setCreature(creature);
		creatures.add(creature);
	}

	public void addCreature(Creature creature) {
		creatures.add(creature);
	}

	public void removeCreature(Creature creature) {
		creatures.remove(creature);
	}

	public List<Species> getSpeciesOrder() {
		return speciesOrder;
	}

	public List<Creature> getCreatures() {
		return creatures;
	}

	protected void fertilise() {
		for (Integer indexx = 0; indexx < biotopArray.length; indexx++) {
			for (Integer indexy = 0; indexy < biotopArray[0].length; indexy++) {
				biotopArray[indexx][indexy].energy += biotopSettings.fertiliseRate();
			}
		}
	}

	public void waTorMove() {
		allCreaturesMove();
		fertilise();
	}

	public void allCreaturesMove() {
		List<Creature> snapshot = new ArrayList<Creature>(creatures);
		Collections.shuffle(snapshot);
		for (Creature creature : snapshot) {
			if (creature.isAlive()) {
				creature.move();
			}
		}
	}

	public Biotop biotopAt(int x, int y) {
		int width = biotopArray.length;
		int height = biotopArray[0].length;
		return biotopArray[((x % width) + width) % width][((y % height) + height) % height];
	}

	public double toroidalDistance(Biotop a, Biotop b) {
		int width = biotopArray.length;
		int height = biotopArray[0].length;
		int dx = Math.abs(a.x - b.x);
		dx = Math.min(dx, width - dx);
		int dy = Math.abs(a.y - b.y);
		dy = Math.min(dy, height - dy);
		return Math.sqrt(dx * dx + dy * dy);
	}

	public int directionDx(Biotop from, Biotop to) {
		return wrapDelta(from.x, to.x, biotopArray.length);
	}

	public int directionDy(Biotop from, Biotop to) {
		return wrapDelta(from.y, to.y, biotopArray[0].length);
	}

	private int wrapDelta(int from, int to, int size) {
		int delta = to - from;
		if (delta > size / 2) {
			delta -= size;
		}
		if (delta < -size / 2) {
			delta += size;
		}
		return delta;
	}

	private Biotop getRandomAvailablePosition() {
		Biotop b;
		Random random = new Random();
		do {
			Integer x = random.nextInt(biotopArray.length);
			Integer y = random.nextInt(biotopArray[0].length);
			b = biotopArray[x][y];
		} while (b.getCreature() != null);
		return b;
	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		for (Integer indexx = 0; indexx < biotopArray.length; indexx++) {
			for (Integer indexy = 0; indexy < biotopArray[0].length; indexy++) {
				Creature creature = biotopArray[indexx][indexy].getCreature();
				output.append(creature == null ? "_" : creature.getSpecies().name().charAt(0));
			}
			output.append("\n");
		}
		return output.toString();
	}
}
