//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.coboltforge.xbox.android.contentxml;

import android.content.ContentValues;

import com.coboltforge.xbox.util.xml.SimpleHandler;
import com.coboltforge.xbox.util.xml.IElement;
import com.coboltforge.xbox.util.xml.IHandlerStack;
import com.coboltforge.xbox.util.xml.ISimpleElement;
import com.coboltforge.xbox.util.xml.SimpleAttributes;

import java.util.Iterator;
import java.util.Map.Entry;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentValuesElement implements IElement, ISimpleElement {
    private ContentValues _values;
    private String _elementTag;

    public ContentValuesElement(ContentValues var1, String var2) {
        this._values = var1;
        this._elementTag = var2;
    }

    public String getElementTag() {
        return this._elementTag;
    }

    public DefaultHandler readFromXML(IHandlerStack var1) {
        return new SimpleHandler(var1, this);
    }

    public void writeToXML(ContentHandler var1) throws SAXException {
        com.coboltforge.xbox.util.xml.SimpleAttributes var2 = new SimpleAttributes();
        Iterator var3 = this._values.valueSet().iterator();

        while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if (var4.getKey() != null && var4.getValue() != null) {
                var2.addValue(var4.getKey(), var4.getValue());
            }
        }

        var1.startElement("", "", this.getElementTag(), var2.getAttributes());
        var1.endElement("", "", this.getElementTag());
    }

    public void gotElement(String var1, String var2, Attributes var3) throws SAXException {
        int var4 = var3.getLength();

        for(int var5 = 0; var5 < var4; ++var5) {
            this._values.put(var3.getLocalName(var5), var3.getValue(var5));
        }

    }
}
