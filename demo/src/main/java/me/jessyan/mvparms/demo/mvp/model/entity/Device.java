package me.jessyan.mvparms.demo.mvp.model.entity;

import org.jetbrains.annotations.NotNull;

public class Device {
    private final String id;
    private final String name;

    private final String adminState;
    private final String operatingState;

    private final String url;

    public Device(String id, String name, String adminState, String operatingState, String url) {
        this.id = id;
        this.name = name;
        this.adminState = adminState;
        this.operatingState = operatingState;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAdminState() {
        return adminState;
    }

    public String getOperatingState() {
        return operatingState;
    }

    @NotNull
    @Override
    public String toString() {
        return "id -> " + id + " name -> " + name;
    }
}
