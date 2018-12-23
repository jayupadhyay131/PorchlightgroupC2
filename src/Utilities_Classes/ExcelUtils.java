package Utilities_Classes;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;

import org.apache.poi.xssf.usermodel.XSSFRow;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

	import java.io.FileInputStream;
	import java.io.FileOutputStream;

	import org.apache.poi.ss.usermodel.Cell;
	import org.apache.poi.ss.usermodel.DataFormatter;
	import org.apache.poi.xssf.usermodel.XSSFCell;
	import org.apache.poi.xssf.usermodel.XSSFRow;
	import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;

	public class ExcelUtils 
	{
		
		public static XSSFSheet ExcelWSheet;
		public static XSSFWorkbook ExcelWBook;
		public static XSSFCell Cell;
		public static XSSFRow Row;

		//This method is to set the File path and to open the Excel file, Pass Excel Path and SheetName as Arguments to this method
		
		public static void setExcelFile(String Path, String SheetName) throws Exception {
			
			try{
				
				//Open the Excel file
				
				FileInputStream ExcelFile = new FileInputStream(Path);
				
				//Access the required test data sheet
				
				ExcelWBook = new XSSFWorkbook(ExcelFile);
				ExcelWSheet = ExcelWBook.getSheet(SheetName);
			}
			catch(Exception e){
			
				throw(e);
			}
		}
		
		//This method is to read the test data from the Excel cell, in this we are passing parameters as Row number and Column number
		
		public static String getCellData(int RowNum, int ColNum) throws Exception {
			try{
				
				//System.out.println(RowNum + "  " +ColNum);
				
				DataFormatter formatter = new DataFormatter();
				Cell cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
				String CellData = formatter.formatCellValue(cell);
				//System.out.println(CellData);
				return CellData;
				
			}
			catch(Exception e){
				
				throw (e);
			}
		}
				
		public static String getCellData(int RowNum, String ColumnName) throws Exception {
			try{
				
				//System.out.println(RowNum + "  " +ColNum);
				
				DataFormatter formatter = new DataFormatter();
				Cell cell = null;
				int noofclomn = ExcelWSheet.getRow(0).getLastCellNum();
				
				for(int col=0; col<noofclomn;col++)
				{
					cell = ExcelWSheet.getRow(0).getCell(col);
					
					if(cell.toString().equalsIgnoreCase(ColumnName))
					{
						cell = ExcelWSheet.getRow(RowNum).getCell(col);
						break;
					}
				}
//				System.out.println("colums Count = "+noofclomn);
				String CellData = formatter.formatCellValue(cell);
				//System.out.println(CellData);
				return CellData;
				
			}
			catch(Exception e){
				
				throw (e);
			}
		}
		
		//This method is to write in the Excel cell, Row number and Column number are the parameters
		
		public static void setCellData(String RegisterID, int RowNum, int ColNum) throws Exception{
			
			try{
				
				Row = ExcelWSheet.getRow(RowNum);
				Cell = Row.getCell(ColNum, org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);
				
				if (Cell== null){
					
					Cell = Row.createCell(ColNum);
					Cell.setCellValue(RegisterID);
				}
				else{
					
					Cell.setCellValue(RegisterID);
				}
				
				FileOutputStream fileOut = new FileOutputStream(TestData.Path_TestData + TestData.File_TestData);
				ExcelWBook.write(fileOut);
				fileOut.flush();
				fileOut.close();
				
			}
			catch (Exception e){
				
				throw (e);
			}
		}
	}



