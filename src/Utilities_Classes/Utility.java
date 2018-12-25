package Utilities_Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Utility {
	static WebDriver driver = null;
	static String Databaseconnection = "localhost";
	
	// change password here
	static String DBPassword = "mysql1991"; 
	
	static String DBUserName = "root";
	static String DatabasePort = "3306";
	
	public WebDriver startBrowser(String BrowserName)
	{
		driver = null;
		switch (BrowserName)
		{
			case "firefox":
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"\\RequiredFiles\\geckodriver.exe");
				driver = new FirefoxDriver();
				break;
			
			case "chrome":
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\RequiredFiles\\chromedriver.exe");
				driver = new ChromeDriver();
				break;
			
			case "ie":
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\RequiredFiles\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				break;
		}
		
		driver.manage().window().maximize();
		return driver;
	}
	
	public static Connection OpenConnenctionTestData() throws ClassNotFoundException, SQLException
	{
		Connection Conn = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  
			Conn=DriverManager.getConnection("jdbc:mysql://"+Databaseconnection+":"+DatabasePort+"",DBUserName,DBPassword);			
		}
		 catch(Exception ex)
		{
			 
		}
		return Conn;
	}
}
