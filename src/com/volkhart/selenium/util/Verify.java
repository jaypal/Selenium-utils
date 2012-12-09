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

import com.volkhart.selenium.util.report.Event;
import com.volkhart.selenium.util.report.Reporter;

/**
 * @author Marius Volkhart
 */
public class Verify {

    public static boolean verifyEquals(boolean expected, boolean actual) {
        return verifyEquals("Verify", "Expected (" + expected + "), actual (" + actual + ")",
                expected, actual);
    }

    public static boolean verifyEquals(char expected, char actual) {
        return verifyEquals("Verify", "Expected (" + expected + "), actual (" + actual + ")",
                expected, actual);
    }

    public static boolean verifyEquals(Object expected, Object actual) {
        return verifyEquals("Verify", expected, actual);
    }

    public static boolean verifyEquals(double expected, double actual, double delta) {
        return verifyEquals("Verify", "Expected (" + expected + "), actual (" + actual + ")",
                expected, actual, delta);
    }

    public static boolean verifyEquals(int expected, int actual) {
        return verifyEquals("Verify", "Expected (" + expected + "), actual (" + actual + ")",
                expected, actual);
    }

    public static boolean verifyEquals(String title, boolean expected, boolean actual) {
        return verifyEquals(title, "Expected (" + expected + "), actual (" + actual + ")",
                expected, actual);
    }

    public static boolean verifyEquals(String title, char expected, char actual) {
        return verifyEquals(title, "Expected (" + expected + "), actual (" + actual + ")",
                expected, actual);
    }

    public static boolean verifyEquals(String title, Object expected, Object actual) {
        String expectedText;
        String actualText;
        expectedText = (expected == null) ? "<b>null</b>" : expected.toString();
        actualText = (actual == null) ? "<b>null</b>" : actual.toString();
        
        return verifyEquals(title, "Expected Object (" + expectedText + "), actual Object ("
                + actualText + ")", expected, actual);
    }

    public static boolean verifyEquals(String title, double expected, double actual, double delta) {
        return verifyEquals(title, "Expected (" + expected + "), actual (" + actual + ")",
                expected, actual, delta);
    }

    public static boolean verifyEquals(String title, int expected, int actual) {
        return verifyEquals(title, "Expected (" + expected + "), actual (" + actual + ")",
                expected, actual);
    }

    public static boolean verifyEquals(String title, String message, boolean expected,
            boolean actual) {
        if (expected == actual) {
            Reporter.get().add(new Event(title, message, Event.Status.PASS));
            return true;
        } else {
            Reporter.get().add(new Event(title, message, Event.Status.FAIL));
            return false;
        }
    }

    public static boolean verifyEquals(String title, String message, char expected, char actual) {
        if (expected == actual) {
            Reporter.get().add(new Event(title, message, Event.Status.PASS));
            return true;
        } else {
            Reporter.get().add(new Event(title, message, Event.Status.FAIL));
            return false;
        }
    }

    public static boolean verifyEquals(String title, String message, Object expected, Object actual) {
        if (expected == null && actual == null) {
            Reporter.get().add(new Event(title, message, Event.Status.PASS));
            return true;
        } else if (expected != null && expected.equals(actual)) {
            Reporter.get().add(new Event(title, message, Event.Status.PASS));
            return true;
        } else {
            Reporter.get().add(new Event(title, message, Event.Status.FAIL));
            return false;
        }
    }

    public static boolean verifyEquals(String title, String message, double expected,
            double actual, double delta) {
        if (expected == actual) {
            Reporter.get().add(new Event(title, message, Event.Status.PASS));
            return true;
        } else if (Math.abs(expected - actual) <= delta) {
            Reporter.get().add(new Event(title, message, Event.Status.PASS));
            return true;
        } else {
            Reporter.get().add(new Event(title, message, Event.Status.FAIL));
            return false;
        }
    }

    public static boolean verifyEquals(String title, String message, int expected, int actual) {
        if (expected == actual) {
            Reporter.get().add(new Event(title, message, Event.Status.PASS));
            return true;
        } else {
            Reporter.get().add(new Event(title, message, Event.Status.FAIL));
            return false;
        }
    }

    // public static boolean verifyEquals(String title, String message, List<?>
    // obj1, List<?> obj2) {
    // Reporter reporter = Reporter.get();
    // if (obj1 == null && obj2 == null) {
    // // Pass
    // } else if ((obj1 != null && obj2 == null) || (obj1 == null && obj2 !=
    // null)) {
    // // Fail
    // } else {
    // if (obj1.size() != obj2.size()) {
    // reporter.add(new Event(title, "obj1 has size (" + obj1.size()
    // + ") and obj2 has size (" + obj2.size() + ").", Event.Status.FAIL));
    // }
    // }
    //
    // return false;
    // }

    public static boolean verifyFalse(boolean condition) {
        return verifyFalse("Expected <b>false</b> and found (" + condition + ").", condition);
    }

    public static boolean verifyFalse(String message, boolean condition) {
        return verifyTrue(message, !condition);
    }

    public static boolean verifyNotNull(Object obj) {
        return verifyNotNull("Verify not null", obj);
    }

    public static boolean verifyNotNull(String message, Object obj) {
        return verifyTrue(message, obj != null);
    }

    public static boolean verifyNotSame(Object obj1, Object obj2) {
        return verifyNotSame("", obj1, obj2);
    }

    public static boolean verifyNotSame(String message, Object obj1, Object obj2) {
        boolean toReturn;
        Event event;
        if (obj1 == null && obj2 == null) {
            event = new Event("Verify not the same", message, Event.Status.FAIL);
            toReturn = false;
        } else if (obj1 != null && obj1.equals(obj2)) {
            event = new Event("Verify not the same", message, Event.Status.FAIL);
            toReturn = false;
        } else {
            event = new Event("Verify not the same", message, Event.Status.PASS);
            toReturn = true;
        }
        Reporter.get().add(event);
        return toReturn;
    }

    public static boolean verifyTrue(boolean condition) {
        return verifyTrue("Expected <b>true</b> and found (" + condition + ").", condition);
    }

    public static boolean verifyTrue(String message, boolean condition) {
        Event event;
        if (condition) {
            event = new Event("Verify true", message, Event.Status.PASS);
        } else {
            event = new Event("Verify true", message, Event.Status.FAIL);
        }
        Reporter.get().add(event);
        return condition;
    }

    public static void fail(String message) {
        fail("Fail", message);
    }

    public static void fail(String title, String message) {
        Reporter.get().add(new Event(title, message, Event.Status.FAIL));
    }

    public static void failNotEquals(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null) {
            formatted = message + " ";
        }
        fail(formatted + "expected :< " + expected + " > was not:< " + actual + " >");
    }

    public static void failNotSame(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null) {
            formatted = message + " ";
        }
        fail(formatted + "expected same:< " + expected + " > was not:< " + actual + " >");
    }

    public static void failSame(String message) {
        String formatted = "";
        if (message != null) {
            formatted = message + " ";
        }
        fail(formatted + "expected not same");
    }

    public static void pass(String title, String message) {
        Reporter.get().add(new Event(title, message, Event.Status.PASS));
    }

    public static void pass(String message) {
        pass("Pass", message);
    }

}
