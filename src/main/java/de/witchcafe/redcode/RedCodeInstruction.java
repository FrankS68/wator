package de.witchcafe.redcode;

import java.util.HashMap;
import java.util.LinkedList;

public class RedCodeInstruction {
	public enum Instruction {DAT,MOV,ADD,SUB,MUL,DIV,MOD,SPL,JMP,JMZ,DJZ,CMP};
	protected Character footprint;
	protected Instruction instruction;
	protected String redcode;
	protected RedCodeArgument argumentA,argumentB;
	
	public RedCodeInstruction(String input) {
		String[] token = input.split(" ");
		instruction = Instruction.valueOf(token[0]);
		argumentA = new RedCodeArgument(token[1]);
		argumentB = token.length == 3 ?  new RedCodeArgument(token[2]) : null;
		redcode = input;
	}
	
	public String toString() {
		return footprint + ":" + instruction + argumentA + (argumentB != null ? " " + argumentB : "");
	}
	
	protected RedCodeInstruction(RedCodeInstruction rcs) {
		this.footprint = rcs.footprint;
		this.instruction = rcs.instruction;
		this.argumentA = rcs.argumentA.clone();
		this.argumentB = rcs.argumentB.clone();
		this.redcode = rcs.redcode;
	}

	public RedCodeInstruction clone() {
		return new RedCodeInstruction(this);
	}

	static HashMap<Integer, LinkedList<String>> slotLogs = new HashMap<Integer, LinkedList<String>>();
	public static void coreDump(CoreWarProgram program,Integer cursor,RedCodeInstruction[] redCodeMemory) {
		int index = 0;
		for (RedCodeInstruction instruction : redCodeMemory) {
			LinkedList<String> slotLog = slotLogs.get(index);
			if (slotLog == null) {
				slotLog = new LinkedList<String>();
				slotLogs.put(index, slotLog );
			}
			slotLog.push((cursor == index++ ? program.sign + "-->" : "") + "\t"  + instruction + "\t");
			if (slotLog.size() > 10) {
				slotLog.removeLast();
			}
			slotLog.forEach(slotLogMessage->System.out.print(slotLogMessage));
			System.out.println();
		}
	}
	
	public static void runOn(CoreWarProgram program,RedCodeInstruction[] redCodeMemory) {
		Integer cursor = program.cursors.poll();
		if (cursor == null) {
			System.out.println("Program died "+program.sign);
		}
		cursor = cursor % redCodeMemory.length;
		RedCodeInstruction instruction = redCodeMemory[cursor];
		// System.out.println(cursor + "\t" + instruction);
		coreDump(program, cursor, redCodeMemory);
		instruction.footprint = program.sign;
		if (instruction.instruction.equals(Instruction.MOV)) {
			Integer fromCursor = instruction.argumentA.calculate(cursor,redCodeMemory.length,redCodeMemory);
			Integer toCursor = instruction.argumentB.calculate(cursor,redCodeMemory.length,redCodeMemory);
			redCodeMemory[toCursor] = redCodeMemory[fromCursor].clone(); 
			program.cursors.push(cursor + 1);
		}
		if (instruction.instruction.equals(Instruction.JMP)) {
			Integer fromCursor = instruction.argumentA.calculate(cursor,redCodeMemory.length,redCodeMemory);
			program.cursors.push(fromCursor);
		}
		if (instruction.instruction.equals(Instruction.ADD)) {
			Integer fromCursor = instruction.argumentA.calculate(cursor,redCodeMemory.length,redCodeMemory);
			Integer toCursor = instruction.argumentB.calculate(cursor,redCodeMemory.length,redCodeMemory);
			redCodeMemory[toCursor].argumentB.value += redCodeMemory[fromCursor].argumentA.value; 
			program.cursors.push(cursor + 1);
		}
		if (instruction.instruction.equals(Instruction.DAT)) {
			System.out.println("ProgramThread died "+program.sign);
		}
	}
}
