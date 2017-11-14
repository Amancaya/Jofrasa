package free.tech.jofrasa.Activitys;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableRow.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import free.tech.jofrasa.Adapters.AdapterNav;
import free.tech.jofrasa.ExtraClass.QueryRealm;
import free.tech.jofrasa.Model.Purchase;
import free.tech.jofrasa.Nav_central;
import free.tech.jofrasa.R;
import free.tech.jofrasa.StaticData;
import io.realm.Realm;
import io.realm.RealmObject;

public class ShoppingCartActivity extends AppCompatActivity {

    private static final String TAG = "ShoppingCartActivity";
    private TableLayout tabLayout;
    private TableRow tableRow;
    private RecyclerView recyclerView;
    private QueryRealm queryRealm;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_detail);

        realm = Realm.getDefaultInstance();
        queryRealm = new QueryRealm(realm, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        if (queryRealm != null)
            recyclerView.setAdapter(new AdapterNav(queryRealm.getListPurchases(), this, realm));

        //to hide the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        queryRealm.CanceledAsyntask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
