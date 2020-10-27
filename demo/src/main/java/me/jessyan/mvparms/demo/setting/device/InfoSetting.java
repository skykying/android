package me.jessyan.mvparms.demo.setting.device;

public class InfoSetting {

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
}
