package Utilities_Classes;
	
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

	public class ImportData_Platform
	{
		
//		static String Database = "platform_sanitydb";
		static String Database = "porchdb";
		static String XLSFilePath = System.getProperty("user.dir")+"\\TestData\\NG_Platform_TestData.xls";
//		static String XLSFilePath = "D:\\TestData.xls";
//		static String TableName = "platform_sanity_test_Data";
		static String TableName = "porchdb_test_Data";
		static String DBUserName = "root";
//		static String DBPassword = "test1234";
		static String DBPassword = "Test@123456"; 
		static String Databaseconnection = "localhost";
//		static String Databaseconnection = "192.168.1.105";
//		static String Databaseconnection = "ng-testing.cb04wcbjfatz.us-east-1.rds.amazonaws.com";
		static String DatabasePort = "3306";
		
		public static void main(String []args) throws Exception
		{
			String PrintQuery = "";
			try 
			{
				String Seperator, Encoding,  HeaderYesNo;
				Seperator = ",";
				Encoding = null;
				HeaderYesNo = "Yes";
				ConvertXLSIntoMySQL(XLSFilePath, Seperator, Encoding, TableName, Database, HeaderYesNo);
		
			}
			catch(Exception e)
			{
				System.out.println(" Problem in Import XLS cases into MySQL Table Reason : " + e.getLocalizedMessage() );
				System.out.println("********** Failed Query = "+ PrintQuery +" ******");
			}
		}
		public static void ConvertXLSIntoMySQL(String XLSFilePath,String Seperator,String Encoding,String TableName, String Database, String HeaderYesNo) throws IOException, SQLException
		{
			Connection ConnMySQL = null;
			String PrintQuery = "";
			try
			{
				ConnMySQL = OpenConnection(Database,DBUserName,DBPassword);
				String CSVFilePath = "C:\\Temp";
				if (!new File(CSVFilePath).exists())
				{
					new File(CSVFilePath).mkdir();
				}
				CSVFilePath = CSVFilePath + "\\Temp.CSV";
				if((Encoding==null)|| (!Encoding.equals("UnicodeBig")))
				{
					Encoding = "UTF8";
				}
				File XLSFile = new File(XLSFilePath);
								
				try {
					Workbook wb = Workbook.getWorkbook(XLSFile);
					Sheet S1 = wb.getSheet(0);//getting first sheet
					int colcount = S1.getColumns(); // getting number of columns
					int rowcount = S1.getRows();// getting number of rows
					Cell CellData = null;
					File CsvfileName = new File(CSVFilePath);
					OutputStream out = new FileOutputStream(CsvfileName);
					OutputStreamWriter osw = new OutputStreamWriter(out, Encoding);
					BufferedWriter bw = new BufferedWriter(osw);
					for(int Row = 0;Row <rowcount;Row ++ )
					{
						//RowData = S1.getRow(Row);
						String RowDataCSV = ""; // will hold the row data separated by comma.
						for(int col = 0; col <colcount;col ++ )
						{
							//CellData = S1.getCell(col,Row);
							CellData = S1.getCell(col,Row);
							if (col ==0)
							{
								//RowDataCSV = CellData.getContents().replaceAll(",", "");
								RowDataCSV = ((jxl.Cell) CellData).getContents().replaceAll(",", "");
							}
							else
							{
								//RowDataCSV = RowDataCSV + Seperator + CellData.getContents().replaceAll(",", "");
								RowDataCSV = RowDataCSV + Seperator + ((jxl.Cell) CellData).getContents().replaceAll(",", "");
							}
							// System.out.print(" ROW/COL [" + Row + "][" + col + "]" + CellData.getContents() + " ");
						}
						bw.write(RowDataCSV);
						bw.newLine();
					}
					bw.flush();
					bw.close();
					String FileDirectoryPath = new File(CSVFilePath).getParent();
					String ImportFileName = new File(CSVFilePath).getName();
					CSVImportInMySQL(ConnMySQL, TableName, FileDirectoryPath, ImportFileName, HeaderYesNo);
				}
				
				

				
				catch(BiffException be)
				{
					System.out.println("Error while Converting XLS into CsV , File " + XLSFilePath + " " + be.getLocalizedMessage());
					System.out.println("********** Failed Query = "+ PrintQuery +" ******");
				}
	
				
			}
			catch(Exception e)
			{
				System.out.println("********** Query fail Reason : "+ e.getLocalizedMessage() +" ******");
				System.out.println("********** Failed Query = "+ PrintQuery +" ******");
			}
			finally
			{
				ConnMySQL.close();
				ConnMySQL = null;
			}
		}
		public static void CSVImportInMySQL(Connection ConnectionName,String TableName,String FileDirectoryPath,String ImportFileName,String HeaderYesNo) throws  FileNotFoundException, SQLException
		{
			String CompleteFileData =null;
			int TableColCount = 0;
			String PrintQuery = "";
			try
			{
				FileInputStream fis = new FileInputStream(FileDirectoryPath + "\\" + ImportFileName);
				int n;
				while ((n = fis.available()) > 0)
				{
					byte[] b = new byte[n];
					int result = fis.read(b);
					if (result == -1)
						break;
					CompleteFileData = new String(b);
				} // End while
				fis.close();
			} // End try
			catch (IOException e)
			{
				System.out.println("********** Query fail Reason : "+ e.getLocalizedMessage() +" ******");
				System.out.println("********** Failed Query = "+ PrintQuery +" ******");
			}
			StringTokenizer NewLinest = new StringTokenizer(CompleteFileData,"\r\n");
			System.out.println(" Total Rows in File :: " + NewLinest.countTokens());
			int rowCount = 0;
			String ColumnNameList =null;
			while (NewLinest.hasMoreTokens())
			{
				String RowData = NewLinest.nextToken();
				if(RowData.indexOf("\"") > 0)
				{
					RowData = GetRowData_MySQL(RowData,"\"");
				}
				if(RowData.indexOf("'") > 0)
				{
					RowData = RowData.replaceAll("'", "''");
				}
				RowData = RowData.replaceAll(",", "#,#");
				StringTokenizer ColumnData = new StringTokenizer(RowData,",");
				int ColumnCount = ColumnData.countTokens();
				if(rowCount == 0)
				{
					TableColCount = createTableStructure_MySQL(ConnectionName,TableName,RowData,HeaderYesNo);
					if(!HeaderYesNo.toUpperCase().equals("YES"))
					{
						rowCount ++;
					}
				}
				if(ColumnCount > TableColCount)
				{
					int ExistingNoofColumns = TableColCount;
					int NoOfColunsNeedToAdd = ColumnCount - TableColCount;
					AlterTableStructure_MySQL(TableName, ConnectionName, ExistingNoofColumns, NoOfColunsNeedToAdd);
					TableColCount = TableColCount + NoOfColunsNeedToAdd;
				}
				try
				{
					ColumnNameList = getColumnNameList_MySQL(ConnectionName,TableName,ColumnCount);
				}
				catch (Exception e)
				{
					System.out.println("********** Query fail Reason : "+ e.getLocalizedMessage() +" ******");
					System.out.println("********** Failed Query = "+ PrintQuery +" ******");
				}
				RowData = RowData.replaceAll("#,#", ",");
				String InsertColumnData = RowData.replaceAll(",", "','");
				InsertColumnData = "'" + InsertColumnData + "'";
				if ((rowCount == 0) && HeaderYesNo.toUpperCase().equals("YES"))
				{
					rowCount ++;
				}
				else
				{
					String sQuery = null;
					try
					{
						sQuery = " Insert Into "+Database+"." + TableName + "(" + ColumnNameList +") Values(" + InsertColumnData +")";
						PrintQuery = sQuery ;
						Statement StmtCases = ConnectionName.createStatement();
						StmtCases.executeUpdate(sQuery);
						StmtCases.close();
						StmtCases = null;
					}
					catch(SQLException e)
					{
						System.out.println("Error in insert query " + sQuery + " ::" + e.getLocalizedMessage());
						System.out.println("********** Failed Query = "+ PrintQuery +" ******");
					}
				}
			}
		}
		public static int createTableStructure_MySQL(Connection ConnectionName,String TableName,String RowData,String HeaderYesNo) throws SQLException, FileNotFoundException
		{
			String sQuery;
			Statement StmtCases;
			String PrintQuery = "";
			try
			{
				sQuery = " Drop Table "+Database+"." + TableName + " ";
				PrintQuery = sQuery;
				StmtCases = ConnectionName.createStatement();
				StmtCases.executeUpdate(sQuery);
				StmtCases.close();
			}
			catch(SQLException e)
			{
				System.out.println("********** Query fail Reason : "+ e.getLocalizedMessage() +" ******");
				System.out.println("********** Failed Query = "+ PrintQuery +" ******");
			}
			try
			{
				StringTokenizer FullRowData = new StringTokenizer(RowData,",");
				System.out.println(" Total Columns in File :: " + FullRowData.countTokens());
				String ColumnName = "";
				int ColCount = 0;
				if(HeaderYesNo.toUpperCase().equals("YES"))
				{
					while(FullRowData.hasMoreTokens())
					{
						ColCount = ColCount + 1;
						String ColumnData = FullRowData.nextToken();
						if(ColumnData.trim().equals("##") || ColumnData.trim().equals("#"))
						{
							ColumnData = "F" + ColCount + "";
						}
						ColumnName = ColumnName + ColumnData.trim() + ",";
					}
				}
				else
				{
					while(FullRowData.hasMoreTokens())
					{
						ColCount = ColCount + 1;
						String ColumnData = "F" + ColCount + "";
						ColumnName = ColumnName + "[" + ColumnData.trim() + "],";
						FullRowData.nextToken();
					}
				}
				ColumnName = ColumnName.replaceAll("#", "");
				ColumnName = ColumnName.replaceAll(",", " VARCHAR(500),");
				if(ColumnName.length() != 0)
				{
					ColumnName = ColumnName.substring(0, ColumnName.length() - 1);
					//ColumnName = ColumnName.replaceAll("a", "");
					//ColumnName = ColumnName.replaceAll("[", "");
					//ColumnName = ColumnName.replaceAll("]", "");
				}
				String sQueryCreate = "CREATE TABLE "+Database+"." + TableName + " (" + ColumnName + ") ";
				PrintQuery = sQueryCreate;
				Statement StmtCreate = ConnectionName.createStatement();
				StmtCreate.executeUpdate(sQueryCreate);
				StmtCreate.close();
				return ColCount;
			}
			catch(Exception e)
			{
				System.out.println("********** Query fail Reason : "+ e.getLocalizedMessage() +" ******");
				System.out.println("********** Failed Query = "+ PrintQuery +" ******");
				return 0;
			}
		}
		public static String getColumnNameList_MySQL(Connection ConnectionAccess,String TableName,int ColumnCount) 
		{
			String PrintQuery = "";
			try
			{
				String sQuery = " Select * From "+Database+"." + TableName +" Where 1 = 2 ";
				PrintQuery = sQuery;
				Statement StmtCases = ConnectionAccess.createStatement();
				ResultSet ColumnList = StmtCases.executeQuery(sQuery);
				ResultSetMetaData ColumnListMetaData = ColumnList.getMetaData();
				String ColumnNameList = ColumnListMetaData.getColumnName(1);
				for (int ColCountLoop = 2;ColCountLoop <= ColumnCount;ColCountLoop ++)
				{
					ColumnNameList = ColumnNameList + "," + ColumnListMetaData.getColumnName(ColCountLoop) ;
				}
				//ColumnNameList = ColumnNameList + "]";
				StmtCases.close();
				return ColumnNameList;
			}
			catch(Exception e)
			{
				System.out.println("********** Query fail Reason : "+ e.getLocalizedMessage() +" ******");
				System.out.println("********** Failed Query = "+ PrintQuery +" ******");
				return null;
			}
		}
		public static String GetRowData_MySQL(String FileRowData,String Delimeter)
		{
			String PrintQuery = "";
			try
			{
				StringTokenizer ST = new StringTokenizer(FileRowData,Delimeter);
				int TokenNo = 1;
				String FinalString = "";
				while(ST.hasMoreTokens())
				{
					String CurrentToken = ST.nextToken();
					if(TokenNo % 2 == 0)
					{
						FinalString = FinalString + CurrentToken.replaceAll(",", "");
					}
					else
					{
						FinalString = FinalString + CurrentToken;
					}
					TokenNo ++;
				}
				return FinalString;
			}
			catch(Exception e)
			{
				System.out.println("********** Query fail Reason : "+ e.getLocalizedMessage() +" ******");
				System.out.println("********** Failed Query = "+ PrintQuery +" ******");
				return null;
			}
		}
		public static void AlterTableStructure_MySQL(String TableName,Connection ConnectionName,int ExistingNoofColumns,int NoOfColunsNeedToAdd) 
		{
			String PrintQuery = "";
			try
			{
				for(int i = 1; i <= NoOfColunsNeedToAdd;i++)
				{
					String sQueryAlter = "ALTER TABLE "+Database+"." + TableName + " ADD F" + (ExistingNoofColumns + i) + " VARCHAR(250) ";
					PrintQuery = sQueryAlter;
					Statement StmtAlter = ConnectionName.createStatement();
					StmtAlter.execute(sQueryAlter);
					StmtAlter.close();
				}
			}
			catch(Exception e)
			{
				System.out.println("********** Query fail Reason : "+ e.getLocalizedMessage() +" ******");
				System.out.println("********** Failed Query = "+ PrintQuery +" ******");
			}
		}
		public static Connection OpenConnection(String Database, String UserName,String Password)
		{
			Connection Conn = null;
			try
			{
				Class.forName("com.mysql.jdbc.Driver");  
				Conn=DriverManager.getConnection("jdbc:mysql://"+Databaseconnection+":"+DatabasePort+"",UserName,Password);			
			}
			 catch(Exception ex)
			{
				 
			}
			return Conn;
		}
	}

	
	
	

