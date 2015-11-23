import java.util.HashMap;
import java.util.Map;
/**
* The pipeline should support the following MIPS instructions:
* OPCODE   VALUE
    NOP     0  [Example: NOP                  :  Do nothing]
    ADD     1  [Example: ADD R1 R2 R3         :  R1 = R2 + R3]
    SUB     2  [Example: SUB R1 R2 R3         :  R1 = R2 - R3]
    MUL     3  [Example: MUL R1 R2 R3         :  R1 = R2 * R3]
    DIV     4  [Example: DIV R1 R2 R3         :  R1 = R2 / R3]
    XOR     5  [Example: XOR R1 R2 R3         :  R1 = R2 ^ R3]
    AND     6  [Example: ADD R1 R2 R3      				break:   :  R1 = R2 & R3]
    OR      7  [Example: OR R1 R2 R3          :  R1 = R2 | R3]
    ADDI    8  [Example: ADDI R1 R2 100       :  R1 = R2 + 100]
    SUBI    9  [Example: SUBI R1 R2 100       :  R1 = R2 - 100]
    LD      10 [Example: LD R1 R2 32          :  R1 = DataMem[R2 + 32]]
    ST      11 [Example: ST R1 R2 32          :  DataMem[R2 + 32] = R1]
    BEQZ    12 [Example: BEQZ R1 100          :  if R1==0 goto instruction at address PC_of_Branch + 4 + 100 * 4]
    BNEQZ   13 [Example: BNEQZ R1 100         :  if R1!=0 goto instruction at address PC_of_Branch + 4 + 100 * 4]
    HLT     50 [Example: HLT                  :  Terminate program]
*/
public class preprocess {
	String sInstruction;
	int Instruction=0x0000;
	String tokens[];

	public int getDecimalValue(){
		
		return Instruction;
	}
	public void printBinaryValue(){
		System.out.println(Integer.toBinaryString(Instruction));
		
	}
	public void printFormattedInstruction(){
		int operand = ( Instruction & 0B11111100000000000000000000000000 )  >>26 ;
		int op1 = (Instruction & 0B00000011111000000000000000000000 ) >> 21;
		int op2 = (Instruction & 0B00000000000111110000000000000000 ) >> 16;
		int op3 = (Instruction & 0B00000000000000001111100000000000 ) >> 11;
		int imm = (Instruction & 0B00000000000000000000011111111111 );
		System.out.println("|"+Integer.toBinaryString(operand)+"|"+Integer.toBinaryString(op1)+"|"+Integer.toBinaryString(op2)+"|"+Integer.toBinaryString(op3)+"|"+imm);
		
	}
	public preprocess(String S) {
		// TODO Auto-generated constructor stub
		this.sInstruction = S;
		this.tokenize();
		this.process();
	}
	public void setValue(String S)
	{
		this.sInstruction = S;
		this.tokenize();
		this.process();

	}
	
	private void tokenize(){
	//	String delims = "[ .,?!]+";
		String delims = "[ ,]";
		tokens = sInstruction.split(delims);
		
		
	}
	
	public void process(){
		/*
		 * Dictionary for Operands
		 * */
		Map<String,Integer> operands = new HashMap<String,Integer>();
		operands.put("NOP", 0);
		operands.put("ADD", 1);
		operands.put("SUB", 2);
		operands.put("MUL", 3);
		operands.put("DIV", 4);
		operands.put("XOR", 5);
		operands.put("AND", 6);
		operands.put("OR", 7);
		operands.put("ADDI", 8);
		operands.put("SUBI", 9);
		operands.put("LD", 10);
		operands.put("ST", 11);
		operands.put("BEQZ", 12);
		operands.put("BNEQZ", 13);
		operands.put("HLD", 50);

		/*
		 * Dictionary for Operators
		 * */
		Map<String,Integer> operators = new HashMap<String,Integer>();
		operators.put("R0", 0);
		operators.put("R1", 1);
		operators.put("R2", 2);
		operators.put("R3", 3);
		operators.put("R4", 4);
		operators.put("R5", 5);
		operators.put("R6", 6);
		operators.put("R7", 7);
		
		int Op1 = 0x0000;
		int Op2 = 0x0000;
		int Op3 = 0x0000;
		int Imm = 0x0000;
		
		
		int Operand = operands.get(tokens[0]);
		switch(Operand)
		{
		case 0:
			break;
		/* Operations with 3 operands*/	
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			Op2 = Op2 | operators.get(tokens[2]);
			Op2 = Op2<<21;
			Op3 = Op3 | operators.get(tokens[3]);
			Op3 = Op3<<16;
			Op1 = operators.get(tokens[1]);
			Op1 = Op1<<11;
			
			
			Operand = (Operand)<<26;
			Instruction = Operand|Op1|Op2|Op3;
			break;
		/*Operations with 2 operator or Immediate only*/	
		case 8:
		case 9:
		case 10:
		case 11:
			
			Op2 = Op2 | operators.get(tokens[2]);
			Op2 = Op2<<21;
			Op1 = operators.get(tokens[1]);
			Op1 = Op1<<16;
			Imm = Imm| Integer.parseInt(tokens[3]);
			Operand = (Operand)<<26;
			Instruction = Operand|Op1|Op2|Imm;
			break;
		case 12:
		case 13:
			Op1 = operators.get(tokens[1]);
			System.out.println(tokens[1]+"<=>"+tokens[2]);
			Op1 = Op1<<21;
			Imm = Integer.parseInt(tokens[2]);
			Operand = (Operand)<<26;
			Instruction = Operand|Op1|Imm;
			System.out.println(Instruction+"% "+Op1+"% "+Imm);
			break;
		case 50:
			break;
			
		
		
		}


		
		
		
	}
	

}

/*
 * static String zeroes(int length) {
    return (length <= 0) ? ""
      : String.format("%0" + length + "d", 0);
}
//...

int num = 8675309;

// convert to binary
String s = Integer.toBinaryString(num);
System.out.println(s);
// prints "100001000101111111101101"

// fill in leading zeroes
s = zeroes(Integer.SIZE - s.length()) + s;
System.out.println(s);
// prints "00000000100001000101111111101101"

s = s.replaceAll("(?!$)(?<=\\G.{4})", " ");
System.out.println("[" + s + "]");
// prints "[0000 0000 1000 0100 0101 1111 1110 1101]"
 * 
 * */
