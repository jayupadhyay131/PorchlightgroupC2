package Utilities_Classes;

public class EMailCounter 
{
	public static void UpdateExcel() throws Exception
	{
		try
		{
			ExcelUtils.setExcelFile(TestData.Path_TestData + TestData.File_TestData,"Register_Sheet");
			String RegisterID = ExcelUtils.getCellData(1, "Email");
			String GetMailId = RegisterID.substring(0, RegisterID.indexOf("+")+1);
			int getlastdegit = Integer.parseInt(RegisterID.substring(RegisterID.indexOf("+")+1,RegisterID.indexOf("@")));
			String Extension = RegisterID.substring(RegisterID.indexOf("@"),RegisterID.length());
			
			// Update Email ID
			getlastdegit = getlastdegit+1;
			RegisterID = GetMailId+getlastdegit+Extension;
			ExcelUtils.setCellData(RegisterID, 1, 2);
//			ExcelUtils.setCellData(RegisterID, 16, 0);
					
			
			/*// Update ResellerId
			getlastdegit++;
			VenueRegisterID = GetMailId+getlastdegit+Extension;
			ExcelUtils.setCellData(VenueRegisterID, 5, 2);
			ExcelUtils.setCellData(VenueRegisterID, 6, 2);
			*/
			System.out.println("Excel LoginId Updated for Next Run");
		}
		catch(Exception ex){
			
			System.out.println(ex);
		}
		
	}
	
	
	public static void UpdateUserEmail() throws Exception
	{
		try
		{
			ExcelUtils.setExcelFile(TestData.Path_TestData + TestData.File_TestData,"CreateUser_Sheet");
			String RegisterID = ExcelUtils.getCellData(1, "Email");
			String GetMailId = RegisterID.substring(0, RegisterID.indexOf("+")+1);
			int getlastdegit = Integer.parseInt(RegisterID.substring(RegisterID.indexOf("+")+1,RegisterID.indexOf("@")));
			String Extension = RegisterID.substring(RegisterID.indexOf("@"),RegisterID.length());
			
			// Update Email ID
			getlastdegit = getlastdegit+1;
			RegisterID = GetMailId+getlastdegit+Extension;
			ExcelUtils.setCellData(RegisterID, 1, 2);
//			ExcelUtils.setCellData(RegisterID, 16, 0);
					
			
			/*// Update ResellerId
			getlastdegit++;
			VenueRegisterID = GetMailId+getlastdegit+Extension;
			ExcelUtils.setCellData(VenueRegisterID, 5, 2);
			ExcelUtils.setCellData(VenueRegisterID, 6, 2);
			*/
//			System.out.println("User Excel LoginId Updated for Next Run");
		}
		catch(Exception ex){
			
			System.out.println(ex);
		}
		
	}
	

}
