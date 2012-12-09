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

package com.volkhart.selenium.util.report;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Captures a snapshot of the page loaded in the browser. This is not what is
 * displayed on the screen, but the entire Webpage. The snapshot is stored to
 * file.
 * 
 * @author Marius Volkhart
 */
public class Screenshot {

    /**
     * The file extension used for captured images
     */
    static final String EXTENSION = ".png";

    /**
     * The directory under which images are stored.
     */
    static final String DIRECTORY_SUFFIX = "_screenshots";

    private TakesScreenshot mCamera;

    /**
     * The String is the path to where the final screenshot will be found when
     * all reporting is done. The File is the representation of the actual
     * screenshot.
     */
    private Map<String, File> mFiles = new HashMap<String, File>();

    // Ensure that each thread only has a single Screenshot
    private static ThreadLocal<Screenshot> sScreenshot = new ThreadLocal<Screenshot>() {

        @Override
        protected Screenshot initialValue() {
            return new Screenshot();
        }
    };

    // Need private constructor to prevent initialization
    private Screenshot() {
    }

    /**
     * Returns a thread-specific Screenshot instance.
     * 
     * @return The thread-specific Screenshot instance
     */
    public static Screenshot get() {
        return sScreenshot.get();
    }

    /**
     * Sets the {@link WebDriver} from which this Screenshot will capture images
     * 
     * @param driver The WebDriver instance
     */
    public void setWebDriver(WebDriver driver) {
        mCamera = (TakesScreenshot) new Augmenter().augment(driver);
    }

    /**
     * Captures the snapshot of the webpage.
     * 
     * @return The file path to where the image is stored starting with the
     *         non-specific screenshot directory
     */
    public String take() {
        File srcFile = mCamera.getScreenshotAs(OutputType.FILE);
        String path = getRelativeFilePath(srcFile);
        mFiles.put(path, srcFile);
        return path;
    }

    void generateReport(String path) {

        // Delete the directory that contains the images so if re-running a test
        // we don't keep images from the previous test
        File oldImgDir = new File(path + DIRECTORY_SUFFIX);
        if (oldImgDir.exists()) {
            try {
                FileUtils.deleteDirectory(oldImgDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String filePath : mFiles.keySet()) {
            File file = mFiles.get(filePath);
            try {
                // We copy since moving across file systems sometimes causes
                // problems, particularly on unix machines.
                FileUtils.copyFile(file, new File(path + filePath), true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // Delete regardless to avoid loose files
            file.delete();
        }
    }

    /**
     * Returns the filepath starting with the screenshot directory
     */
    private String getRelativeFilePath(File file) {
        return DIRECTORY_SUFFIX + File.separator + UUID.randomUUID().toString()
                + EXTENSION;

    }

}
