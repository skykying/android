/**
 * Copyright (C) 2009 Michael A. MacDonald
 */
package com.coboltforge.xbox.meta;

/**
 * @author Michael A. MacDonald
 */
public class MetaKeyBase implements Comparable<MetaKeyBase> {
    public int keySym;
    public int mouseButtons;
    public int keyEvent;
    public String name;
    public boolean isMouse;
    public boolean isKeyEvent;

    public MetaKeyBase(int mouseButtons, String name) {
        this.mouseButtons = mouseButtons;
        this.name = name;
        this.isMouse = true;
        this.isKeyEvent = false;
    }

    public MetaKeyBase(String name, int keySym, int keyEvent) {
        this.name = name;
        this.keySym = keySym;
        this.keyEvent = keyEvent;
        this.isMouse = false;
        this.isKeyEvent = true;
    }

    public MetaKeyBase(String name, int keySym) {
        this.name = name;
        this.keySym = keySym;
        this.isMouse = false;
        this.isKeyEvent = false;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(MetaKeyBase another) {
        return name.compareTo(another.name);
    }
}
