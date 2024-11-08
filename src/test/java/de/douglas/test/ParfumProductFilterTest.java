package de.douglas.test;

import com.aventstack.extentreports.Status;
import de.douglas.Executor;
import de.douglas.TestBase;
import de.douglas.pages.LandingPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.Map;

@Slf4j
public class ParfumProductFilterTest extends TestBase {
	@Test(dataProvider = "getData")
	public void testProductFilter(Map<String, String> data) {
		executor = new Executor(testName, executionSpecifications);
		executor.log(Status.INFO, "Test Data: " + data);
		try {
			new LandingPage(executor)
					.verifyAndAcceptCookiesConsentIfAvailable()
					.openParfumPage()
					.verifyParfumPage()
					.applyMarkeFilter(data.get("Marke"))
					.applyProduktartFilter(data.get("Produktart"))
					.applyGeschenkFurFilter(data.get("Geschenk für"))
					.applyFurWenFilter(data.get("Für Wen"))
					.waitForLoaderToDisappear()
					.getCriteriaSpecificProducts(data.get("Criteria"))
			;
			executor.log(Status.PASS, "Test Execution Completed");
		} catch (Throwable exception) {
			executor.logFailureException(exception);
			throw exception;
		} finally {
			executor.safelyCloseAllBrowserInstances();
		}
	}
}
