package com.shop.food.app;


import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import com.shop.food.model.FoodInfo;

import java.util.Hashtable;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Supriya A on 1/2/2018.
 */
public class DatabaseController {
    private static DatabaseController instance;
    private Realm realm = null;
    // shared cache for primary keys
    private static Hashtable<Class<? extends RealmModel>, String> primaryKeyMap = new Hashtable<>();

    public DatabaseController(Application application) {
        try {
            if (Realm.getDefaultInstance() != null) {
                realm = Realm.getDefaultInstance();
            } else {
                realm = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseController with(Fragment fragment) {

        if (instance == null) {
            instance = new DatabaseController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static DatabaseController with(Activity activity) {

        if (instance == null) {
            instance = new DatabaseController(activity.getApplication());
        }
        return instance;
    }

    public static DatabaseController with(Application application) {

        if (instance == null) {
            instance = new DatabaseController(application);
        }
        return instance;
    }

    public static DatabaseController getInstance() {
        if (instance == null) {
            instance = new DatabaseController(UrbanPiperApplication.getInstance());
        }
        return instance;
    }

    public Realm getRealm() {

        return realm;
    }


    public RealmResults<FoodInfo> getArticlesFromDb() {
        RealmResults<FoodInfo> articlesList = realm.where(FoodInfo.class).findAll();
        return articlesList;
    }

    public FoodInfo getArticleById(String id) {
        FoodInfo items = realm.where(FoodInfo.class).equalTo("articleId", id).findFirst();

        return items;
    }


    public RealmResults<FoodInfo> getArticleByCheckout( ) {
        RealmResults<FoodInfo> foodItems = realm.where(FoodInfo.class).greaterThan("quantity", 0).findAll();

        return foodItems;
    }


    public static void resetRealm() {
        if (instance != null) {
            instance = null;
        }
    }


    public void saveRealmObject(FoodInfo realmObject) {
        //realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObject);
        //realm.commitTransaction();
    }
}
