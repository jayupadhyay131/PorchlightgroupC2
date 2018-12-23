package Functional_Classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import Executable_Classes.Login_Application;
import Utilities_Classes.RepositoryParser;
import Utilities_Classes.Utility;

public class Functions {

	String TestDB = Login_Application.TestDB;
			
	public void Enter_URL(WebDriver driver, String Cases) throws SQLException, ClassNotFoundException
	{
		Connection Conn = Utility.OpenConnenctionTestData();
		Statement stmt=Conn.createStatement();  
		ResultSet rs=stmt.executeQuery("SELECT URL  FROM "+TestDB+" WHERE Cases = '"+Cases+"'");  
		
		while(rs.next())  
		{
				String URL = rs.getString(1);
				
				driver.get(URL);
		}
		stmt.close();
		Conn.close();
		
	}
	
	public void Enter_UserName(WebDriver driver, String Cases,RepositoryParser parser) throws InterruptedException, SQLException, ClassNotFoundException
	{
		Connection Conn = Utility.OpenConnenctionTestData();
		Statement stmt=Conn.createStatement();  
		ResultSet rs=stmt.executeQuery("SELECT UserName  FROM "+TestDB+" WHERE Cases = '"+Cases+"'");
		
		while(rs.next())  
		{
				String UserName = rs.getString(1);
				
				driver.findElement(By.id("identifierId")).sendKeys(UserName);;
				driver.findElement(By.id("identifierNext")).click();
				Thread.sleep(5000);
				
		}
		stmt.close();
		Conn.close();
		
		
	}
	
	public void Enter_Password(WebDriver driver, String Cases,RepositoryParser parser) throws InterruptedException, SQLException, ClassNotFoundException
	{
		Connection Conn = Utility.OpenConnenctionTestData();
		Statement stmt=Conn.createStatement();  
		ResultSet rs=stmt.executeQuery("SELECT Password  FROM "+TestDB+" WHERE Cases = '"+Cases+"'");
		
		while(rs.next())  
		{
				String Password = rs.getString(1);
				
				driver.findElement(By.cssSelector("#password .zHQkBf")).sendKeys(Password);
				Thread.sleep(5000);
				
		}
		stmt.close();
		Conn.close();
		
	}
	
	public void Login_Button(WebDriver driver) throws InterruptedException
	{
		driver.findElement(By.id("passwordNext")).click();
		Thread.sleep(5000);
	}
}
