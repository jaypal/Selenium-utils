/*
 * Copyright (C) 2012 Marius Volkhart
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.volkhart.selenium.util;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.awt.Toolkit;
import java.util.Set;

/**
 * Provides window interactions on existing WebDriver objects.
 * @author Marius Volkhart
 */
public class Window {

	/**
	 * Maximizes the browser window.
	 * 
	 * @param driver
	 *            WebDriver instance being maximized
	 */
	public static void maximize(WebDriver driver) {
		Toolkit t = Toolkit.getDefaultToolkit();
		java.awt.Dimension screenSize = t.getScreenSize();
		Dimension screenRes = new Dimension(screenSize.width, screenSize.height);
		driver.manage().window().setSize(screenRes);
	}

	/**
	 * Switches the WebDriver pointer to the specified window
	 * 
	 * Uses {@code String.contains()} to identify windows.
	 * 
	 * @param driver
	 *            WebDriver being redirected
	 * @param partialTitle
	 *            A portion of the title of the window.
	 */
	public static void switchTo(WebDriver driver, String partialTitle) {
		Set<String> windows = driver.getWindowHandles();
		for (String s : windows) {
			driver.switchTo().window(s);
			if (driver.getTitle().toLowerCase().contains(partialTitle.toLowerCase()))
				break;
		}
	}

	/**
	 * Provides a way of doing dynamic waits
	 * 
	 * @param webElement
	 *            The WebElement being waited on
	 * @return Is not used but required by API
	 */
	public static ExpectedCondition<WebElement> isVisible(final WebElement webElement) {
		return new ExpectedCondition<WebElement>() {

			@Override
			public WebElement apply(WebDriver arg0) {
				if (webElement.isDisplayed())
					return webElement;
				return null;
			}
		};
	}

	/**
	 * Provides a way of doing dynamic waits
	 * 
	 * @param locator
	 *            the method by which the WebElement is identified
	 * @return The WebElement once found
	 */
	public static ExpectedCondition<WebElement> isVisible(final By locator) {
		return new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver sDriver) {
				WebElement toReturn = sDriver.findElement(locator);
				if (toReturn.isDisplayed()) {
					return toReturn;
				}
				return null;
			}
		};
	}

}
