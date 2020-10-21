/**
 * Copyright (C) 2009 Michael A. MacDonald
 */
package com.coboltforge.dontmind.xbox.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView.ScaleType;

import com.antlersoft.android.dbimpl.NewInstance;
import com.coboltforge.dontmind.xbox.color.COLORMODEL;

/**
 * @author Michael A. MacDonald
 */
public class ConnectionContext implements Comparable<ConnectionContext> {

    // Members corresponding to defined fields
    private long gen__Id;
    private java.lang.String gen_nickname;
    private java.lang.String gen_address;
    private int gen_port;
    private java.lang.String gen_password;
    private java.lang.String gen_colorModel;
    private long gen_forceFull;
    private java.lang.String gen_repeaterId;
    private java.lang.String gen_inputMode;
    private java.lang.String gen_SCALEMODE;
    private boolean gen_useLocalCursor;
    private boolean gen_keepPassword;
    private boolean gen_followMouse;
    private boolean gen_useRepeater;
    private long gen_metaListId;
    private long gen_LAST_META_KEY_ID;
    private boolean gen_followPan;
    private java.lang.String gen_userName;
    private java.lang.String gen_secureConnectionType;
    private boolean gen_showZoomButtons;
    private java.lang.String gen_DOUBLE_TAP_ACTION;


    public ConnectionContext() {
        set_Id(0);
        setAddress("");
        setPassword("");
        setKeepPassword(true);
        setNickname("");
        setUserName("");
        setPort(5900);
        setColorModel(COLORMODEL.C24bit.nameString());
        setScaleMode(ScaleType.MATRIX);
        setFollowMouse(true);
        setRepeaterId("");
        setMetaListId(1);
    }


    // Field accessors
    public long get_Id() {
        return gen__Id;
    }

    public void set_Id(long arg__Id) {
        gen__Id = arg__Id;
    }

    public java.lang.String getNickname() {
        return gen_nickname;
    }

    public void setNickname(java.lang.String arg_nickname) {
        gen_nickname = arg_nickname;
    }

    public java.lang.String getAddress() {
        return gen_address;
    }

    public void setAddress(java.lang.String arg_address) {
        gen_address = arg_address;
    }

    public int getPort() {
        return gen_port;
    }

    public void setPort(int arg_port) {
        gen_port = arg_port;
    }

    public java.lang.String getPassword() {
        return gen_password;
    }

    public void setPassword(java.lang.String arg_password) {
        gen_password = arg_password;
    }

    public java.lang.String getColorModel() {
        return gen_colorModel;
    }

    public void setColorModel(java.lang.String arg_colorModel) {
        gen_colorModel = arg_colorModel;
    }

    public long getForceFull() {
        return gen_forceFull;
    }

    public void setForceFull(long arg_forceFull) {
        gen_forceFull = arg_forceFull;
    }

    public java.lang.String getRepeaterId() {
        return gen_repeaterId;
    }

    public void setRepeaterId(java.lang.String arg_repeaterId) {
        gen_repeaterId = arg_repeaterId;
    }

    public java.lang.String getScaleModeAsString() {
        return gen_SCALEMODE;
    }

    public void setScaleModeAsString(java.lang.String arg_SCALEMODE) {
        gen_SCALEMODE = arg_SCALEMODE;
    }

    public boolean getUseLocalCursor() {
        return gen_useLocalCursor;
    }

    public void setUseLocalCursor(boolean arg_useLocalCursor) {
        gen_useLocalCursor = arg_useLocalCursor;
    }

    public boolean getKeepPassword() {
        return gen_keepPassword;
    }

    public void setKeepPassword(boolean arg_keepPassword) {
        gen_keepPassword = arg_keepPassword;
    }

    public boolean getFollowMouse() {
        return gen_followMouse;
    }

    public void setFollowMouse(boolean arg_followMouse) {
        gen_followMouse = arg_followMouse;
    }

    public boolean getUseRepeater() {
        return gen_useRepeater;
    }

    public void setUseRepeater(boolean arg_useRepeater) {
        gen_useRepeater = arg_useRepeater;
    }

    public long getMetaListId() {
        return gen_metaListId;
    }

    public void setMetaListId(long arg_metaListId) {
        gen_metaListId = arg_metaListId;
    }

    public long getLastMetaKeyId() {
        return gen_LAST_META_KEY_ID;
    }

    public void setLastMetaKeyId(long arg_LAST_META_KEY_ID) {
        gen_LAST_META_KEY_ID = arg_LAST_META_KEY_ID;
    }

    public boolean getFollowPan() {
        return gen_followPan;
    }

    public void setFollowPan(boolean arg_followPan) {
        gen_followPan = arg_followPan;
    }

    public java.lang.String getUserName() {
        return gen_userName;
    }

    public void setUserName(java.lang.String arg_userName) {
        gen_userName = arg_userName;
    }

    public java.lang.String getSecureConnectionType() {
        return gen_secureConnectionType;
    }

    public void setSecureConnectionType(java.lang.String arg_secureConnectionType) {
        gen_secureConnectionType = arg_secureConnectionType;
    }

    public boolean getShowZoomButtons() {
        return gen_showZoomButtons;
    }

    public void setShowZoomButtons(boolean arg_showZoomButtons) {
        gen_showZoomButtons = arg_showZoomButtons;
    }

    public java.lang.String getDoubleTapActionAsString() {
        return gen_DOUBLE_TAP_ACTION;
    }

    public void setDoubleTapActionAsString(java.lang.String arg_DOUBLE_TAP_ACTION) {
        gen_DOUBLE_TAP_ACTION = arg_DOUBLE_TAP_ACTION;
    }

    boolean isNew() {
        return get_Id() == 0;
    }

    public void save(SQLiteDatabase database) {

    }

    public ScaleType getScaleMode() {
        return ScaleType.valueOf(getScaleModeAsString());
    }

    public void setScaleMode(ScaleType value) {
        setScaleModeAsString(value.toString());
    }

    @Override
    public String toString() {
        return get_Id() + " " + getNickname() + ": " + getAddress() + ", port " + getPort();
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ConnectionContext another) {
        int result = getNickname().compareTo(another.getNickname());
        if (result == 0) {
            result = getAddress().compareTo(another.getAddress());
            if (result == 0) {
                result = getPort() - another.getPort();
            }
        }
        return result;
    }

    /**
     * parse host:port or [host]:port and split into address and port fields
     *
     * @param hostport_str
     * @return true if there was a port, false if not
     */
    public boolean parseHostPort(String hostport_str) {
        int nr_colons = hostport_str.replaceAll("[^:]", "").length();
        int nr_endbrackets = hostport_str.replaceAll("[^]]", "").length();

        if (nr_colons == 1) { // IPv4
            String p = hostport_str.substring(hostport_str.indexOf(':') + 1);
            try {
                setPort(Integer.parseInt(p));
            } catch (Exception e) {
            }
            setAddress(hostport_str.substring(0, hostport_str.indexOf(':')));
            return true;
        }
        if (nr_colons > 1 && nr_endbrackets == 1) {
            String p = hostport_str.substring(hostport_str.indexOf(']') + 2); // it's [addr]:port
            try {
                setPort(Integer.parseInt(p));
            } catch (Exception e) {
            }
            setAddress(hostport_str.substring(0, hostport_str.indexOf(']') + 1));
            return true;
        }
        return false;
    }

    public boolean Gen_getValues() {
        return false;
    }
}
