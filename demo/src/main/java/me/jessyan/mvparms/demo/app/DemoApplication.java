package me.jessyan.mvparms.demo.app;

import android.content.Context;

import com.jess.arms.base.BaseApplication;

import java.io.FileNotFoundException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import me.jessyan.mvparms.demo.db.CustomMigration;

public class DemoApplication extends BaseApplication {

    private static Realm instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = initDB(getApplicationContext());
    }

    public  Realm initDB(Context context) {
        Realm.init(context);
        Realm realm = null;

        RealmConfiguration config0 = new RealmConfiguration.Builder().name("default0").schemaVersion(3).build();
        try {
            realm = Realm.getInstance(config0);
        } catch (RealmMigrationNeededException e) {
            e.printStackTrace();
            // You can then manually call Realm.migrateRealm().
            try{
                Realm.migrateRealm(config0, new CustomMigration());
                realm = Realm.getInstance(config0);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }

        Realm.setDefaultConfiguration(config0);
        return realm;
    }

    public static Realm getRealmInstance(){
        return instance;
    }
}
