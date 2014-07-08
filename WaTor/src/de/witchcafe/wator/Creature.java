package de.witchcafe.wator;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Creature implements Runnable{

	private static HashMap<String, Creature> creatureByPosition = new HashMap<String, Creature>();
	
	protected static Random random = new Random();
	
	private boolean shark = false;
	protected String name;
	
	private int x;
	private int y;
	private int z;
	private double energy = 1;
	private double mass = 1;
	private double distance = 1;
	private int steps = 0;
	private Date lastStep = new Date();

	public Creature(String n){
		this(n,random.nextInt(20),random.nextInt(20),random.nextInt(20));
	}
	
	protected Creature(String n,int x,int y, int z){
		name = n;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	protected void move(){
		int dx = random.nextInt(5) - 2;
		int dy = random.nextInt(5) - 2;
		int dz = random.nextInt(5) - 2;
		int sum = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
		double diff = Math.pow(sum / 8.0, 1.0 / 3);
		distance += diff;
		double energyAmount = diff * mass / 10;
		energy -= energyAmount;
		mass += energyAmount / 10;
		x += dx; 
		y += dy; 
		z += dz; 
	}
	
	public String toString(){
		return String.format("(%s-%s:%sx,%sy,%sz)\n\tmass\t%s\n\tenergy\t%s\n\tdistance\t%s:%s", getClass().getSimpleName(),name,x,y,z,mass,energy,steps,distance);
	}
	
	@Override
	public void run() {
		while(energy > 0.1){
			move();
			try {
				Thread.sleep((random.nextInt(4) + 1) * 100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			steps ++;
			System.out.printf("%s\t%s\n",new Date(),this);
		}
	}
	
}
