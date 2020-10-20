package me.jessyan.mvparms.demo.db;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class dbUser extends RealmObject {
    @PrimaryKey
    private String id;
    private int age;
    
    @Required
    private String name;
    private RealmList<dbDog> dbDogRealmList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<dbDog> getDbDogRealmList() {
        return dbDogRealmList;
    }

    public void setDbDogRealmList(RealmList<dbDog> dbDogRealmList) {
        this.dbDogRealmList = dbDogRealmList;
    }
}