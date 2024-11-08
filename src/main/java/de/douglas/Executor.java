package de.douglas;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.util.Assert;
import de.douglas.enums.Action;
import de.douglas.enums.AlternativeClickType;
import de.douglas.enums.Browser;
import de.douglas.enums.Channel;
import de.douglas.util.ExtentManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static de.douglas.util.Constants.*;

@Slf4j
public class Executor {
	ExtentTest test;
	RemoteWebDriver driver;
	Map<String, Object> executionSpecifications;
	WebDriverWait wait;

	public Executor(String testName, Map<String, Object> executionSpecifications) {
		this.executionSpecifications = executionSpecifications;
		initializeDriver();
		log.info("Starting executor");
		test = ExtentManager.getInstance().createTest(testName);
		wait = new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIMEOUT);
	}

	public void initializeDriver() {
		log.info("Initializing driver");
		Capabilities capabilities = null;
		log.debug("Execution Specifications: {}", executionSpecifications);
		switch ((Browser) executionSpecifications.get("browser")) {
			case CHROME:
				capabilities = new ChromeOptions();
				break;
			case SAFARI:
				capabilities = new SafariOptions();
				break;
			case FIREFOX:
				capabilities = new FirefoxOptions();
		}
		switch ((Channel) executionSpecifications.get("channel")) {
			case LOCAL:
				assert capabilities != null;
				switch ((Browser) executionSpecifications.get("browser")) {
					case CHROME:
						assert capabilities instanceof ChromeOptions;
						driver = new ChromeDriver((ChromeOptions) capabilities);
						break;
					case SAFARI:
						driver = new SafariDriver();
						break;
					case FIREFOX:
						assert capabilities instanceof FirefoxOptions;
						driver = new FirefoxDriver((FirefoxOptions) capabilities);
				}
				driver.manage().window().fullscreen();
				driver.manage().deleteAllCookies();
				break;
			case GRID:
				try {
					driver = new RemoteWebDriver(new URL((String) executionSpecifications.get("gridUrl")), capabilities);
				} catch (MalformedURLException e) {
					throw new RuntimeException(e);
				}
				break;
		}
		driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT_TIMEOUT);
		driver.get((String) executionSpecifications.get("url"));
		log.info("Driver initialized");
	}

	public void safelyCloseAllBrowserInstances() {
		if (driver != null) {
			closeAllTabsAndWindows();
			driver.quit();
		} else {
			log(Status.INFO, "Driver not initialized");
		}
	}

	private void closeAllTabsAndWindows() {
		try {
			for (String window : driver.getWindowHandles()) {
				driver.switchTo().window(window);
				driver.close();
				log(Status.INFO, "Closing window " + window);
			}
		} catch (Exception ignored) {
		}
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	private WebElement findElement(By by) {
		return highlight(wait.until(ExpectedConditions.presenceOfElementLocated(by)));
	}

	private WebElement findElementInsideShadowRoot(By shadowRootBy, By elementBy) {
		WebElement shadowRoot = findElement(shadowRootBy);
		log.info("Shadow Size: {}", shadowRoot.getSize());
		SearchContext context = shadowRoot.getShadowRoot();
		WebElement element = context.findElement(elementBy);
		return element;
	}

	private WebElement highlight(WebElement element) {
//		driver.executeScript("arguments[0].scrollIntoView()", element);
		driver.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;')", element);
		return element;
	}

	public void click(By by, AlternativeClickType... clickType) {
		try {
			if (clickType.length == 0) {
				findElement(by).click();
			} else if (clickType.length == 1) {
				switch (clickType[0]) {
					case ACTIONS:
						new Actions(driver).moveToElement(findElement(by)).click().build().perform();
						break;
					case JAVASCRIPT:
						driver.executeScript("arguments[0].click()", findElement(by));
						break;
				}
			} else {
				throw new IllegalArgumentException("More than one Click Type Passed: " + Arrays.toString(clickType));
			}
			log(Status.PASS, getReportMessageMap(Action.CLICK, by));
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}

	public void click(By shadowRootLocatedBy, By elementLocatedBy) {
		try {
			findElementInsideShadowRoot(shadowRootLocatedBy, elementLocatedBy).click();
			log(Status.PASS, getReportMessageMap(Action.CLICK, elementLocatedBy));
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}

	public void sendKeys(By by, CharSequence... keysToSend) {
		try {
			findElement(by).sendKeys(keysToSend);
			log(Status.PASS, getReportMessageMap(Action.SEND_KEYS, by, keysToSend));
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}

	public void logConsole(Status status, String message) {
		switch (status) {
			case WARNING:
				log.warn(message);
				break;
			case FAIL:
				log.error(message);
				break;
			default:
				log.info(message);
				break;
		}
	}

	public void log(Status status, String message) {
		logConsole(status, message);
		test.log(status, getExtentMessage(getReportMessageMap(status, message)));
	}

	public void log(Status status, Map<String, String> messageMap) {
		logConsole(status, messageMap.toString());
		test.log(status, getExtentMessage(messageMap));
	}

	public void logFailureException(Throwable message) {
		logConsole(Status.FAIL, message.getMessage());
		test.log(Status.FAIL, message);
	}

	private String getExtentMessage(Map<String, String> messageMap) {
		String header = messageMap.get(ACTION_KEY);
		if (messageMap.containsKey(TEXT_KEY)) {
			header = header + ": " + messageMap.get(TEXT_KEY);
		}
		return EXTENT_LOG_SAMPLE.replace(HTML_LOG_IMAGE_BASE64, getScreenshotAsBase64()).replace(HTML_LOG_CODE, messageMap.get(MESSAGE_KEY)).replace(HTML_LOG_HEADER, header.toUpperCase());
	}

	private Map<String, String> getReportMessageMap(Action action, By by, CharSequence... keysToSend) {
		Map<String, String> message = new TreeMap<>();
		if (Objects.requireNonNull(action) == Action.CLICK) {
			message.put(ACTION_KEY, "Click");
		} else if (Objects.requireNonNull(action) == Action.SEND_KEYS) {
			message.put(ACTION_KEY, "Send Keys");
			message.put(TEXT_KEY, generateStringFromCharSequence(keysToSend));
		}
		message.put(MESSAGE_KEY, by.toString().replace("By.", ""));
		return message;
	}

	private Map<String, String> getReportMessageMap(Status status, String message) {
		Map<String, String> messageMap = new TreeMap<>();
		messageMap.put(ACTION_KEY, status.name());
		messageMap.put(MESSAGE_KEY, message);
		return messageMap;
	}

	private String generateStringFromCharSequence(CharSequence... keysToSend) {
		StringBuilder stringBuilder = new StringBuilder();
		for (CharSequence key : keysToSend) {
			stringBuilder.append(key);
		}
		return stringBuilder.toString();
	}

	private String getScreenshotAsBase64() {
		String base64Image = driver.getScreenshotAs(OutputType.BASE64);
		Assert.notEmpty(base64Image, "Screenshot is empty");
		if (!base64Image.startsWith("data:")) {
			base64Image = "data:image/png;base64," + base64Image;
		}
		return base64Image;
	}

	public boolean isElementPresent(By by) {
		try {
			return findElement(by).isDisplayed();
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}

	public String getText(By by) {
		try {
			return findElement(by).getText();
		} catch (Exception exception) {
			log(Status.FAIL, exception.getMessage());
			throw exception;
		}
	}


}
