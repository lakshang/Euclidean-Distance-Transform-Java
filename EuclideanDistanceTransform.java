import java.io.*;
import java.util.Scanner;
import java.lang.Math;

public class EuclideanDistanceTransform
{
	int numRows;
	int numCols;
	int minVal;
	int maxVal;
	int newMin;
	int newMax;
	double[][] zeroFramedAry;
	double[] neighborAry = new double[5];
	double sqrt_2 = Math.sqrt(2.0); 
	
	EuclideanDistanceTransform(Scanner inFile, FileWriter outFile1, FileWriter outFile2)
	{
		try
		{
	// read the image header
			numRows = inFile.nextInt();
			numCols = inFile.nextInt();
			minVal = inFile.nextInt();
			maxVal = inFile.nextInt();
					
	// dynamically allocate zeroFramedAry
			zeroFramedAry = new double[numRows + 2][numCols + 2];
			
	// load the input image onto zeroFramedAry
			this.loadImage(inFile);
	
	// zeroFrame the zeroFrameAry
			this.zeroFramed();
		
	// euclidean distance transform pass-1
			this.firstPassEuclideanDistance(outFile2);
	
	// euclidean distance transform pass-2
			this.secondPassEuclideanDistance(outFile2);			
	
	// find the newMin and newMax of pass-2 result	
			int data;
	
			for(int row = 1; row < numRows + 1; row++)
				for(int col = 1; col < numCols + 1; col++)
				{	
					data = (int) (zeroFramedAry[row][col] + 0.5);
			
					if(row == 1 && col == 1)
					{
						newMin = data;
						newMax = data;
					}
					else if(data < newMin)
						newMin = data;
					else if(data > newMax)
						newMax = data;
				}
	
	// print header for outFile1
			outFile1.write(numRows + " ");
			outFile1.write(numCols + " ");
			outFile1.write(newMin + " ");
			outFile1.write(newMax + " ");
	
			outFile1.write('\n');
		
	// print result of pass-2 to outFile1
			for(int row = 1; row < numRows + 1; row++)
			{
				for(int col = 1; col < numCols + 1; col++)
				{	
					data  = (int) (zeroFramedAry[row][col] + 0.5);
			
					outFile1.write(data + " ");	
				}
		
				outFile1.write('\n');
			}
	
			outFile1.write('\n');							
		}
		catch(IOException e)
		{
			System.out.println(e);
		}	
	}
	
	void loadImage(Scanner inFile)
	{
		for(int row = 1; row < numRows + 1; row++)
			for(int col = 1; col < numCols + 1; col++)
				zeroFramedAry[row][col] = inFile.nextInt();				
	}

	void zeroFramed()
	{
		for(int k = 0; k <= numRows + 1; k++)
		{
			zeroFramedAry[k][0] = 0;
			zeroFramedAry[k][numCols + 1] = 0;
		}
		
		for(int p = 0; p <= numCols + 1; p++)
		{
			zeroFramedAry[0][p] = 0;
			zeroFramedAry[numRows + 1][p] = 0;
		}
	}
	
	void firstPassEuclideanDistance(FileWriter outFile)
	{	
		try
		{
			double min;
	
			for(int row = 1; row < numRows + 1; row++)
				for(int col = 1; col < numCols + 1; col++)
					if(zeroFramedAry[row][col] != 0)
					{
						this.loadNeighbors(1, row, col);
						
						min = neighborAry[0];
					
						for(int index = 0; index < 4; index++)
							if(neighborAry[index] < min)
								min = neighborAry[index];
									
						zeroFramedAry[row][col] = min;	
					}
	
			outFile.write("Result after euclidean distance transform pass-1: \n");
		
			this.prettyPrintDistance(outFile);
		}
		catch(IOException e)
		{
			System.out.println(e);
		}	
	}
	
	void secondPassEuclideanDistance(FileWriter outFile)
	{	
		try {
			double min;
	
			for(int row = numRows; row > 0; row--)
				for(int col = numCols; col > 0; col--)
					if(zeroFramedAry[row][col] != 0)
					{
						this.loadNeighbors(2, row, col);
								
						min = neighborAry[0];
						
						for(int index = 0; index < 5; index++)
							if(neighborAry[index] < min)
								min = neighborAry[index];
						
						zeroFramedAry[row][col] = min;
					}
			
			outFile.write("Result after euclidean distance transform pass-2: \n");
	
			this.prettyPrintDistance(outFile);
		}
		catch(IOException e)
		{
			System.out.println(e);
		}	
	}	
	
	void loadNeighbors(int pass, int row, int col)
	{
		if(pass == 1)
		{
			neighborAry[0] = (zeroFramedAry[row][col - 1]) + 1.0;	     // left
			neighborAry[1] = (zeroFramedAry[row - 1][col - 1]) + sqrt_2; // top left
			neighborAry[2] = (zeroFramedAry[row - 1][col]) + 1.0;        // top center
			neighborAry[3] = (zeroFramedAry[row - 1][col + 1]) + sqrt_2; // top right
		}
		else if(pass == 2)
		{
			neighborAry[0] = (zeroFramedAry[row][col + 1]) + 1.0;  	     // right
			neighborAry[1] = (zeroFramedAry[row + 1][col + 1]) + sqrt_2; // bottom right
			neighborAry[2] = (zeroFramedAry[row + 1][col]) + 1.0;        // bottom center
			neighborAry[3] = (zeroFramedAry[row + 1][col - 1]) + sqrt_2; // bottom left
			neighborAry[4] = zeroFramedAry[row][col];	             // self
		}
	}
	
	void prettyPrintDistance(FileWriter outFile)
	{
		try 
		{
			int data;
	
			for(int row = 1; row < numRows + 1; row++)
			{
				for(int col = 1; col < numCols + 1; col++)
				{	
					if(zeroFramedAry[row][col] == 0)
						outFile.write("  ");
					else
					{
						data = (int) (zeroFramedAry[row][col] + 0.5);
						
						outFile.write(data + " ");
					}
				}
			
				outFile.write('\n');	
			}
		
			outFile.write('\n');
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			Scanner inFile = new Scanner(new FileReader(args[0]));
			FileWriter outFile1 = new FileWriter(args[1]);
			FileWriter outFile2 = new FileWriter(args[2]);
			
			EuclideanDistanceTransform euclideanDistanceTransform = new EuclideanDistanceTransform(inFile,
				outFile1, outFile2);
			
			inFile.close();
			outFile1.close();
			outFile2.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}	
	}	
}
