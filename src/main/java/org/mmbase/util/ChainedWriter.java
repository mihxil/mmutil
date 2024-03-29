/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.mmbase.util;

import java.io.*;
import java.util.*;

/**
 * Like unix' 'tee'. Delegates all write operation to several others.
 *
 * Alternative names: WriterChain, TeeWriter, WriterTee
 *
 * @author	Michiel Meeuwissen
 * @since	MMBase-1.9
 * @version $Id: ChainedWriter.java 44739 2011-01-12 08:52:31Z michiel $
 */
public class ChainedWriter extends Writer {

    private final List<Writer> writers = new ArrayList<Writer>();
    public ChainedWriter(Writer... ls) {
        for (Writer w : ls) {
            addWriter(w);
        }
    }

    public final ChainedWriter addWriter(Writer w) {
        writers.add(w);
        return this;
    }

    @Override
    public Writer append(char c) throws IOException {
        for (Writer w : writers) {
            w.append(c);
        }
        return this;
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        for (Writer w : writers) {
            w.append(csq);
        }
        return this;
    }
    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        for (Writer w : writers) {
            w.append(csq, start, end);
        }
        return this;
    }
    @Override
    public  void close() throws IOException {
        for (Writer w : writers) {
            w.close();
        }
    }
    @Override
    public void flush() throws IOException {
        for (Writer w : writers) {
            w.flush();
        }
    }
    @Override
    public void write(char[] cbuf) throws IOException {
        for (Writer w : writers) {
            w.write(cbuf);
        }
    }
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (Writer w : writers) {
            w.write(cbuf, off, len);
        }
    }
    @Override
    public void write(int c) throws IOException {
        for (Writer w : writers) {
            w.write(c);
        }
    }
    @Override
    public void write(String str, int off, int len) throws IOException {
        for (Writer w : writers) {
            w.write(str, off, len);
        }
    }

}
