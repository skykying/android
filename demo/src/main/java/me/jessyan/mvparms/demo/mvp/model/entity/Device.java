package me.jessyan.mvparms.demo.mvp.model.entity;

import org.jetbrains.annotations.NotNull;

public class Device {
//    private final int id;
//    private final String name;
//
//    private final String adminState;
//    private final String operatingState;
//
//    private final String url;
//
//    public Device(int id, String name, String adminState, String operatingState, String url) {
//        this.id = id;
//        this.name = name;
//        this.adminState = adminState;
//        this.operatingState = operatingState;
//        this.url = url;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//
//    public int getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getAdminState() {
//        return adminState;
//    }
//
//    public String getOperatingState() {
//        return operatingState;
//    }
//
//    @NotNull
//    @Override
//    public String toString() {
//        return "id -> " + id + " name -> " + name;
//    }

    private final int id;
    private final String login;
    private final String avatar_url;

    public Device(int id, String login, String avatarUrl) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatarUrl;
    }

    public String getAvatarUrl() {
        if (avatar_url.isEmpty()) {
            return avatar_url;
        }
        return avatar_url.split("\\?")[0];
    }


    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    @NotNull
    @Override
    public String toString() {
        return "id -> " + id + " login -> " + login;
    }
}
