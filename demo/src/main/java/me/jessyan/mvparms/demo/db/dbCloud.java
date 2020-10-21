package me.jessyan.mvparms.demo.db;

import io.realm.RealmObject;

public class dbCloud extends RealmObject {
    //cloud address
    private String address;

    // cloud port
    private int port;

    // cloud protocol, tcp, mqtt, http
    private String protocol;

    // cloud module, nbiot, wifi, net
    private String module;

    // cloud update interval
    private int update_interval;

    // cloud tip inteval
    private int tip_interval;

    private String name;
    private int age;
    private boolean isFale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFale() {
        return isFale;
    }

    public void setFale(boolean fale) {
        isFale = fale;
    }


}