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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides the framework for executing multiple, concurrent tests.
 * 
 * @author Marius Volkhart
 */
public class Execution {

    /**
     * Handles the threads. ExecutorService is used to improve performance.
     */
    private static ExecutorService sExecutor;
    private static CompletionService<Boolean> sCompletionService;
    private static List<Test.Builder> sBuilders = new ArrayList<Test.Builder>();
    private static boolean sStarted = false;

    public static void start() {
        setUp();
        sStarted = true;

        // Submit builders on independent threads so thread-local variables are
        // safe
        for (Test.Builder builder : sBuilders) {
            sCompletionService.submit(builder);
        }

        // Make sure all tests have completed before continuing
        for (int i = 0, max = sBuilders.size(); i < max; i++) {
            try {
                sCompletionService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean queue(Test.Builder testBuilder) {
        boolean toReturn = false;
        if (!sStarted) {
            toReturn = sBuilders.add(testBuilder);
        }
        return toReturn;
    }

    /**
     * Provides logic that will run before the start of <b>any</b> tests.
     */
    private static void setUp() {

        sExecutor = Executors.newCachedThreadPool();
        sCompletionService = new ExecutorCompletionService<Boolean>(sExecutor);

    }

}
