package org.research.kadda.nuclide.service;

import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.After;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.JavascriptExecutor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class SeleniumNuclideTest {
	/*
	private final Logger log = LoggerFactory.getLogger(SeleniumNuclideTest.class);
	private static StringBuilder builder = new StringBuilder();
	private WebDriver driver;
	private Map<String, Object> vars;
	JavascriptExecutor js;

	@Before
	public void start() {
		try {
			Class<? extends WebDriver> driverClass = ChromeDriver.class;
			ChromeDriverManager.getInstance(driverClass).setup();
			driver = new ChromeDriver();
			js = (JavascriptExecutor) driver;
			vars = new HashMap<String, Object>();
		} catch (Exception ex) {
			log.info("Exception while instantiating driver. ", ex);
		}
	}

	@After
	public void tearDown() throws FileNotFoundException, UnsupportedEncodingException {
		driver.quit();
		PrintWriter logFile = new PrintWriter("seleniumNuclideTest.log", "UTF-8");
		logFile.write(builder.toString());
		logFile.close();
	}

	public void login() {
		driver.get("https://apollo/nuclideDB/login");
		driver.manage().window().setSize(new Dimension(986, 744));
		driver.findElement(By.id("form_userId")).click();
		driver.findElement(By.id("form_userId")).sendKeys("benmeka1");
		driver.findElement(By.id("form_userPassword")).click();
		driver.findElement(By.id("form_userPassword")).sendKeys("SEC4321");
		driver.findElement(By.name("Submit")).click();
	}

	@Test
	public void newTracer() {
		login();
		driver.get("https://apollo/nuclideDB/main");
		driver.manage().window().setSize(new Dimension(986, 745));
		driver.findElement(By.linkText("New Tracer Tube")).click();
		driver.findElement(By.id("form_nuclideName")).click();
		{
			WebElement dropdown = driver.findElement(By.id("form_nuclideName"));
			dropdown.findElement(By.xpath("//option[. = '125I']")).click();
		}
		driver.findElement(By.id("form_nuclideName")).click();
		driver.findElement(By.id("form_substanceName")).click();
		driver.findElement(By.id("form_substanceName")).sendKeys("subsTest125i");
		driver.findElement(By.id("batchName")).click();
		driver.findElement(By.id("batchName")).sendKeys("batchTest");
		driver.findElement(By.cssSelector("tr:nth-child(5)")).click();
		driver.findElement(By.id("form_initialActivity")).sendKeys("2000");
		vars.put("varInitialActivity", driver.findElement(By.id("form_initialActivity")).getAttribute("value"));
		System.out.println(vars.get("varInitialActivity").toString());
		driver.findElement(By.cssSelector("tr:nth-child(6)")).click();
		driver.findElement(By.id("form_initialAmount")).sendKeys("2000");
		vars.put("varInitialAmount", driver.findElement(By.id("form_initialAmount")).getAttribute("value"));
		System.out.println(vars.get("varInitialAmount").toString());
		driver.findElement(By.id("form_location")).click();
		{
			WebElement dropdown = driver.findElement(By.id("form_location"));
			dropdown.findElement(By.xpath("//option[. = 'C-Lab Biology H89.O2.K07.1']")).click();
		}
		driver.findElement(By.id("form_location")).click();
		driver.findElement(By.name("Submit")).click();
		driver.findElement(By.cssSelector("p")).click();
		{
			WebElement element = driver.findElement(By.cssSelector("p"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		vars.put("varTracerId", driver.findElement(By.id("tracerIdRecorded")).getText());
		System.out.println(vars.get("varTracerId").toString());
		driver.findElement(By.linkText("Tracer Overview")).click();
		driver.findElement(By.name("Submit")).click();
		driver.findElement(By.cssSelector("label > input")).click();
		driver.findElement(By.cssSelector("label > input")).sendKeys(vars.get("varTracerId").toString());
		driver.findElement(By.cssSelector(".button2")).click();
		driver.findElement(By.cssSelector("tr:nth-child(9) > td:nth-child(2) > div")).click();
		{
			WebElement element = driver.findElement(By.cssSelector("tr:nth-child(9) > td:nth-child(2) > div"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		assertThat(driver.findElement(By.cssSelector("tr:nth-child(9) > td:nth-child(2) > div")).getText(),
				is("vars.get(\"varInitialActivity\").toString()"));
		{
			WebElement element = driver.findElement(By.cssSelector("tr:nth-child(11) > td:nth-child(2) > div"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		assertThat(driver.findElement(By.cssSelector("tr:nth-child(11) > td:nth-child(2) > div")).getText(),
				is("vars.get(\"varInitialAmount\").toString()"));
	}

	@Rule
	public TestWatcher watchman = new TestWatcher() {
		@Override
		protected void failed(Throwable e, Description description) {
			if (description != null) {
				builder.append(description);
			}
			if (e != null) {
				builder.append(' ');
				builder.append(e);
			}
			builder.append(" FAIL\n");
		}

		@Override
		protected void succeeded(Description description) {
			if (description != null) {
				builder.append(description);
			}
			builder.append(" OK\n");
		}
	};
	*/
}
