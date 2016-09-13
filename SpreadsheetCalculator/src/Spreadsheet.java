import java.io.InputStream;
import java.util.Scanner;

public class Spreadsheet {
	int row = 0;
	int col = 0;
	Cell[][] sheetCells = null;
	
	public Spreadsheet(){
		
	}
	
	public void processCells(){
		
	}
	
	public void evaluate(){
		
	}
	
	public void printResults(){
		
	}
	
	public void readInput(){
		InputStream inputStream = System.in;
		Scanner scanner = new Scanner(inputStream);
		try{
			// space delimited
			scanner.useDelimiter("\\s+");
			// read the first line for size of the spreadsheet
			String[] rowCol = scanner.nextLine().split(" ");
			if(rowCol.length != 2){
				throw new IllegalArgumentException("Invalid Input for Spreadsheet Size!");
			}
			//read the first line for the size of the spreadsheet
			row = Integer.parseInt(rowCol[0]);
			col = Integer.parseInt(rowCol[1]);
			sheetCells = new Cell[row][col];
			// read the cells in 
			int rowIdx = 0; 
			int colIdx = 0;
			int cellCount = 0;
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				if(line.length() == 0){
					break;
				}
				sheetCells[rowIdx][colIdx++] = new Cell(line);
				cellCount++;
				if(cellCount == col){
					colIdx = 0;
					rowIdx++;
				}
			}
			if(cellCount != row*col){
				throw new IllegalArgumentException("Wrong number of input for the spread sheet");
			}	
		}catch(Exception e){
			System.err.println("ERROR READING INPUTS!");
			System.exit(-1);
		}
		finally{
			scanner.close();
		}
	}
	
	public static void main(String[] args){
		Spreadsheet sheet = new Spreadsheet();
		sheet.readInput();
		sheet.evaluate();
		sheet.printResults();
	}
}

class Cell{
	double value;
	boolean isEvaluated;
	String cellContent;

	public Cell(String contents){
		this.cellContent = contents;
		this.isEvaluated = false;
	}
	
}
