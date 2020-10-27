package me.jessyan.mvparms.demo.setting.device;

public class NetSetting {

    // net group
    private String netAddress;
    private String netAddressMask;
    private String netDns;

    public String getNetAddress() {
        return netAddress;
    }

    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress;
    }

    public String getNetAddressMask() {
        return netAddressMask;
    }

    public void setNetAddressMask(String netAddressMask) {
        this.netAddressMask = netAddressMask;
    }

    public String getNetDns() {
        return netDns;
    }

    public void setNetDns(String netDns) {
        this.netDns = netDns;
    }
}
