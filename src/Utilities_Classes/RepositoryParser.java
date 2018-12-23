package Utilities_Classes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;

public class RepositoryParser {

		private FileInputStream stream;
		private String RepositoryFile;
		private Properties propertyfile = new Properties();
		
		public RepositoryParser(String fileName) throws IOException{
			
			this.RepositoryFile = fileName;
			stream = new FileInputStream(RepositoryFile);
			propertyfile.load(stream);
		}
		
		public By getObjectLocator(String locatorName){
			
			String locatorProperties = propertyfile.getProperty(locatorName);
			//System.out.println(locatorName.toString());
			String LocatorType = locatorProperties.substring(0,locatorProperties.indexOf(":"));
			String LocatorValue = locatorProperties.substring(locatorProperties.indexOf(":")+1,locatorProperties.length());
			
			By locator = null;
			
			switch (LocatorType) {
			case "id": locator = By.id(LocatorValue);
				break;
			case "name": locator = By.name(LocatorValue);
				break;
			case "xpath": locator = By.xpath(LocatorValue);
				break;
			case "tagname": locator = By.tagName(LocatorValue);
				break;
			case "linktext": locator = By.linkText(LocatorValue);
				break;
			case "partiallinktext": locator = By.partialLinkText(LocatorValue);
				break;
			case "cssselector": locator = By.cssSelector(LocatorValue);
				break;
			case "classname": locator = By.className(LocatorValue);
				break;
			}
			
			return locator;
		}
		
		public String getObjectLocatorValue (String locatorName){
			
			String locatorProperties = propertyfile.getProperty(locatorName);
			String LocatorValue = locatorProperties.split(":")[1];
			
			return LocatorValue;
			
		}
}
