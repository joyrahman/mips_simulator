import java.io.PrintWriter;


public class PipeLine {
	private Instruction[] IMEM = new Instruction[1024];
	private int DMEM[] = new int[1024];
	//private int Registers[] = new int[8];	
	//private register Registers[] = new register[8]; 
	private register[] Registers = new register[8];
	private int PC=0;
	//private int NPC=0;
	boolean IF_LOCK=false;  //free
	boolean ID_LOCK=false;
	boolean EX_LOCK=false;
	boolean MEM_LOCK=false;
	boolean WB_LOCK=false;
	boolean HLTFlag = false;
	boolean stallFlag = false;
	boolean branchFlag = false;
	public Latches IF_ID ;
	public Latches ID_EX ;
	public Latches EX_MEM ;
	public Latches MEM_WB;
	Dstack depstack;
	int max_stall = 0;
	int branch_stall = 0;
	boolean turnofStallFlag = false;
	boolean turnOfBranch = true;
	boolean computeDataHazard = false;
	boolean computeBranchHazard = false;
	boolean HazardEncountered = false;
	boolean delayLock1 = false;
	boolean delayLock2 = false;
	int counterDataHazard =0;
	int counterBranchHazard = 0;
	// Counter For Stall and Cycle
	int counterTotalCycles = 0;
	int counterInstructionsFetched = 0;
	int counterInstructionsFinished = 0;
	int counterTotalStallCycles = 0;
	boolean branchObserved = false;


	public PipeLine(Instruction IMEM[]){
		this.IMEM = IMEM;		
		IF_ID = new Latches();
		ID_EX = new Latches();
		EX_MEM = new Latches();
		MEM_WB = new Latches();
		depstack = new Dstack();
		for (int i =0;i<8;i++)
		{
			Registers[i] =new register(0);
			//System.out.println(Registers[i].dirty);
		}

	}
	
	

