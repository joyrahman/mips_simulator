import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;



public class MipsInput {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 

	 * 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		int instructionCounter = 0;
		int nopCounter = 0;
		Instruction[] instructionBuffer = new Instruction[256];
		
		/*
		 * File Handling
		 * 
		 * */
		String infile = null;
		String outfile = null;
		if (args.length >= 2) //both files given via command line
	    {
	        infile = args[0];
	        if (fileExists(infile) == false)
	        {
	            System.out.println("File not found. Using input.prog as default");
	        	infile = "input.prog";
	        }
	        outfile = args[1];
	    }
	    else if (args.length == 1) //input file given via command line
	    {
	        infile = args[0];
	        System.out.println(" Output File not defined. Using console as default");
	        outfile = "System.out";
	    }
	    else //no files given on command line
	    {
	    	System.out.println("both input and output not defined. This program will read from input.prog and write to output.prog");
			infile = "input.prog";
			outfile = "output.prog";
	    }

		
		// Read Input From File
		//System.out.println(System.getProperty("user.dir"));
		//Scanner in = new Scanner(new FileReader(System.getProperty("user.dir")+"/bin/5.in"));
		Scanner in = new Scanner(new FileReader(infile));
		
		PrintWriter out = new PrintWriter(outfile, "UTF-8");
		
		while(in.hasNext())
		{
			String word = in.nextLine();
			System.out.println(word);
			instructionBuffer[instructionCounter] = new Instruction(word);
				instructionCounter++;

		}

	
		PipeLine p1 = new PipeLine(instructionBuffer);

		p1.Controller();
		p1.printStatisticsFile(out);
		p1.printStatisticsConsole();
		in.close();
		out.close();
		
		

	}
	
	
public static boolean fileExists(String n)
	{
	    return (new File(n)).exists();
	}	

}
