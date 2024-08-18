package de.witchcafe.redcode;

import org.junit.jupiter.api.Test;

class RedCodeTests {
	
	@Test
	void contextLoads() {
	}

	@Test
	void testSimplestProgram() {
		CoreWar coreWar = new CoreWar();
		CoreWarProgram a = new CoreWarProgram('A') {
			
			@Override
			protected void setupCode(Integer startpoint) {
				redCodeMemory[startpoint] = new RedCodeInstruction("MOV $0 $1");
				redCodeMemory[startpoint].footprint = sign;
			}
		};
		CoreWarProgram b = new CoreWarProgram('B') {
			
			@Override
			protected void setupCode(Integer startpoint) {
				redCodeMemory[startpoint] = new RedCodeInstruction("ADD #4 3");
				redCodeMemory[startpoint + 1] = new RedCodeInstruction("MOV 2 @2");
				redCodeMemory[startpoint + 2] = new RedCodeInstruction("JMP -2");
				redCodeMemory[startpoint + 3] = new RedCodeInstruction("DAT #0 #0");
				redCodeMemory[startpoint].footprint = sign;
				redCodeMemory[startpoint + 1].footprint = sign;
				redCodeMemory[startpoint + 2].footprint = sign;
				redCodeMemory[startpoint + 3].footprint = sign;
			}
		};
		coreWar.addProgram(a);
		coreWar.addProgram(b);
		coreWar.run(80);
	}
}
