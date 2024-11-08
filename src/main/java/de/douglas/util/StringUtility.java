package de.douglas.util;

import org.openqa.selenium.By;

public class StringUtility {
	public static By getUpdatedLocatedBy(String locator) {
		String byCssSelector = "By.cssSelector:";
		String byXPath = "By.xpath:";
		if (locator.contains(byCssSelector)) {
			return By.cssSelector(locator.replace(byCssSelector, "").trim());
		} else if (locator.contains(byXPath)) {
			return By.xpath(locator.replace(byXPath, "").trim());
		}
		return null;
	}
}
