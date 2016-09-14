import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * author Jian Xiang (jx5c@virginia.edu)
 * title: coding test: Spreadsheet calculator
 * Sample run: 
 * 		java Spreadsheet
 */
public class Spreadsheet {
	//sizes of the spreadsheet
	int row = 0;
	int col = 0;
	Cell[][] sheetCells = null;
	
	public Spreadsheet(){
	}
	
	/**
	 * evaluate every cell;
	 */
	public void evaluate(){
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				evaluateCell(sheetCells[i][j], new HashSet<Cell>());
			}
		}
	}
	/**
	 * Utility function used to judge if a string corresponds to a number 
	 * @param term: a string for testing
	 * @return
	 */
	private boolean isNumber(String term) {
		 try {
		    Double.parseDouble(term);
		    return true;
		 }catch (NumberFormatException e) {
		    return false;
		 }
	}
	
	/**
	 * locate the cell object given the location string, e.g. A3
	 * @param cellLoc location in string format
	 * @return
	 */
	private Cell getCellByPos(String cellLoc){
		try {
			int rowIdx = cellLoc.charAt(0)-'A';
			int colIdx = Integer.parseInt(cellLoc.substring(1))-1;
			return sheetCells[rowIdx][colIdx];
		}catch (NumberFormatException e) {
			System.err.println("Wrong format of the cell: " + cellLoc);
			System.exit(-1);
		}
		return null;
		
	}
	
	/**
	 * This function evaluates a cell. It uses a stack to do the evaluation post order expression; 
	 * NOTE: if the evaluation depends on another cell, a recursive call on this function is used to evaluate the new cell, and then push back to the top of the current stack;  
	 * @param cell: the cell being evaluated
	 * @param evaluatingCells: the cells that are being evaluated in the queue; NOTES: if a cell to be evaluated is in this set, then a circular dependency exists, an error should be reported 
	 */
	private double evaluateCell(Cell cell, Set<Cell> evaluatingCells){
		//if already evaluated, then return the value
		if(cell.isEvaluated){
			return cell.value;
		}
		//if the cell already in the set of cells being evaluated, a cyclic dependency exists 
		if(evaluatingCells.contains(cell)){
			System.err.println("CYCLIC DEPENDENCY EXISTS!"); 
			System.err.println("When evaluating Cell: "+ cell.cellContent);
			System.exit(-1);
		}
		
		//evaluation with stack;
		evaluatingCells.add(cell);
		Stack<Double> operands = new Stack<Double>();
		String[] terms = cell.cellContent.split(" ");
		for(String term : terms){
			if(term.equals("+")){
				operands.push(operands.pop()+operands.pop());
			}else if(term.equals("/")){
				double divisor = operands.pop();
				operands.push(operands.pop()/divisor);
			}else if(term.equals("*")){
				operands.push(operands.pop()*operands.pop());
			}else if(term.equals("/")){
				double subtractor = operands.pop();
				operands.push(operands.pop() - subtractor);
			}else if(isNumber(term)){
				operands.push(Double.parseDouble(term));	
			}else{
				//if this cell has dependency, evaluate with recursive call 
				Cell newCell = getCellByPos(term);
				operands.push(evaluateCell(newCell, evaluatingCells));
			}
		}
		cell.isEvaluated = true;
		cell.value = operands.pop();
		return cell.value;
	}
	
	/**
	 * print the results according to the format requirements
	 */
	public void printResults(){
		System.out.println(col +" "+ row);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.printf("%.5f%n", sheetCells[i][j].value);	
			}
		}
	}
	
	/**
	 * read all the inputs from std:in; conduct basic error checking
	 */
	public void readInput(){
		Scanner scanner = null;
		try{
			InputStream inputStream = System.in;
			scanner = new Scanner(inputStream);
			// space delimited
			scanner.useDelimiter("\\s+");
			// read the first line for size of the spreadsheet
			String[] rowCol = scanner.nextLine().split(" ");
			if(rowCol.length != 2){
				throw new IllegalArgumentException("Invalid Input for Spreadsheet Size!");
			}
			//read the first line for the size of the spreadsheet
			col = Integer.parseInt(rowCol[0]);
			row = Integer.parseInt(rowCol[1]);
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
				if(colIdx == col){
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
		//read the inputs
		sheet.readInput();
		//evaluate all the cells
		sheet.evaluate();
		//print the results;
		sheet.printResults();
	}
}

class Cell{
	//evaluated value
	double value;
	//if has been evaluated
	boolean isEvaluated;
	//the origina contents
	String cellContent;

	public Cell(String contents){
		this.cellContent = contents;
		this.isEvaluated = false;
	}
	
}
