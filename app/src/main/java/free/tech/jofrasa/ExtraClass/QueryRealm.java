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
    private static final String TAG = "QueryRealm";

    private RealmAsyncTask realmAsyncTask;
    private Realm realm;
    private Activity activity;
    public QueryRealm(Realm realm, Activity activity){
       this.realm = realm;
       this.activity = activity;
    }

    public Realm getRealm(){
        return this.realm;
    }

    public List<RealmObject> getListPurchases(){
        List<RealmObject> objectList = new ArrayList<>();
        objectList.addAll(realm.where(Purchase.class).findAll());
        return objectList;
    }

    public boolean UpdateQuantityData(int ID, final int quantity, final double price){
        final boolean[] retornar = {false};
        final Purchase purchaseUp = realm.where(Purchase.class).equalTo("idProduct", ID).findFirst();
        realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                purchaseUp.setQuantity(quantity);
                purchaseUp.setPrice(price);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Actualizado correctamente");
                retornar[0] = true;
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "No Actualizo correctamente");
                retornar[0] = false;
            }
        });

        return retornar[0];
    }

    public boolean DeleteData(int ID){
        Log.e("Query", "DeleteData");
        RealmResults<Purchase> purchases = realm.where(Purchase.class).equalTo("idProduct", ID).findAll();
        return  purchases.deleteAllFromRealm();
    }

    public int countCart(){
        return (int) realm.where(Purchase.class).count();
    }

    public void CanceledAsyntask(){
        if (realmAsyncTask != null){
            if (!realmAsyncTask.isCancelled()){
                realmAsyncTask.cancel();
            }
        }

    }
}
