package de.douglas.util;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
public class ExtentManager {
	static ExtentReports extent;
	public static ExtentReports getInstance() {
		if (extent == null) {
			initializeExtentReports();
		}
		return extent;
	}

	private static void initializeExtentReports() {
		extent = new ExtentReports();
		ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(Constants.EXTENT_REPORT_FILE_NAME);
		extent.attachReporter(extentSparkReporter);
	}
}
