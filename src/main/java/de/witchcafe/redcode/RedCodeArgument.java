package de.witchcafe.redcode;

public class RedCodeArgument{
	protected static String operators = "#$*@{}<>";
	protected Character operator;
	protected Integer value;
	
	public RedCodeArgument(String input) {
		operator = input.charAt(0);
		if (operators.indexOf(operator) > -1) {				
			value = Integer.valueOf(input.substring(1));
		}
		else {
			operator = '$';
			value = Integer.valueOf(input);
		}
	}
	
	private RedCodeArgument(RedCodeArgument rca) {
		this.operator = rca.operator;
		this.value = rca.value;
	}

	public RedCodeArgument clone() {
		return new RedCodeArgument(this);
	}

	public Integer calculate(Integer cursor,Integer range, RedCodeInstruction[] redCodeMemory) {
		if (operator.equals('$')) {
			return (cursor + value) % range;
		}
		if (operator.equals('@')) {
			Integer relativeCursor = (cursor + value) % range;
			return redCodeMemory[relativeCursor].argumentB.value + cursor;
		}
		if (operator.equals('#')) {
			return cursor;
		}
		throw new RuntimeException("operator not implemented");
		// return Integer.MIN_VALUE;
	}
	
	public String toString() {
		return operator+value.toString();
	}
}