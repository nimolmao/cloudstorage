package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void unauthorizedAccessTest() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void authorizedAccessTest() {
		signingIn();
		//logging out
		driver.findElement(By.id("logout-btn")).click();
		//verify that the homepage is no longer accessible
//		driver.get("http://localhost:" + this.port + "/home");
//		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	public void createNoteTest() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		String noteDesciption = "Adding note test description.";
		signingIn();
		driver.findElement(By.id("nav-notes-tab")).click();
		WebElement addNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-note-btn")));
		addNoteButton.click();
		{
			WebElement element = driver.findElement(By.id("add-note-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.id("note-title")).sendKeys("note1");
		driver.findElement(By.id("note-description")).sendKeys(noteDesciption);
		driver.findElement(By.cssSelector("#noteModal .btn-primary")).click();
		driver.findElement(By.linkText("here")).click();
		WebElement notesTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
		notesTab.click();
		WebElement body = wait.until(ExpectedConditions.elementToBeClickable(By.tagName("body")));
		String bodyText = body.getText();
		Assertions.assertEquals(true, bodyText.contains("note1"));
	}

	public void signingIn() {
		driver.get("http://localhost:" + this.port + "/signup");
		// signing up
		driver.findElement(By.id("inputFirstName")).sendKeys("John");
		driver.findElement(By.id("inputLastName")).sendKeys("Doe");
		driver.findElement(By.id("inputUsername")).sendKeys("testUser");
		driver.findElement(By.id("inputPassword")).sendKeys("test123");
		driver.findElement(By.cssSelector(".btn")).click();
		//signing in
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.findElement(By.id("inputUsername")).sendKeys("testUser");
		driver.findElement(By.id("inputPassword")).sendKeys("test123");
		driver.findElement(By.id("submit-btn")).click();
		//verify that the homepage is accessible
		Assertions.assertEquals("Home", driver.getTitle());
	}

}