	private void IF(){
		IF_LOCK = true;
		counterInstructionsFetched++;
		//depstack.insert(IMEM[PC]);
		/*if(branchObserved!=true)
		{
			PC=NPC;
		}
		*/
		System.out.print(IMEM[PC].toString());
		//IMEM[PC].print();
		IF_ID.operator = IMEM[PC].operator; 
		IF_ID.rs = IMEM[PC].rs;
		IF_ID.rt = IMEM[PC].rt;
		IF_ID.rd = IMEM[PC].rd;
		IF_ID.Imm = IMEM[PC].immediate;
		IF_ID.NPC = PC+1;
/*
		if(branchObserved!=true)
		{
			PC++;
		}
*/
		PC++;
		//PC++;
		//set IF_ID latch non empty, because IF put value in IF_ID latch, also copy everything in the latch that you need for the next stage
 		//System.out.println(PC+"_"+Registers[6]);	
		IF_ID.setEmpty(false);
		IF_LOCK =  false;
		
		//System.out.print("| IF |");
		
	}
	private void ID(){
		String debugState = "-";
        // set IF_ID latch empty
        //IF_ID.setEmpty(true);
        ID_LOCK = true; 
        
        /*
         * SPECIAL AREA FOR DEBUG
         * */
        /*
        System.out.println("[Debug Data]");
        System.out.print(IF_ID.operator);
        System.out.print(IF_ID.rs);
        System.out.print(IF_ID.rt);
        System.out.print(IF_ID.rd);
        System.out.print(IF_ID.Imm);

        */
                
        /* Things that has to be done*/
        if (IF_ID.operator==1|IF_ID.operator==2|IF_ID.operator==3|IF_ID.operator==4|IF_ID.operator==5|IF_ID.operator==6|IF_ID.operator==7)
        {

            IF_ID.A = Registers[IF_ID.rs].value;
            IF_ID.B = Registers[IF_ID.rt].value;
            
           
            
            if((Registers[IF_ID.rs].dirty>0) || Registers[IF_ID.rt].dirty>0)
            {
            	HazardEncountered = true;
            }
            
            /*
            if(IF_ID.rd==IF_ID.rs ||IF_ID.rd==IF_ID.rt)
            {
            	delayLock1 = true;
            }
            */
            /*
             * SPECIAL AREA FOR DEBUG
             * */
            /*
            System.out.println("[Dirty Data]");
            System.out.print(Registers[IF_ID.rs].dirty);
            System.out.print(Registers[IF_ID.rt].dirty);
            System.out.print(Registers[IF_ID.rd].dirty);
            */
            /*
            if(!delayLock1)
            {	Registers[IF_ID.rd].dirty ++;
            
            }
            */
            


        }
        //CASE : 3 Operator Operands Involving Immediate
        if (IF_ID.operator== 8||IF_ID.operator==  9 || IF_ID.operator== 10){
            int x = IF_ID.rs; 
            int y = IF_ID.rt;
        	IF_ID.A = Registers[x].value;
            IF_ID.B = Registers[y].value;
 
            //  System.out.println("A="+IF_ID.A+"B="+IF_ID.B+"Imm="+IF_ID.Imm);
            
            if(Registers[IF_ID.rs].dirty>0)
            {
            	HazardEncountered = true;
            }
            /*
            if(IF_ID.rt==IF_ID.rs)
            {
            	delayLock2 = true;
            }
            else
            {
            	Registers[IF_ID.rt].dirty ++;
                
            }
            */
            
            
            
            /*
             * SPECIAL AREA FOR DEBUG
             * */
            /*
            System.out.println("[Dirty Data]");
            System.out.print(Registers[IF_ID.rs].dirty);
            System.out.print(Registers[IF_ID.rt].dirty);
            System.out.print(Registers[IF_ID.rd].dirty);
            */

        }
        
        if (IF_ID.operator==  11){
            IF_ID.A = Registers[IF_ID.rs].value;
            IF_ID.B = Registers[IF_ID.rt].value;
            //  System.out.println("A="+IF_ID.A+"B="+IF_ID.B+"Imm="+IF_ID.Imm);
            if((Registers[IF_ID.rs].dirty>0) || Registers[IF_ID.rt].dirty>0)
            {
            	HazardEncountered = true;
            }
            
            
            
            

        }
        
        if (IF_ID.operator== 12||IF_ID.operator==  13){
        IF_ID.A  = Registers[IF_ID.rs].value;
        	if(Registers[IF_ID.rs].dirty>0)
        	{
        	HazardEncountered = true;
        	}
        //computeBranchHazard = true;
        }
        //for hlt case
        if(IF_ID.operator == 50)
        {
        	HLTFlag = true;
            IF_LOCK = true;
            ID_LOCK = true;
            
        }
        

        
        ID_EX.copy(IF_ID);  
        
   /*
        if(computeDataHazard==false)
        {
            // need to run the below just only once.
            counterDataHazard = depstack.computeDependency()-1;
            //need to run the below as many times as the value comes
            if(counterDataHazard>0)
                {
                computeDataHazard = true;
                }

        }


        if(counterDataHazard>0)
        {
                counterTotalStallCycles++;
                System.out.print("  S  ");
                stallIFDueToData();
                counterDataHazard--;
                //check for dirty
               
                	

        }
        */
        
        if(HazardEncountered == true )
        {
        	 counterTotalStallCycles++;
             System.out.print("  D  ");
        	 //debugState = " S ";
             stallIFDueToData();
             HazardEncountered = false;
             //counterDataHazard--;
        }
        
        else if (HLTFlag==true)
        {
            ID_EX.setEmpty(false);
            ID_LOCK = true;
        	
        }
    

        else 
            {
                if(IF_ID.operator==1|IF_ID.operator==2|IF_ID.operator==3|IF_ID.operator==4|IF_ID.operator==5|IF_ID.operator==6|IF_ID.operator==7)
                {
                	Registers[IF_ID.rd].dirty ++;
                	//delayLock1 = false;
                }
                if(IF_ID.operator== 8||IF_ID.operator==  9 || IF_ID.operator== 10)
                {
                	Registers[IF_ID.rt].dirty ++;
                	//delayLock2 = false;
                }
  
                computeDataHazard = false;
                unsetStallIFDueToData();

                //Leave the stage
                ID_LOCK = false;
				if(IF_ID.operator==12||IF_ID.operator==13)
				{
					//counterBranchHazard = 2;
					//do something
					IF_LOCK = true;
					ID_LOCK = true;
					
					branchFlag = true;
					//if(branchFlag==true)
			        	 //debugState = "D  B";

					
				}
				
		       /* if(IF_ID.operator == 50)
		        {
		        	HLTFlag = true;
		            IF_LOCK = true;
		            ID_LOCK = true;
		            
		        }
		        */
				
//				/*
//				if (IF_ID.operator==1|IF_ID.operator==2|IF_ID.operator==3|IF_ID.operator==4|IF_ID.operator==5|IF_ID.operator==6|IF_ID.operator==7)
//				{
//			        Registers[IF_ID.rd].dirty = true;
//			        /*
//		             * SPECIAL AREA FOR DEBUG
//		             * 
//		            System.out.println("[Dirty Data2]");
//		            System.out.print(IF_ID.rs);
//		            System.out.print(IF_ID.rt);
//		            System.out.print(IF_ID.rd);
//		            System.out.print(Registers[IF_ID.rs].dirty);
//		            System.out.print(Registers[IF_ID.rt].dirty);
//		            System.out.print(Registers[IF_ID.rd].dirty);
//				}
//				
//				if (IF_ID.operator== 8||IF_ID.operator==  9 || IF_ID.operator== 10)
//				{
//			        Registers[IF_ID.rt].dirty = true;
//			        
//		             * SPECIAL AREA FOR DEBUG
//		             * 
//		            System.out.println("[Dirty Data2]");
//		            System.out.print(Registers[IF_ID.rs].dirty);
//		            System.out.print(Registers[IF_ID.rt].dirty);
//		            System.out.print(Registers[IF_ID.rd].dirty);
//				}
//				*/*/
		       // HazardEncountered = false;
	        	// if(debugState.equals("-")==true)
	        		 //debugState = "D";
                System.out.print(" D ");
        

            }


    }	
	
	
	private void EX()
	{
		/*
		 * 	NOP     0  
			ADD     1  
			SUB     2  
			MUL     3  
			DIV     4  
			XOR     5  
			AND     6  
			OR      7  
			ADDI    8  
			SUBI    9  
			LD      10 
			ST      11 
			BEQZ    12 
			BNEQZ   13 
			HLT     50
		 * */
		
		EX_LOCK = true;
		if (ID_EX.operator==1|ID_EX.operator==2| ID_EX.operator==3| ID_EX.operator==4| ID_EX.operator==5|ID_EX.operator==6| ID_EX.operator==7)
		{

			switch(ID_EX.operator)
			{
			case 1: 
				ID_EX.ALUOutput = ID_EX.A + ID_EX.B;
				break;
			case 2:
				ID_EX.ALUOutput = ID_EX.A - ID_EX.B;
				break;
			case 3: 
				ID_EX.ALUOutput = ID_EX.A * ID_EX.B;
				break;
			case 4:
				ID_EX.ALUOutput =  ID_EX.A/ID_EX.B;
				break;
			case 5:
				ID_EX.ALUOutput = ID_EX.A^ID_EX.B;
				break;
			case 6:
				ID_EX.ALUOutput = ID_EX.A & ID_EX.B;
				break;
			case 7:
				ID_EX.ALUOutput = ID_EX.A | ID_EX.B;
				break;				 


			}

		}
		if (ID_EX.operator== 8|| ID_EX.operator==  9)
		{

			switch(ID_EX.operator)
			{
			case 8: 
				ID_EX.ALUOutput = ID_EX.A + ID_EX.Imm;
				break;
			case 9:
				ID_EX.ALUOutput = ID_EX.A - ID_EX.Imm;
				break;
			}

		}
		if (ID_EX.operator== 10|| ID_EX.operator==  11)
		{
			ID_EX.ALUOutput = ID_EX.A + ID_EX.Imm;
			
		}
		if (ID_EX.operator== 12 || ID_EX.operator == 13)
		{
			counterTotalStallCycles +=2;
			ID_EX.condition = false;
			//System.out.print("B");
			/*Stall other cycles*/
			ID_LOCK = true;
			IF_LOCK = true;
			switch(ID_EX.operator)
			{
			case 12:
				if(ID_EX.A==Registers[0].value)
					{
					ID_EX.condition = true;
					ID_EX.ALUOutput = ID_EX.NPC + ID_EX.Imm;
					}
		
				else
				{
					ID_EX.condition = false;
				}
				break;
		
			case 13:
				//System.out.println("case 13");
				if(ID_EX.A!=Registers[0].value)
				{
			
				ID_EX.condition = true;
				ID_EX.ALUOutput = ID_EX.NPC + ID_EX.Imm;
				//System.out.println(ID_EX.condition+" "+ID_EX.A);
				}

				else
				{
					ID_EX.condition = false;
					//System.out.println(ID_EX.condition+" ami nai "+ID_EX.A);
				}
				break;
			
			}
			
			
	}
		
		EX_MEM.copy(ID_EX);		
		EX_MEM.setEmpty(false);
		ID_EX.setEmpty(true);		
		EX_LOCK = false;
		
	
	}

