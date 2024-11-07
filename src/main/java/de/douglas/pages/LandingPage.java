package de.douglas.pages;

import de.douglas.Executor;
import org.openqa.selenium.By;

public class LandingPage {
	Executor executor;
	public LandingPage(Executor executor) {
		this.executor = executor;
	}

	public LandingPage acceptCookiesConsentIfAvailable() {
		executor.click(Objects.SHADOW_ROOT_COOKIES_CONSENT.by, Objects.BUTTON_ACCEPT_ALL.by);
		return this;
	}

	public void openParfum() {
		executor.clickUsingJavascript(Objects.LINK_NAVIGATION_MENU_PARFUM.by);
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
