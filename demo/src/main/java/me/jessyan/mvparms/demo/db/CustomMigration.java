package me.jessyan.mvparms.demo.db;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class CustomMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion==0){
            oldVersion ++;
        }

        if (oldVersion == 1) {

            schema.create("ImportantCalls")
                    .addField("id", int.class)
                    .addField("number", String.class)
                    .addField("callType", String.class)
                    .addField("startTime", String.class)
                    .addField("callRecords", String.class)
                    .addField("callStatus", String.class)
                    .addField("actualStartTime", Long.class);
            oldVersion++;
        }
    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof CustomMigration);
    }
}