	private void MEM(){
		/*
		 * Nothing to Do for Operation 1~9
		 * 
		 * */
		// set EX_MEM latch empty
		MEM_LOCK = true;
		//PC = NPC;
	
		if (EX_MEM.operator== 10|| EX_MEM.operator==  11)
		{
			switch(EX_MEM.operator)
			{
			case 10:
				EX_MEM.LMD = DMEM[EX_MEM.ALUOutput];
			break;
			case 11:
			DMEM[EX_MEM.ALUOutput] = EX_MEM.B;	
			}
			
		}
		if (EX_MEM.operator== 12|| EX_MEM.operator==  13)
		{
			/*release the stalling*/
			branchFlag = false;
			//System.out.print("B");
			IF_LOCK = false;
			ID_LOCK = false;
			//System.out.println(EX_MEM.condition);
			if(EX_MEM.condition==true)
			{
				PC = EX_MEM.ALUOutput;
				branchObserved = true;
			}
			
			
		}

		MEM_WB.copy(EX_MEM);		
		MEM_WB.setEmpty(false);
		EX_MEM.setEmpty(true);		
		MEM_LOCK = false;

		
		
	}

	private void WB(){
		// set MEM_WB latch empty
		WB_LOCK = true;
		counterInstructionsFinished++;
		if (MEM_WB.operator==1| MEM_WB.operator==2| MEM_WB.operator==3| MEM_WB.operator==4| MEM_WB.operator==5| MEM_WB.operator==6| MEM_WB.operator==7)
		{
			Registers[MEM_WB.rd].value=MEM_WB.ALUOutput;
			if(Registers[MEM_WB.rd].dirty>0)
			{
			Registers[MEM_WB.rd].dirty --;
			}
		}

		if (MEM_WB.operator== 8| MEM_WB.operator==  9)
		{
			Registers[MEM_WB.rt].value=MEM_WB.ALUOutput;
			if(Registers[MEM_WB.rt].dirty >0)
				{
				Registers[MEM_WB.rt].dirty --;
						
				}

		}
		if(MEM_WB.operator==10)
		{	
			Registers[MEM_WB.rt].value=MEM_WB.LMD;
			if(Registers[MEM_WB.rt].dirty >0)
			{
			Registers[MEM_WB.rt].dirty --;
					
			}
		}
		MEM_WB.setEmpty(true);
		WB_LOCK = false;
		
		
		//System.out.print("| WB |");
				
	}



