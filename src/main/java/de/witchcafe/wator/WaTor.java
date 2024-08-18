package de.witchcafe.wator;

import java.util.LinkedList;
import java.util.Random;

public class WaTor {
	protected Biotop[][] biotopArray;
	protected LinkedList<Creature> creatures;
	
	public WaTor(Integer x,Integer y,Integer creatureCount) {
		initBiotop(x, y);
		initCreatures(creatureCount);
	}
	
	protected void initBiotop(Integer x,Integer y) {
		biotopArray = new Biotop[x][y];
		for (Integer indexx = 0;indexx < x;indexx++) {
			for (Integer indexy = 0;indexy < y;indexy++) {
				biotopArray[indexx][indexy] = new Biotop(indexx,indexy);
			}
		}
	}
	
	protected void initCreatures(Integer countCreatures) {
		creatures = new LinkedList<Creature>();
		for (int index = 0;index < countCreatures;index++) {
			Creature creature = new Creature(this);
			getRandomAvailablePosition().setCreature(creature);
			creatures.add(creature);
		}
	}
	
	protected void fertilise() {
		for (Integer indexx = 0;indexx < biotopArray.length;indexx++) {
			for (Integer indexy = 0;indexy <  biotopArray[0].length;indexy++) {
				biotopArray[indexx][indexy].energy += Double.valueOf(0.1);
			}
		}
	}
	
	public void waTorMove() {
		allCreaturesMove();
		fertilise();
	}
	
	public void allCreaturesMove() {
		int count = creatures.size();
		for (int index = 0;index < count;index ++) {
			singleCreatureMove();
		}
	}
	
	public void singleCreatureMove() {
		Creature creature = creatures.pollLast();
		creature.move();
		creatures.push(creature);
	}

	private Biotop getRandomAvailablePosition() {
		Biotop b = null;
		Random random = new Random();
		do{
			Integer x = random.nextInt(this.biotopArray.length);
			Integer y = random.nextInt(this.biotopArray[0].length);
			b = biotopArray[x][y];
		} while (b.getCreature() != null);
		return b;
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		for (Integer indexx = 0;indexx < biotopArray.length;indexx++) {
			for (Integer indexy = 0;indexy < biotopArray[0].length;indexy++) {
				output.append(biotopArray[indexx][indexy].getCreature() == null ? "_" : "C");
			}
			output.append("\n");
		}
		return output.toString();
	}
}
