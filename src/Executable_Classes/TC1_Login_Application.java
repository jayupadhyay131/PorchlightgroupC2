package Executable_Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Utilities_Classes.Utility;


public class TC1_Login_Application 
{
	Utilities_Classes.Utility utility = new Utilities_Classes.Utility();
	Functional_Classes.Functions functions = new Functional_Classes.Functions();
	public Utilities_Classes.RepositoryParser parser;
//	public static String TestDB = "test.TestingData";
	public static String TestDB = "porchdb.porchdb_test_data";
	int StartCaseNo = 1;
	String Cases,Execute;
	static WebDriver driver;
	
	
	@BeforeTest
	@Parameters("browser")
	public void OpenBrowser(String browser) throws IOException 
	{
		parser = new Utilities_Classes.RepositoryParser("project.properties");
		driver = utility.startBrowser(browser);
		Execute = "Y";
	}
	
	@BeforeMethod
	public void GetData() throws SQLException, ClassNotFoundException
	{
		Connection Conn = Utility.OpenConnenctionTestData();
		Statement stmt=Conn.createStatement();  
		ResultSet rs=stmt.executeQuery("SELECT Cases,ExecuteYN  FROM "+TestDB+" WHERE CaseNo = "+StartCaseNo);  

		while(rs.next())  
		{
			Cases = rs.getString(1);
			Execute = rs.getString(2);
		}

		stmt.close();
		Conn.close();
		StartCaseNo++;
	}
		
	@Test(priority=2)
	public void Open_URL() throws ClassNotFoundException, SQLException 
	{
		functions.Enter_URL(driver, Cases);
		Execute = "Y";
	}
	
	@Test(priority=3)
	public void Enter_UserName() throws InterruptedException, ClassNotFoundException, SQLException 
	{
		functions.Enter_UserName(driver, Cases,parser);
		Execute = "Y";
	}
	
	@Test(priority=4)
	public void Enter_Password()  throws InterruptedException, ClassNotFoundException, SQLException 
	{
		functions.Enter_Password(driver, Cases,parser);
		Execute = "Y";
	}
	
	@Test(priority=5)
	public void Login_Button()  throws InterruptedException 
	{
		functions.Login_Button(driver);
		Execute = "Y";
	}
	
	@Test(priority=6)
	public void Close_Browser() 
	{
		driver.quit();
	}
}
