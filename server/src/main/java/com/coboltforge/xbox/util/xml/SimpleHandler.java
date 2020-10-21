//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.coboltforge.xbox.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class SimpleHandler extends DefaultHandler {
    private String m_current_element_name;
    private StringBuffer m_current_element_contents;
    private Attributes m_current_attributes;
    private ISimpleElement m_element;
    private IHandlerStack m_impl;
    public static AttributesImpl m_empty = new AttributesImpl();

    public SimpleHandler(IHandlerStack var1, ISimpleElement var2) {
        this.m_impl = var1;
        this.m_element = var2;
        this.m_current_element_contents = new StringBuffer();
    }

    public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
        this.m_current_element_name = var2;
        this.m_current_element_contents.setLength(0);
        AttributesImpl var5 = new AttributesImpl();
        this.m_current_attributes = var5;
        int var6 = var4.getLength();

        for(int var7 = 0; var7 < var6; ++var7) {
            var5.addAttribute(var4.getURI(var7), var4.getLocalName(var7), var4.getQName(var7), var4.getType(var7), var4.getValue(var7));
        }

    }

    public void endElement(String var1, String var2, String var3) throws SAXException {
        this.m_element.gotElement(this.m_current_element_name, this.m_current_element_contents.toString(), this.m_current_attributes);
        if (this.m_impl != null) {
            this.m_impl.popHandlerStack();
        }

    }

    public void characters(char[] var1, int var2, int var3) {
        this.m_current_element_contents.append(var1, var2, var3);
    }

    public void ignorableWhitespace(char[] var1, int var2, int var3) {
        this.m_current_element_contents.append(var1, var2, var3);
    }

    String getCurrentElementName() {
        return this.m_current_element_name;
    }

    StringBuffer getContents() {
        return this.m_current_element_contents;
    }

    Attributes getAttributes() {
        return this.m_current_attributes;
    }

    public static void writeElement(ContentHandler var0, String var1, String var2) throws SAXException {
        var0.startElement("", var1, "", m_empty);
        char[] var3 = var2.toCharArray();
        var0.characters(var3, 0, var3.length);
        var0.endElement("", var1, "");
    }
}
