/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.mmbase.util;

import java.io.*;

/**
 * Oddly enough, Java does not provide this itself. Code is nearly identical to
 * java.io.StringWriter. Code is also a near copy of {@link StringBufferWriter}.
 * @see java.io.StringWriter
 *
 * @author	Michiel Meeuwissen
 * @since	MMBase-1.9
 * @version $Id: StringBuilderWriter.java 41036 2010-02-15 22:30:54Z michiel $
 */
public class StringBuilderWriter extends Writer {

    protected StringBuilder buffer;

    /**
     * Create a new StringBufferWriter
     * @param buffer The StringBuffer to use
     * @throws java.lang.NullPointerException if <code>buffer</code> is null.
     */
    public StringBuilderWriter(StringBuilder buffer) {
        if (buffer == null) throw new NullPointerException("Buffer may not be null");
        this.buffer = buffer;
        lock = buffer;
    }

    /**
     * Write a single character.
     */
    public void write(int c) {
        buffer.append((char) c);
    }

    /**
     * Write a portion of an array of characters.
     *
     * @param  charArray  Array of characters
     * @param  offset   Offset from which to start writing characters
     * @param  length   Number of characters to write
     */
    public void write(char charArray[], int offset, int length) {
        if ((offset < 0) || (offset > charArray.length) || (length < 0) ||
            ((offset + length) > charArray.length) || ((offset + length) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (length == 0) {
            return;
        }
        buffer.append(charArray, offset, length);
    }

    /**
     * Write a string.
     */
    public void write(String str) {
        buffer.append(str);
    }

    /**
     * Write a portion of a string.
     *
     * @param  str  String to be written
     * @param  offset  Offset from which to start writing characters
     * @param  length  Number of characters to write
     */
    public void write(String str, int offset, int length)  {
        buffer.append(str.substring(offset, offset + length));
    }

    /**
     * Return the buffer's current value as a string.
     */
    public String toString() {
        return buffer.toString();
    }

    /**
     * Return the string buffer itself.
     *
     * @return StringBuilder holding the current buffer value.
     */
    public StringBuilder getBuffer() {
        return buffer;
    }

    /**
     * Flush the stream.
     */
    public void flush() {
    }

    /**
     * Closing a <tt>StringBuilderWriter</tt> has no effect. The methods in this
     * class can be called after the stream has been closed without generating
     * an <tt>IOException</tt>.
     */
    public void close() throws IOException {
    }

}
