//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class SimpleAttributes {
    private Attributes attr;
    public int defaultInt = 0;
    public String defaultString = "";
    public double defaultDouble = 0.0D;

    public SimpleAttributes(Attributes var1) {
        this.attr = var1;
    }

    public SimpleAttributes() {
        this.attr = new AttributesImpl();
    }

    public Attributes getAttributes() {
        return this.attr;
    }

    public <E extends Enum<E>> E enumValue(Object var1, E var2) {
        String var3 = this.attr.getValue(var1.toString());
        if (var3 != null) {
            try {
                return (E) Enum.valueOf(var2.getClass(), var3);
            } catch (IllegalArgumentException var5) {
            }
        }

        return var2;
    }

    public void addValue(Object var1, Object var2) {
        ((AttributesImpl)this.attr).addAttribute("", "", var1.toString(), "", var2.toString());
    }

    public int intValue(Object var1, int var2) {
        String var3 = this.attr.getValue(var1.toString());
        if (var3 != null) {
            try {
                return Integer.valueOf(var3);
            } catch (NumberFormatException var5) {
            }
        }

        return var2;
    }

    public int intValue(Object var1) {
        return this.intValue(var1, this.defaultInt);
    }

    public void setDefaultInt(int var1) {
        this.defaultInt = var1;
    }

    public String stringValue(Object var1, String var2) {
        String var3 = this.attr.getValue(var1.toString());
        if (var3 == null) {
            var3 = var2;
        }

        return var3;
    }

    public String stringValue(Object var1) {
        return this.stringValue(var1, this.defaultString);
    }

    public double doubleValue(Object var1, double var2) {
        String var4 = this.attr.getValue(var1.toString());
        if (var4 != null) {
            try {
                return Double.valueOf(var4);
            } catch (NumberFormatException var6) {
            }
        }

        return var2;
    }

    public double doubleValue(Object var1) {
        return this.doubleValue(var1, this.defaultDouble);
    }

    public long longValue(Object var1, long var2) {
        String var4 = this.attr.getValue(var1.toString());
        if (var4 != null) {
            try {
                return Long.valueOf(var4);
            } catch (NumberFormatException var6) {
            }
        }

        return var2;
    }

    public long longValue(Object var1) {
        return this.longValue(var1, (long)this.defaultInt);
    }

    public boolean booleanValue(Object var1, boolean var2) {
        String var3 = this.attr.getValue(var1.toString());
        return var3 != null ? Boolean.valueOf(var3) : var2;
    }

    public boolean booleanValue(Object var1) {
        return this.booleanValue(var1, false);
    }
}
