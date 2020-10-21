//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public interface IHandlerStack {
    void pushHandlerStack(DefaultHandler var1);

    void startWithHandler(DefaultHandler var1, String var2, String var3, String var4, Attributes var5) throws SAXException;

    void popHandlerStack();
}