	public int Controller(){

		int counterHlt = 0;
		boolean terminateSignal = false;
		PC = 0;
		for (int i = 1; ((i <= 1000)&&(terminateSignal!=true)); i++) //cycle number is i
		{
			System.out.print(i+":");
			counterTotalCycles++;
			if ((WB_LOCK == false) && (MEM_WB.isEmpty()==false) ) 
			{
					System.out.print(" W ");
					WB();
					
			}
			
			if ((MEM_LOCK == false) && (EX_MEM.isEmpty()==false))
			{
				System.out.print(" M ");
				MEM();
				
			}
			
			if ((EX_LOCK == false) && (ID_EX.isEmpty()==false) )
			{
				//if ID_EX latch is non empty
					System.out.print(" X ");
					EX();
				
			}
			
			if ((ID_LOCK == false) && (IF_ID.isEmpty()==false))
			{
					ID();
				
			}
			
			if ((IF_LOCK == false) && (HLTFlag!=true))
			{
				System.out.print(" F ");
				IF();				
			}
			
			System.out.println();
		
			if(HLTFlag==true)
			{
				
				IF_LOCK = true;
				IF_ID.setEmpty(true);
				if(counterHlt==3)
					{terminateSignal = true;
					counterTotalStallCycles +=4;
					
					}
				//counterTotalStallCycles ++;	
				counterHlt++;
				
			}
			
			
		
		}

		return 0;
	}

	

