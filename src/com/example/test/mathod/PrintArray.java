package com.example.test.mathod;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.test.R;

public class PrintArray extends Activity{

	private int [][]p={ {1,12,11,10},
						{2,13,16,9 },
						{3,14,15,8 },
						{4,5, 6, 7 } };  
	TextView mPrint;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.print_arry);
		mPrint=(TextView) this.findViewById(R.id.print_info);
		
		ArrayPrinter ap= new ArrayPrinter(4,4,p);
		
		mPrint.setText("\n"+ap.printArray()+ap.printEastern()+ap.printClockWise());
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	class ArrayPrinter{
		int rowCount,colCount;
		final int ROW,COLUMN;
		final int[][] Array;
		int col=0,row=0;
		StringBuilder sb = new StringBuilder();
		
		ArrayPrinter(int col,int row,int[][] array){
			ROW = col;
			COLUMN = row;
			Array = array;
		}
		
		void printUp(){
			for(int i=0;i<rowCount;i++){
				row--;
				sb.append( Array[row][col]+" ");
			}
			colCount--;
		
		}
		void printDown(){
		
			for(int i=0;i<rowCount;i++){
				row++;
				sb.append( Array[row][col]+" ");
			}
			colCount--;
			

		}
		void printLeft(){
			
			for(int i=0;i<colCount;i++){
				col--;
				sb.append( Array[row][col]+" ");
			}
			rowCount--;
			
		}
		
		void printRight(){
			
			for(int i=0;i<colCount;i++){
				col++;
				sb.append( Array[row][col]+" ");
			}
			rowCount--;
		}
		
		
		
		String printEastern(){
			col=0;
			row=-1;
			rowCount= ROW;
			colCount= COLUMN;
			sb.delete(0, sb.length());
			
			while(rowCount>0 && colCount>0){
				printDown();
				printRight();
				printUp();
				printLeft();
			}
			sb.append("\n");
			return sb.toString();	
		}

		String printClockWise(){
			col=-1;
			row=0;
			rowCount= ROW;
			colCount= COLUMN;
			sb.delete(0, sb.length());
			
			while(rowCount>0 && colCount>0){
				printRight();
				printDown();
				printLeft();
				printUp();				
			}
			sb.append("\n");
			return sb.toString();	
		}
		
		String printArray(){
			col=0;
			row=0;
			sb.delete(0, sb.length());
			 
			for(row = 0;row<ROW;row++){
				for(col = 0;col < COLUMN;col++){
					sb.append(String.format("%2d ",Array[row][col]));
					
				}
				sb.append("\n");
			}   
			
			sb.append("\n");
			return sb.toString();	
		}		
		
	}
	

}
