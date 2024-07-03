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

import java.util.Arrays;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Class that useful for parameter initialization.
 *
 * Just add fields with {@link Parameter} annotation and call constructor.
 */
public abstract class AbstractParams {

    @Parameter(names = { "--help", "-h" }, help = true, description = "Print help.")
    protected boolean help = false;

    /**
     * Parse value to fragments by separators. Strip every fragment after.
     */
    public static String[] split(final String value, final String separator, final int expectedLen,
            final String errorMessage) {
        if (separator == null || separator.isEmpty()) {
            throw new IllegalArgumentException("Separator should not be null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value should not be null");
        }

        final String[] result = Arrays.stream(value.split(separator)).map(f -> f.trim()).filter(f -> !f.isEmpty())
                .toArray(String[]::new);
        if (expectedLen > 0 && expectedLen != result.length) {
            throw new IllegalArgumentException(errorMessage);
        }
        return result;
    }

    /**
     * Return tail of list since some position.
     */
    public static String[] tail(final String[] src, final int startIdx) {
        if (startIdx > src.length - 1) {
            return new String[0];
        }

        return Arrays.copyOfRange(src, startIdx, src.length);
    }

    protected void print(final String format, final Object... args) {
        System.out.print(String.format(format, args));
    }

    protected void println(final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    protected void customParametersProcess() {
    }

    /**
     * Initialized parameters. <strong>Do not call it in constructor!</strong>
     */
    public void init(final String[] args) {
        final JCommander commander = JCommander.newBuilder().addObject(this).build();
        commander.setProgramName(getClass().getCanonicalName());
        commander.parse(args);

        customParametersProcess();

        if (this.help) {
            commander.usage();
            System.exit(0);
        }
    }
}
