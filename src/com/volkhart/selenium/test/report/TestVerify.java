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

package com.volkhart.selenium.test.report;

import static org.junit.Assert.fail;

import com.volkhart.selenium.report.Reporter;
import com.volkhart.selenium.util.Verify;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestVerify {

    private static Reporter mReporter;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        mReporter = Reporter.get();
        mReporter.setTitle("JUnit Test");
        mReporter.setScript(Verify.class.getName());
        mReporter.setOutputPath("/home/marius/testVerify.html");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        mReporter.generateReport();
    }

    @Test
    public void testVerifyEqualsStringBooleanBoolean() {
        mReporter.setFunction("Booleans");
        Assert.assertFalse("true false", Verify.verifyEquals(true, false));
        Assert.assertTrue("true true", Verify.verifyEquals(true, true));
        Assert.assertTrue("false false", Verify.verifyEquals(false, false));
        Assert.assertFalse("true false", Verify.verifyEquals(false, true));

        Assert.assertFalse("\"message\" true false", Verify.verifyEquals("message", true, false));
        Assert.assertTrue("true true", Verify.verifyEquals(null, true, true));
    }

    @Test
    public void testVerifyEqualsStringCharChar() {
        mReporter.setFunction("Chars");
        Assert.assertTrue("c, c", Verify.verifyEquals('c', 'c'));
        Assert.assertTrue("5, 5", Verify.verifyEquals('5', '5'));
        Assert.assertTrue("#, #", Verify.verifyEquals('#', '#'));
        Assert.assertFalse("c, d", Verify.verifyEquals('c', 'd'));

        Assert.assertTrue("\"message\" c, c", Verify.verifyEquals("message", 'c', 'c'));
        Assert.assertTrue("5, 5", Verify.verifyEquals(null, '5', '5'));
    }

    @Test
    public void testVerifyEqualsStringObjectObject() {
        mReporter.setFunction("Objects");
        Assert.assertTrue("\"abc\", \"abc\"", Verify.verifyEquals("abc", "abc"));
        Assert.assertFalse("\"bcd\", \"abc\"", Verify.verifyEquals("bcd", "abc"));
        Assert.assertFalse("null, \"abc\"", Verify.verifyEquals(null, "abc"));
        Assert.assertFalse("\"abc\", null", Verify.verifyEquals("abc", null));
        Assert.assertTrue("null null", Verify.verifyEquals(null, null));

        Assert.assertTrue("message, \"abc\", \"abc\"", Verify.verifyEquals("message", "abc", "abc"));
    }

    @Test
    public void testVerifyEqualsStringDoubleDoubleDouble() {
        fail("Not yet implemented");
    }

    @Test
    public void testVerifyEqualsStringIntInt() {
        fail("Not yet implemented");
    }

}
