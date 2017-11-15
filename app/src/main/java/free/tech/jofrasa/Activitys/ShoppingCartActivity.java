package free.tech.jofrasa.Activitys;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import free.tech.jofrasa.Adapters.AdapterNav;
import free.tech.jofrasa.ExtraClass.ApiUtils;
import free.tech.jofrasa.ExtraClass.QueryRealm;
import free.tech.jofrasa.Interface.ApiInterface;
import free.tech.jofrasa.Interface.UpdateValues;
import free.tech.jofrasa.Model.Purchase;
import free.tech.jofrasa.Nav_central;
import free.tech.jofrasa.R;
import io.realm.Realm;
import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends AppCompatActivity implements UpdateValues{

    private static final String TAG = "ShoppingCartActivity";
    private QueryRealm queryRealm;
    private Realm realm;
    private TextView subtotal, total;
    private Button send;
    private ApiInterface mApiService;
    private int controlThread;
    private Nav_central fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_detail);

        realm = Realm.getDefaultInstance();
        queryRealm = new QueryRealm(realm);

        controlThread = 0;
        mApiService = mApiService = ApiUtils.getAPIService();

        subtotal = (TextView) findViewById(R.id.subtotal);
        total = (TextView) findViewById(R.id.total);
        send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (RealmObject realmObject: queryRealm.getListPurchases()) {
                    Purchase purchase = (Purchase) realmObject;
                    SendProducts(purchase.getIdProduct(), "12345", purchase.getQuantity());
                }
            }
        });

        CAll4Fragment();
        CalculateValues();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(Purchase.class);
                    fragment.ClearAdapterAndList();
                    CalculateValues();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    @Override
    public void ChangeValues() {
        CalculateValues();
    }

    private void CAll4Fragment(){
        fragment = new Nav_central();
        Bundle bundle = new Bundle();
        bundle.putString(Nav_central.TAG, Nav_central.CART);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.commit();
    }

    private void CalculateValues(){
        double totalP = 0;
        for (RealmObject realmObject: queryRealm.getListPurchases()) {
            totalP = totalP + ((Purchase) realmObject).getPrice();
        }
        subtotal.setText(totalP+" Bs");
        total.setText((totalP+10)+" Bs");
    }

    private void SendProducts(int idProduct, String nit, int quatity){
        mApiService.saveProduct(idProduct, nit, quatity).enqueue(new Callback<Purchase>() {
            @Override
            public void onResponse(Call<Purchase> call, Response<Purchase> response) {

                if (response.isSuccessful()) {
                    controlThread++;
                    Log.e(TAG, "send successful "+controlThread);
                }
                ShowList();
            }

            @Override
            public void onFailure(Call<Purchase> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private synchronized void ShowList(){
        if (controlThread == queryRealm.getListPurchases().size()) {
            Toast.makeText(this, "Solicitud enviada correctamente", Toast.LENGTH_LONG).show();
        }
    }
}
