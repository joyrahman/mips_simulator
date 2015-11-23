
public class Dstack {
	private Instruction istack[] = new Instruction[4];
	private int dependency[] = {0,0,0,0};
	
	public void insert(Instruction x)
	{
		istack[0] = istack[1];
		istack[1] = istack[2];
		istack[2] = istack[3];
		istack[3] = x;
		
		
		
	}
	
	public Dstack(){
		for(int i =0;i<4;i++)
		{
			istack[i]= new Instruction();
			
		}
		
		
	}
	
	public int computeDependency(){
		for(int i=1;i<4;i++)
		{
			dependency[i] = 0;
		
		}
		
		checkDependency(istack[3], istack[2], 1);
		checkDependency(istack[3], istack[1], 2);
		checkDependency(istack[3], istack[0], 3);
		return findMaxStall();
	
	}
	
	int findMaxStall()
	{
		int max_stall = 0;
		for(int i=1;i<4;i++)
		{
			if(max_stall<dependency[i])
			{
				max_stall = dependency[i];
			}
		}
		return max_stall;
		
	}
	
	public void checkDependency(Instruction a, Instruction b, int level)
	{
		switch(a.operator)
		{
		
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			//i and i_1
			// FOR R1,R2,R3 Type i and R1,R2,R3 Type i_1
			if(b.operator>=1 && b.operator<=7)
			{
				if((a.rs==b.rd)||(a.rt==b.rd))
				{
					dependency[level] = 4-level;
				}
			
			}
			// FOR R1,R2,R3 Type i and R1,R2,100 Type i_1
			if(b.operator>=8 && b.operator<=10)
			{
			
				if((a.rs==b.rt)||(a.rt==b.rt))
				{
					dependency[level] = 4-level;
				}
			}
			
			
			break;
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:	
			if(b.operator>=1 && b.operator<=7)
			{
				if(a.rs==b.rd)
				{
					dependency[level] = 4-level;
				}
			
			}
			// FOR R1,R2,R3 Type i and R1,R2,100 Type i_1
			if(b.operator>=8 && b.operator<=10)
			{
			
				if(a.rs==b.rt)
				{
					dependency[level] = 4-level;
				}
			}
			
			
			
		}//end of switch
		
		
	}
	
	

}
