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

package com.volkhart.selenium.framework;

import com.volkhart.selenium.util.Window;
import com.volkhart.selenium.util.report.Reporter;
import com.volkhart.selenium.util.report.Screenshot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Provides the container for all logic that is executed during a script. Each
 * test is its own object to make thread safety easier to manage. Utility
 * methods should synchronized and objects should be thread dependent.
 */
public class Test {

    protected WebDriver mDriver;
    protected Reporter mReporter;
    protected Screenshot mScreenshot;
    protected Wait<WebDriver> mWait;

    public Test(WebDriver driver, String browser, String title, String path) {
        mDriver = driver;
        mReporter = Reporter.get();
        mReporter.setTitle(title);
        mReporter.setOutputPath(path);
        mReporter.setBrowser(browser);
        mScreenshot = Screenshot.get();
        mScreenshot.setWebDriver(driver);

        // Set the maximum wait time here
        mWait = new WebDriverWait(driver, 10);
    }

    private void run() throws Exception {

        // Execute test setup code
        beforeTest();

        doTest();

        // Execute test close code
        afterTest();
    }

    /**
     * Provides logic that will run at the start of every test
     */
    protected void beforeTest() {

    }

    /**
     * Provides logic that will run at the end of every test assuming the code
     * is reached
     */
    protected void afterTest() throws IOException {
        mDriver.close();
        mReporter.generateReport();
    }

    protected void doTest() {

    }

    public static class Builder implements Callable<Boolean> {

        protected DesiredCapabilities mCapabilities;
        protected String mTitle;
        protected String mPath;

        public Builder(String title) {
            mTitle = title;
        }

        public void setCapabilities(DesiredCapabilities capabilities) {
            mCapabilities = capabilities;
        }

        public void setPath(String path) {
            mPath = (new File(path)).getPath();
        }

        public Test build() {
            WebDriver driver = new RemoteWebDriver(mCapabilities);
            Window.maximize(driver);
            return new Test(driver, mCapabilities.getBrowserName() + " "
                    + mCapabilities.getVersion(),
                    mTitle, mPath);
        }

        protected Test build(Test test) {
            return test;
        }

        @Override
        public Boolean call() throws Exception {
            build().run();
            return null;
        }
    }

}
