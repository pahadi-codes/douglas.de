package de.douglas;
import com.aventstack.extentreports.ExtentReports;
import de.douglas.enums.Browser;
import de.douglas.enums.Channel;
import de.douglas.util.ExtentManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
@Slf4j
public class TestBase {
	public Executor executor;
	Properties properties = new Properties();
	public Map<String, Object> executionSpecifications = new TreeMap<>();
	public void initialize() {
		log.info("Initializing Execution");
		try {
			InputStream stream = Executor.class.getResourceAsStream("/environment.properties");
			properties.load(stream);
		} catch (IOException e) {
			e.fillInStackTrace();
		}
		initializeExecutionSpecifications();
	}

	public void initializeExecutionSpecifications() {
		log.info("Initializing execution specifications");
		executionSpecifications.put("browser", Browser.valueOf(System.getProperty("browser", properties.getProperty("browser")).toUpperCase()));
		executionSpecifications.put("channel", Channel.valueOf(System.getProperty("channel", properties.getProperty("channel")).toUpperCase()));
		if (executionSpecifications.get("channel") == Channel.GRID) {
			executionSpecifications.put("gridUrl", System.getProperty("gridUrl", properties.getProperty("gridUrl").toUpperCase()));
		}
		executionSpecifications.put("url", System.getProperty("url", properties.getProperty("url")));
		log.info("Execution Specifications: {}", executionSpecifications);
	}
	//Loading Initial Configs
	@BeforeSuite
	public void beforeSuite() {
		log.info("Before Suite");
		initialize();
	}
	@AfterSuite
	public void afterSuite() {
		log.info("After Suite");
	}
//	@BeforeTest
//	public void beforeTest() {
//		log.info("Before Test");
//	}
//
	@AfterTest
	public void afterTest() {
		log.info("After Test");
		ExtentManager.getInstance().flush();
	}
//
//	@BeforeClass
//	public void beforeClass() {
//		log.info("Before Class");
//	}
//
//	@AfterClass
//	public void afterClass() {
//		log.info("After Class");
//	}
//
//	@BeforeGroups
//	public void beforeGroups() {
//		log.info("Before Groups");
//		initialize();
//	}
//
//	@AfterGroups
//	public void afterGroups() {
//		log.info("After Groups");
//	}
}
