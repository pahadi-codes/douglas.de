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
	LandingPage landingPage;

	@Test(dataProvider = "getData")
	public void testProductFilter(Map<String, String> data) {
		executor = new Executor(testName, executionSpecifications);
		executor.log(Status.INFO, "Test Data: " + data);
		try {
			landingPage = new LandingPage(executor);
			landingPage
					.verifyAndAcceptCookiesConsentIfAvailable()
					.openParfumPage()
					.verifyParfumPage()
					.applyMarkeFilter(data.get("Marke"))
					.applyProduktartFilter(data.get("Produktart"))
					.applyGeschenkFurFilter(data.get("Geschenk für"))
					.applyFurWenFilter(data.get("Für Wen"))
					.getCriteriaSpecificProducts(data.get("Criteria"))
			;
		} catch (Throwable exception) {
			executor.logFailureException(exception);
			throw exception;
		} finally {
			executor.safelyCloseAllBrowserInstances();
		}
	}
}
