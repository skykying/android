package me.jessyan.mvparms.demo.db;

import io.realm.RealmObject;

public class dbDog extends RealmObject {
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

    @Override
    public String toString() {
        return "dbDog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", isFale=" + isFale +
                '}';
    }
}