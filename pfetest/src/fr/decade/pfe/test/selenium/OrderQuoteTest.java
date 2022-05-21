/**
 *
 */
package fr.decade.pfe.test.selenium;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


/**
 * @author mariem
 *
 */
public class OrderQuoteTest
{
	private WebDriver driver;
	private Map<String, Object> vars;
	JavascriptExecutor js;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		driver = new FirefoxDriver();
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		driver.quit();
	}

	@Test
	public final void orderQuote()
	{
		driver.get("https://powertools.local:9002/powertools/en/USD/login");
		driver.manage().window().setSize(new Dimension(1710, 667));
		driver.findElement(By.id("j_username")).click();
		driver.findElement(By.id("j_password")).sendKeys("123456");
		driver.findElement(By.id("j_username")).sendKeys("william.hunter@rustic-hw.com");
		driver.findElement(By.cssSelector(".btn-primary")).click();
		driver.findElement(By.cssSelector(".yCmsComponent > .myAccountLinksHeader")).click();
		driver.findElement(By.linkText("Quotes")).click();
		driver.findElement(By.name("action")).click();
		driver.findElement(By.linkText("00013000")).click();
		Assert.assertEquals(driver.findElement(By.id("status-00013000")).getText(), "Ordred");
	}

}
