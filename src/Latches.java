
public class Latches {
	public int NPC=0;
	public int ALUOutput=0;
	public int rs=0;
	public int rt=0;
	public int rd=0;
	public int Imm=0;
	public int operator=0;
	public int LMD=0;
	//public int PC=0;
	public int A=0;
	public int B=0;
//	public Instruction IR = new Instruction();
	boolean condition = false;
	private boolean empty = true;
	//define a variable like boolean empty = true; // at first all latches are empty, so only IF stage will execute
	
	public Latches()
	{
		//this.PC=0;
	}
	public boolean isEmpty(){
		return this.empty;
		
		
	}
	public void setEmpty(boolean Value){
		this.empty = Value;
		
		if(this.empty)
		{
			
			this.A = 0;
			this.B = 0;
			this.ALUOutput = 0;
			
			this.rs = 0;
			this.rt = 0;
			this.rd = 0;
			this.Imm = 0;
			//this.operator = 0;
			
			this.LMD = 0;
			this.condition = false;
			this.NPC = 0;
		}
		
		
		
	}
	
	public void copy(Latches x){
		this.A = x.A;
		this.B = x.B;
		this.ALUOutput = x.ALUOutput;
		this.rs = x.rs;
		this.rt = x.rt;
		this.rd = x.rd;
		this.Imm = x.Imm;
		this.operator = x.operator;
		this.LMD = x.LMD;
		this.condition = x.condition;
		//this.empty = x.empty;
		this.NPC = x.NPC;
				
		
		
	}
	
	
	
	

}