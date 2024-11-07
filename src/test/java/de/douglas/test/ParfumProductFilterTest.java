package de.douglas.test;
import com.aventstack.extentreports.Status;
import de.douglas.Executor;
import de.douglas.TestBase;
import de.douglas.pages.LandingPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class ParfumProductFilterTest extends TestBase {
	LandingPage landingPage;
	@Test
	public void testProductFilter() {
		executor = new Executor(testName, executionSpecifications);
		try {
			log.info("ProductFilterTest.testProductFilter");
			executor.log(Status.INFO, executor.getPageTitle());
			landingPage = new LandingPage(executor);
			landingPage.acceptCookiesConsentIfAvailable().openParfum();
		} catch (Throwable exception) {
			executor.logFailureException(exception);
			throw exception;
		} finally {
			executor.safelyCloseAllBrowserInstances();
		}
	}
}
