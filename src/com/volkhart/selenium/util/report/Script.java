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
 * Provides a high-level way of separating tests into segments.
 * 
 * @author Marius Volkhart
 */
public class Script {

    static final String DEFAULT_TITLE = "Default Script Title";

    private final String mName;
    private ArrayList<Function> mElements;
    private Function mCurrentElement;

    Script(String name) {

        // Check for null since name won't be used until report is written at
        // which point failure will be untimely and hard to trace
        mName = Preconditions.checkNotNull(name);
        mElements = new ArrayList<Function>();
    }

    boolean add(Function e) {
        mCurrentElement = e;
        return mElements.add(e);
    }

    boolean add(Event e) {
        if (mCurrentElement == null) {
            add(new Function(Function.DEFAULT_TITLE));
        }
        return mCurrentElement.add(e);
    }

    Iterable<Function> getFunctions() {
        return ImmutableList.copyOf(mElements);
    }

    String getName() {
        return mName;
    }

    int getFailureCount() {
        int count = 0;
        for (Function e : mElements) {
            count += e.getFailureCount();
        }
        return count;
    }

    int getWarningCount() {
        int count = 0;
        for (Function e : mElements) {
            count += e.getWarningCount();
        }
        return count;
    }

}
