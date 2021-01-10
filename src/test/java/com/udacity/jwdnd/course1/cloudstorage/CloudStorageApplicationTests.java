package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

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
//		driver.get("http://localhost:" + this.port + "/signup");
//		// signing up
//		driver.findElement(By.id("inputFirstName")).sendKeys("John");
//		driver.findElement(By.id("inputLastName")).sendKeys("Doe");
//		driver.findElement(By.id("inputUsername")).sendKeys("testUser");
//		driver.findElement(By.id("inputPassword")).sendKeys("test123");
//		driver.findElement(By.cssSelector(".btn")).click();
//		//signing in
//		driver.get("http://localhost:" + this.port + "/login");
//		Assertions.assertEquals("Login", driver.getTitle());
//		driver.findElement(By.id("inputUsername")).sendKeys("testUser");
//		driver.findElement(By.id("inputPassword")).sendKeys("test123");
//		driver.findElement(By.id("submit-btn")).click();
//		//verify that the homepage is accessible
//		Assertions.assertEquals("Home", driver.getTitle());
		signingIn();
		//logging out
		driver.findElement(By.cssSelector(".btn-secondary:nth-child(2)")).click();
		//verify that the homepage is no longer accessible
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	public void createNoteTest() {

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
