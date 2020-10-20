package me.jessyan.mvparms.demo.db;

import android.content.Context;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;


//https://blog.csdn.net/qq_31433525/article/details/79067266
public class dbRealm {

    public void initDB(Context context) {
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name("test.realm").build();
        Realm.setDefaultConfiguration(configuration);
    }

    public void add(Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //创建对象，由于有主键需要加上参数，因为realm不能自增，所有使用uuid代替
                dbUser user = realm.createObject(dbUser.class, UUID.randomUUID().toString());
                user.setAge(18);
                user.setName("谁谁谁");
                RealmList<dbDog> dbDogs = new RealmList<>();
                for(int i=0;i<3;i++){
                    dbDog dbDog = realm.createObject(dbDog.class);
                    dbDog.setAge(5);
                    dbDog.setFale(true);
                    dbDog.setName("小白"+i);
                    dbDogs.add(dbDog);
                }
                user.setDbDogRealmList(dbDogs);
                //由于有主键使用copyToRealmOrUpdate
                realm.copyToRealmOrUpdate(user);
            }
        });
    }


    public void add2(Realm realm){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String json = "{id:\"123\",age:5,name:\"szh\",dogRealmList:[{name:\"hh\",age:2,isFale:false},{name:\"sss\",age:4,isFale:false}]}";
                realm.createObjectFromJson(dbUser.class,json);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                System.out.println("success");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                System.out.println("error");
                System.out.println(error);
            }
        });
    }

    public void del(Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //先查询再删除
                RealmResults<dbUser> result= realm.where(dbUser.class).findAll();
                result.get(4).deleteFromRealm();//删除指定位置（第4条记录）的记录
                result.deleteFromRealm(4);//删除指定位置（第4条记录）的记录
                result.deleteFirstFromRealm(); //删除user表的第一条数据
                result.deleteLastFromRealm();//删除user表的最后一条数据
                result.deleteAllFromRealm();//删除user表的全部数据
            }
        });
    }

    public void modifer(Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //先查询后更改
                RealmResults<dbDog> result = realm.where(dbDog.class).equalTo("name","hh").findAll();
                for(dbDog dbDog : result){
                    dbDog.setAge(20);
                }
            }
        });
    }
}
