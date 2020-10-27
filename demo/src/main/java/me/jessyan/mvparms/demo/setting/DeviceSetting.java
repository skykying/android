package me.jessyan.mvparms.demo.setting;

public class DeviceSetting {

    // cloud setting
    private int port;
    private String address;
    private int delay;

    // net group
    private String netAddress;
    private String netAddressMask;
    private String netDns;

    //wifi group
    private String wifiName;
    private String wifipassword;

    // input port functions
    private int portOne;
    private int portTwo;
    private int portThree;
    private int portThix;
    private int portFive;
    private int portSix;

    // output port actions
    private int actionPortOne;
    private int actionPortTwo;
    private int actionPortThree;
    private int actionPortThix;
    private int actionPortFive;
    private int actionPortSix;

    // device identifications
    private String IMEI;
    private String netFunction;

    // software version
    private String version;

    // device location
    private String location;

    //device name
    private String deviceName;

    //device code of local network
    private int productCode;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifipassword() {
        return wifipassword;
    }

    public void setWifipassword(String wifipassword) {
        this.wifipassword = wifipassword;
    }

    public int getPortOne() {
        return portOne;
    }

    public void setPortOne(int portOne) {
        this.portOne = portOne;
    }

    public int getPortTwo() {
        return portTwo;
    }

    public void setPortTwo(int portTwo) {
        this.portTwo = portTwo;
    }

    public int getPortThree() {
        return portThree;
    }

    public void setPortThree(int portThree) {
        this.portThree = portThree;
    }

    public int getPortThix() {
        return portThix;
    }

    public void setPortThix(int portThix) {
        this.portThix = portThix;
    }

    public int getPortFive() {
        return portFive;
    }

    public void setPortFive(int portFive) {
        this.portFive = portFive;
    }

    public int getPortSix() {
        return portSix;
    }

    public void setPortSix(int portSix) {
        this.portSix = portSix;
    }

    public int getActionPortOne() {
        return actionPortOne;
    }

    public void setActionPortOne(int actionPortOne) {
        this.actionPortOne = actionPortOne;
    }

    public int getActionPortTwo() {
        return actionPortTwo;
    }

    public void setActionPortTwo(int actionPortTwo) {
        this.actionPortTwo = actionPortTwo;
    }

    public int getActionPortThree() {
        return actionPortThree;
    }

    public void setActionPortThree(int actionPortThree) {
        this.actionPortThree = actionPortThree;
    }

    public int getActionPortThix() {
        return actionPortThix;
    }

    public void setActionPortThix(int actionPortThix) {
        this.actionPortThix = actionPortThix;
    }

    public int getActionPortFive() {
        return actionPortFive;
    }

    public void setActionPortFive(int actionPortFive) {
        this.actionPortFive = actionPortFive;
    }

    public int getActionPortSix() {
        return actionPortSix;
    }

    public void setActionPortSix(int actionPortSix) {
        this.actionPortSix = actionPortSix;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getNetFunction() {
        return netFunction;
    }

    public void setNetFunction(String netFunction) {
        this.netFunction = netFunction;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
