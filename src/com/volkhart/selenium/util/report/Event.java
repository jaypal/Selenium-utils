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

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a way to represent key test points to the user in a descriptive way
 * that is useful for debugging and analysis.
 * 
 * @author Marius Volkhart
 */
public class Event {

    private final String mMessage;
    private final String mTitle;
    private final Status mStatus;
    private final List<String> mStackTrace;
    private final String mScreenshotFilePath;

    public static enum Status {
        PASS, DONE, WARNING, FAIL
    };

    /**
     * Creates a new Event. Events are used to represent key points during the
     * test that need to be reported to the user.
     * 
     * @param title The title to be displayed for the event
     * @param message A more detailed description of what occurred
     * @param status The status of this Event.
     */
    public Event(String title, String message, Status status) {

        mTitle = Preconditions.checkNotNull(title);
        mMessage = Preconditions.checkNotNull(message);
        mStatus = status;

        // Only record screenshots and stack traces for failures & warnings
        if (status == Status.FAIL || status == Status.WARNING) {
            mScreenshotFilePath = Screenshot.get().take();

            // Turn the StackTrace into a String[] for smaller footprint &
            // easier handling.
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            int elementsSize = elements.length - 1;
            mStackTrace = new ArrayList<String>();
            for (int i = 0; i < elementsSize; i++) {
                String trace = elements[i + 1].toString();
                if (!trace.startsWith("java.")
                        && !trace.startsWith("com.volkhart.selenium.framework")) {
                    mStackTrace.add(trace);
                }

            }
        } else {
            mScreenshotFilePath = null;
            mStackTrace = null;
        }

    }

    Status getStatus() {
        return mStatus;
    }

    String getMessage() {
        return mMessage;
    }

    String getTitle() {
        return mTitle;
    }

    String[] getStackTrace() {
        return mStackTrace.toArray(new String[0]);
    }

    String getScreenshotFilePath() {
        return mScreenshotFilePath;
    }

}
