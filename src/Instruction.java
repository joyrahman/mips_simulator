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

public class Instruction {
	int operator;
	int rs;
	int rt;
	int rd;
	int immediate;
	boolean nopFlag=false;
	String stringInstruction;
	public Instruction()
	{
		operator = 0;
		this.rs =0;
		this.rt = 0;
		this.rd = 0;
		this.immediate = 0;
	}
	public Instruction(String s)
	{
	String delims = "[ ,]";
	String tokens[];
	tokens = s.split(delims);
	this.stringInstruction = s;
	
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
	operands.put("HLT", 50);

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
	
	this.operator = operands.get(tokens[0]);
	switch(this.operator)
	{
	case 0:
		this.nopFlag = true;
		this.rd = 0;
		this.rs = 0;
		this.rt = 0;
		
		
		
		break;
	/* Operations with 3 operands*/	
	case 1:
	case 2:
	case 3:
	case 4:
	case 5:
	case 6:
	case 7:
		this.rd = operators.get(tokens[1]);
		this.rs = operators.get(tokens[2]);
		this.rt = operators.get(tokens[3]);
		break;
	/*Operations with 2 operator or Immediate only*/	
	case 8:
	case 9:
	case 10:
	case 11:
		this.rt = operators.get(tokens[1]);
		this.rs = operators.get(tokens[2]);
		this.immediate = Integer.parseInt(tokens[3]);
		break;
	case 12:
	case 13:
		this.rs = operators.get(tokens[1]);
		this.immediate = Integer.parseInt(tokens[2]);
		break;
	case 50:
		this.rd = 0;
		this.rs = 0;
		this.rt = 0;
		break;
		
	
	
		}
	}
	public void print(){
		//System.out.println("Operator  RS RT RD IMM");
		System.out.print(this.operator+" "+this.rs+" "+this.rt+" "+this.rd +" "+this.immediate);
	}
	
	public String toString()
	{
		//return (this.operator+" "+this.rs+" "+this.rt+" "+this.rd +" "+this.immediate);
		return "# "+this.stringInstruction+" ";
	}

}