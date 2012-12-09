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
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

/**
 * Provides a low-level way of separating test steps and grouping multiple
 * {@link Event} instances.
 * 
 * @author Marius Volkhart
 */
public class Function {

    static final String DEFAULT_TITLE = "Default Function Title";

    private int mFailures;
    private int mWarnings;
    private int mPasses;
    private ArrayList<Event> mEvents;
    private final String mName;

    Function(String name) {

        // Check for null since name won't be used until report is written at
        // which point failure will be untimely and hard to trace
        mName = Preconditions.checkNotNull(name);
        mFailures = 0;
        mWarnings = 0;
        mPasses = 0;
        mEvents = new ArrayList<Event>();
    }

    boolean add(Event event) {
        switch (event.getStatus()) {
            case FAIL:
                mFailures++;
                break;
            case WARNING:
                mWarnings++;
                break;
            case PASS:
                mPasses++;
                break;
            default:
                // Intentionally do nothing as we don't maintain a done counter
                break;
        }

        return mEvents.add(event);
    }

    int getFailureCount() {
        return mFailures;
    }

    int getWarningCount() {
        return mWarnings;
    }

    int getPassCount() {
        return mPasses;
    }

    String getName() {
        return mName;
    }

    Iterable<Event> getEvents() {
        return ImmutableList.copyOf(mEvents);
    }

    int size() {
        return mEvents.size();
    }

}
