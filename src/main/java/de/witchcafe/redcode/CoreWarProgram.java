package de.witchcafe.redcode;

import java.util.LinkedList;
import java.util.Random;

import de.witchcafe.redcode.RedCodeInstruction.Instruction;

public abstract class CoreWarProgram {
	RedCodeInstruction[] redCodeMemory;
	
	LinkedList<Integer> cursors = new LinkedList<Integer>();
	
	Character sign; 
	
	public CoreWarProgram(char sign) {
		this.sign = sign;
	}
	
	public final CoreWarProgram setup(RedCodeInstruction[] rcm) {
		return setup(rcm,new Random().nextInt(redCodeMemory.length));
	}
	
	public final CoreWarProgram setup(RedCodeInstruction[] rcm,Integer startpoint) {
		redCodeMemory = rcm;
		setupCode(startpoint);
		cursors.push(startpoint);
		return this;
	}

	protected abstract void setupCode(Integer startpoint);

	public void runInstruction() {
		RedCodeInstruction.runOn(this, redCodeMemory);
	}
}
