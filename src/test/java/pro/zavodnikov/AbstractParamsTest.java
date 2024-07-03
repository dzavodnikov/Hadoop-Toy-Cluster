/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023-2025 Dmitry Zavodnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pro.zavodnikov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * Testing methods from {@link AbstractParams}.
 */
public class AbstractParamsTest {

    private String[] arr(final String... args) {
        return args;
    }

    private void assertParse(final String[] expected, final String line, final String separator,
            final int expectedLen) {
        assertArrayEquals(expected, AbstractParams.split(line, separator, expectedLen, "Wrong format"));
    }

    @Test
    public void testSplit() {
        assertParse(arr("MyTable1", "-"), "MyTable1, -", ",", -1);
        assertParse(arr("MyTable1", "-"), "MyTable1, -", ",", 0);
        assertParse(arr("MyTable1", "-"), "MyTable1, -", ",", 2);

        assertParse(arr("MyTable2", "a", "b"), "MyTable2, a  ,b", ",", 3);

        assertParse(arr("-", "column1"), "-:column1", ":", 2);
    }

    private void assertTail(final String[] expected, final String[] src, final int startIdx) {
        assertArrayEquals(expected, AbstractParams.tail(src, startIdx));
    }

    @Test
    public void testTail() {
        assertTail(arr(), arr(), 0);
        assertTail(arr(), arr(), 1);

        assertTail(arr("a"), arr("a"), 0);
        assertTail(arr(), arr("a"), 1);

        assertTail(arr("a", "b"), arr("a", "b"), 0);
        assertTail(arr("b"), arr("a", "b"), 1);
        assertTail(arr(), arr("a", "b"), 2);
    }
}
