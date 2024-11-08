package de.douglas.pages;

import com.aventstack.extentreports.Status;
import de.douglas.Executor;
import de.douglas.enums.AlternativeClickType;
import de.douglas.util.StringUtility;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

@Slf4j
public class ParfumListingPage {
	Executor executor;

	public ParfumListingPage(Executor executor) {
		this.executor = executor;
	}

	public ParfumListingPage verifyParfumPage() {
		try {
			String textToVerify = executor.getText(Objects.BREADCRUMB.by);
			Assert.assertTrue(textToVerify.contains("Parfum"));
			executor.log(Status.PASS, "Breadcrumb: " + textToVerify);
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw exception;
		}
		return this;
	}

	public ParfumListingPage applyFurWenFilter(String input) {
		if (!input.isBlank()) {
			try {
				executor.click(Objects.DROPDOWN_FURWEN.by);
				executor.click(StringUtility.getUpdatedLocatedBy(Objects.CHECKBOX_SEARCH_DROPDOWN_FURWEN.by.toString().replace("SEARCH_ITEM", input)), AlternativeClickType.JAVASCRIPT);
				executor.log(Status.PASS, "Filters Applied Successfully");
			} catch (Exception exception) {
				executor.log(Status.FAIL, "Error Applying Filters");
				throw exception;
			}
		}
		return this;
	}

	public ParfumListingPage applyGeschenkFurFilter(String input) {
		if (!input.isBlank()) {
			try {
				executor.click(Objects.DROPDOWN_GESCHENKFUR.by);
				executor.sendKeys(Objects.TEXTBOX_SEARCH_DROPDOWN_GESCHENKFUR.by, input);
				executor.click(StringUtility.getUpdatedLocatedBy(Objects.CHECKBOX_SEARCH_DROPDOWN_GESCHENKFUR.by.toString().replace("SEARCH_ITEM", input)), AlternativeClickType.JAVASCRIPT);
				executor.log(Status.PASS, "Filters Applied Successfully");
			} catch (Exception exception) {
				executor.log(Status.FAIL, "Error Applying Filters");
				throw exception;
			}
		}
		return this;
	}

	public ParfumListingPage applyMarkeFilter(String input) {
		if (!input.isBlank()) {
			try {
				executor.click(Objects.DROPDOWN_MARKE.by);
				executor.sendKeys(Objects.TEXTBOX_SEARCH_DROPDOWN_MARKE.by, input);
				executor.click(StringUtility.getUpdatedLocatedBy(Objects.CHECKBOX_SEARCH_DROPDOWN_MARKE.by.toString().replace("SEARCH_ITEM", input)), AlternativeClickType.JAVASCRIPT);
				executor.log(Status.PASS, "Filters Applied Successfully");
			} catch (Exception exception) {
				executor.log(Status.FAIL, "Error Applying Filters");
				throw exception;
			}
		}
		return this;
	}

	public ParfumListingPage applyProduktartFilter(String input) {
		if (!input.isBlank()) {
			try {
				executor.click(Objects.DROPDOWN_PRODUKTART.by);
				executor.sendKeys(Objects.TEXTBOX_SEARCH_DROPDOWN_PRODUKTART.by, input);
				executor.click(StringUtility.getUpdatedLocatedBy(Objects.CHECKBOX_SEARCH_DROPDOWN_PRODUKTART.by.toString().replace("SEARCH_ITEM", input)), AlternativeClickType.JAVASCRIPT);
				executor.log(Status.PASS, "Filters Applied Successfully");
			} catch (Exception exception) {
				executor.log(Status.FAIL, "Error Applying Filters");
				throw exception;
			}
		}
		return this;
	}

	public ParfumListingPage getCriteriaSpecificProducts(String input) {
		executor.log(Status.INFO, "Getting Criteria Specific Products");
		List<WebElement> products;
		if (input.equalsIgnoreCase("Neu")) {
			products = executor.findElements(Objects.BOX_ALL_NEW_PRODUCTS.by);
		} else if (input.equalsIgnoreCase("Sale")) {
			products = executor.findElements(Objects.BOX_ALL_SALE_PRODUCTS.by);
		} else if (input.equalsIgnoreCase("Limitiert")) {
			products = executor.findElements(Objects.BOX_ALL_LIMITED_EDITION_PRODUCTS.by);
		} else {
			throw new IllegalArgumentException("Invalid Search Criteria Specified");
		}
		for (WebElement product : products) {
			executor.log(Status.INFO, "Product: <br/>" + product.getText());
		}
		return this;
	}