	public void printRegisters()
	{
		for (int i=0;i<Registers.length;i++)
		{

			System.out.println("Registers["+i+"] :" + Registers[i].value);
		}
		//System.out.println("PC="+PC+" NPC="+IF_ID.NPC);


	}

	public void printIMEM(){
		for(int i=0;i<IMEM.length;i++)
		{
			System.out.println(IMEM[i]);
		}

	}	
	public void printDMEM(){
		for(int i=0;i<DMEM.length;i++)
		{
			System.out.println(DMEM[i]);
		}

	}	
	
	public void initDmem(){
		
		for (int i=0;i<DMEM.length;i++)
		{
			//DMEM[i] = (int) (Math.random() * 50 + 1); 
			DMEM[i] = i;		
		}
	}
	
	public void printStatisticsFile(PrintWriter out){
		//System.out.println("Test:"+InstructionCounter+" "+nopCounter);
		out.println(counterInstructionsFetched);
		out.println(counterInstructionsFinished);
		out.println(counterTotalCycles);
		out.println(counterTotalStallCycles);
		for(int i =1;i<8;i++)
			{
			out.println(Registers[i].value);
			}
		
	}
	public void printStatisticsConsole(){
		//System.out.println("Test:"+InstructionCounter+" "+nopCounter);
		System.out.println(counterInstructionsFetched);
		System.out.println(counterInstructionsFinished);
		System.out.println(counterTotalCycles);
		System.out.println(counterTotalStallCycles);
		for(int i =1;i<8;i++)
			{
			System.out.println(Registers[i].value);
			}
		
	}



public void setStallIFDueToBranch()
{
                IF_LOCK = true;


}
public void unsetStallIFDueToBranch()
{
                IF_LOCK = false;


}


public void stallIFDueToData(){
                ID_EX.setEmpty(true);
                IF_LOCK = true;
                ID_LOCK = false;
                IF_ID.setEmpty(false);
    
}

public void unsetStallIFDueToData(){
                ID_EX.setEmpty(false);
                IF_LOCK = false;
                IF_ID.setEmpty(true);
    
}

}
