package de.witchcafe.wator;

import java.util.ArrayList;
import java.util.List;

public class Biotop {
	private List<Creature> creatures = new ArrayList<Creature>();
	private Place[][] places;
	
	public Biotop(int x,int y,int c) {
		places = new Place[x][y];
		for (int index = 0;index < c;index++) {
			
		}
	}
}