	public ParfumListingPage waitForLoaderToDisappear() {
		executor.isDisappeared(Objects.LOADER.by);
		return this;
	}

	enum Objects {
		SHADOW_ROOT_COOKIES_CONSENT(By.cssSelector("div#usercentrics-root")),
		BUTTON_ACCEPT_ALL(By.cssSelector("button[data-testid='uc-accept-all-button']")),
		LINK_NAVIGATION_MENU_PARFUM(By.xpath("//a[contains(@class, 'navigation-main')][contains(.,'PARFUM')]")),
		BREADCRUMB(By.cssSelector("span.breadcrumb__list")),
		DROPDOWN_MARKE(By.xpath("//div[@data-testid='brand']")),
		TEXTBOX_SEARCH_DROPDOWN_MARKE(By.xpath("//div[@data-testid='brand']/following-sibling::div[@class='facet__menu']//input[@type='text']")),
		CHECKBOX_SEARCH_DROPDOWN_MARKE(By.xpath("//div[@data-testid='brand']/following-sibling::div[@class='facet__menu']//a/div/div[text()='SEARCH_ITEM']/parent::div/preceding-sibling::span//input[@type='checkbox']")),
		DROPDOWN_GESCHENKFUR(By.xpath("//div[@data-testid='Geschenk für']")),
		TEXTBOX_SEARCH_DROPDOWN_GESCHENKFUR(By.xpath("//div[@data-testid='Geschenk für']/following-sibling::div[@class='facet__menu']//input[@type='text']")),
		CHECKBOX_SEARCH_DROPDOWN_GESCHENKFUR(By.xpath("//div[@data-testid='Geschenk für']/following-sibling::div[@class='facet__menu']//a/div/div[text()='SEARCH_ITEM']/parent::div/preceding-sibling::span//input[@type='checkbox']")),
		DROPDOWN_PRODUKTART(By.xpath("//div[@data-testid='classificationClassName']")),
		TEXTBOX_SEARCH_DROPDOWN_PRODUKTART(By.xpath("//div[@data-testid='classificationClassName']/following-sibling::div[@class='facet__menu']//input[@type='text']")),
		CHECKBOX_SEARCH_DROPDOWN_PRODUKTART(By.xpath("//div[@data-testid='classificationClassName']/following-sibling::div[@class='facet__menu']//a/div/div[text()='SEARCH_ITEM']/parent::div/preceding-sibling::span//input[@type='checkbox']")),
		BUTTON_SAVE_CHANGES_DROPDOWN_PRODUKTART(By.xpath("//div[@data-testid='classificationClassName']/following-sibling::div[@class='facet__menu']/div/button")),
		DROPDOWN_FURWEN(By.xpath("//div[@data-testid='gender']")),
		CHECKBOX_SEARCH_DROPDOWN_FURWEN(By.xpath("//div[@data-testid='gender']/following-sibling::div[@class='facet__menu']//a/div/div[text()='SEARCH_ITEM']/parent::div/preceding-sibling::span//input[@type='checkbox']")),
		LOADER(By.cssSelector(".page-loader-wrapper")),
		BOX_ALL_NEW_PRODUCTS(By.xpath("//div[contains(@class,'product-tile')]//*[contains(@class,'eyecatcher--new')]/parent::div/parent::div/following-sibling::div[contains(@class,'details-container')]")),
		BOX_ALL_SALE_PRODUCTS(By.xpath("//div[contains(@class,'product-tile')]//*[contains(@class,'eyecatcher--discount')]/parent::div/parent::div/following-sibling::div[contains(@class,'details-container')]")),
		BOX_ALL_LIMITED_EDITION_PRODUCTS(By.xpath("//div[contains(@class,'product-tile')]//following-sibling::div[contains(@class,'details-container')]//div[contains(@class,'name')][contains(.,'Edition')]")),
		;
		public final By by;

		Objects(By by) {
			this.by = by;
		}
	}
}
