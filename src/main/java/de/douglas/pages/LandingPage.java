package de.douglas.pages;

import com.aventstack.extentreports.Status;
import de.douglas.Executor;
import de.douglas.enums.AlternativeClickType;
import org.openqa.selenium.By;
import org.testng.Assert;

public class LandingPage {
	Executor executor;
	public LandingPage(Executor executor) {
		this.executor = executor;
	}

	public LandingPage verifyAndAcceptCookiesConsentIfAvailable() {
		try {
			Assert.assertTrue(executor.isElementPresent(Objects.SHADOW_ROOT_COOKIES_CONSENT.by), "Cookie consent pop-up is present");
			executor.log(Status.INFO, "Cookies Consent Pop-up is present");
			executor.click(Objects.SHADOW_ROOT_COOKIES_CONSENT.by, Objects.BUTTON_ACCEPT_ALL.by);
		} catch (Exception exception) {
			executor.log(Status.WARNING, "Cookie consent pop-up is not present");
		}
		return this;
	}

	public ParfumListingPage openParfumPage() {
		executor.click(Objects.LINK_NAVIGATION_MENU_PARFUM.by, AlternativeClickType.JAVASCRIPT);
		return new ParfumListingPage(executor);
	}

	enum Objects {
		SHADOW_ROOT_COOKIES_CONSENT(By.cssSelector("div#usercentrics-root")),
		BUTTON_ACCEPT_ALL(By.cssSelector("button[data-testid='uc-accept-all-button']")),
		LINK_NAVIGATION_MENU_PARFUM(By.xpath("//a[contains(@class, 'navigation-main')][contains(.,'PARFUM')]"))
		;
		public final By by;
		Objects(By by) {
			this.by = by;
		}
	}
}
