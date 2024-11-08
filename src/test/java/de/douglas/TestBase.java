package de.douglas;

import de.douglas.enums.Browser;
import de.douglas.enums.Channel;
import de.douglas.util.Constants;
import de.douglas.util.ExcelFileReader;
import de.douglas.util.ExtentManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@Slf4j
public class TestBase {
	public String testName = "";
	public Executor executor;
	public Map<String, Object> executionSpecifications = new TreeMap<>();
	Properties properties = new Properties();

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

	@BeforeTest
	public void beforeTest() {
		testName = this.getClass().getSimpleName();
	}

	@DataProvider
	public Object[] getData() {
		ExcelFileReader excel = new ExcelFileReader(Executor.class.getResource("/" + Constants.TEST_DATA_EXCEL_FILE_NAME));
		int rowCount = excel.getRowCount(testName) - 1;
		Object[] data = new Object[rowCount];
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			int rowNum = rowIndex + 2;
			Map<String, String> rowData = new TreeMap<>();
			for (String colName : excel.getCellsFromRow(testName, 1).values()) {
				rowData.put(colName, excel.getCellValue(testName, rowNum, colName));
			}
			data[rowIndex] = rowData;
		}
		System.out.println(Arrays.toString(data));
		return data;
	}

	@BeforeSuite
	public void beforeSuite() {
		initialize();
	}

	@AfterTest
	public void afterTest() {
		ExtentManager.getInstance().flush();
	}
}
