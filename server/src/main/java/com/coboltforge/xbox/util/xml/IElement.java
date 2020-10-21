//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.coboltforge.xbox.util.xml;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public interface IElement {
    DefaultHandler readFromXML(IHandlerStack var1);

    void writeToXML(ContentHandler var1) throws SAXException;

    String getElementTag();
}
