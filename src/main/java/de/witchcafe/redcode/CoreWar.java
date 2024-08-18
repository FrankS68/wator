package de.witchcafe.redcode;

import java.util.LinkedList;

public class CoreWar {
	LinkedList<CoreWarProgram> programs = new LinkedList<CoreWarProgram>();
	RedCodeInstruction[] redCodeMemory;
	
	public CoreWar() {
		
	}
	
	public void addProgram(CoreWarProgram program) {
		programs.push(program);
		redCodeMemory = null;
	}
	
	public void setup() {
		Integer distance = 10;
		redCodeMemory = new RedCodeInstruction[programs.size() * distance];
		for (int index = 0;index < programs.size()  ; index++) {
			programs.get(index).setup(redCodeMemory,index * distance);
		}
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		for (RedCodeInstruction slot : redCodeMemory) {
			output.append(slot == null ? "." : slot.footprint == null ? "_" : slot.footprint);
		}
		return output.toString();
	}

	public void run(int steps) {
		if (redCodeMemory == null) setup();
		for (int index = 0;index < steps;index++) {
			CoreWarProgram program = programs.pollLast();
			program.runInstruction();
			programs.push(program);
			System.out.println(this);
		}
	}
}
