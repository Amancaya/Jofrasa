package free.tech.jofrasa.ExtraClass;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import free.tech.jofrasa.Model.Purchase;
import free.tech.jofrasa.R;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by root on 04-11-17.
 */

public class QueryRealm {

    private Realm realm;
    public QueryRealm(Realm realm){
       this.realm = realm;
    }

    public Realm getRealm(){
        return this.realm;
    }

    public List<RealmObject> getListPurchases(){
        List<RealmObject> objectList = new ArrayList<>();
        objectList.addAll(realm.where(Purchase.class).findAll());
        return objectList;
    }

    public boolean deleteAll(){
        RealmResults<Purchase> realmResults = realm.where(Purchase.class).findAll();
        return realmResults.deleteAllFromRealm();
    }

    public int countCart(){
        return (int) realm.where(Purchase.class).count();
    }
}
