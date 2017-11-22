package free.tech.jofrasa.Activitys;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


import com.andremion.counterfab.CounterFab;


import free.tech.jofrasa.ExtraClass.QueryRealm;
import free.tech.jofrasa.Interface.MySendView;
import free.tech.jofrasa.ExtraClass.MySearchView;
import free.tech.jofrasa.Nav_central;
import free.tech.jofrasa.R;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String TAG = "Main";
    private Nav_central fragment;
    MySendView mySendView;
    private SearchView searchView;
    private QueryRealm queryRealm;
    private CounterFab fab;
    //shared preference
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedpreferences = getSharedPreferences("mypreference",
                Context.MODE_PRIVATE);
        toolbar.setTitle("");
        Realm realm = Realm.getDefaultInstance();
        queryRealm = new QueryRealm(realm);
        setSupportActionBar(toolbar);
        CallFragment();
        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        getSharedpreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(queryRealm.countCart());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new MySearchView(this, fragment);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        searchItem.setActionView(searchView);

        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.icons));
        searchEditText.setHintTextColor(getResources().getColor(R.color.icons));

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseSearchView();
                hideKeyboard();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.e(TAG, "onOptionsItemSelected");
        int id = item.getItemId();
        if (R.id.out == id){
            finish();
        }else if (R.id.log_out == id){
            Log.i("Main", id+" id log_out ");
            Log.e("Main", "LogOut");
        }
        return true;
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mySendView = (MySendView) fragment;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void CallFragment(){
        fragment = new Nav_central();
        Bundle bundle = new Bundle();
        bundle.putString(Nav_central.TAG, Nav_central.MAIN);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (fragment != null){
            mySendView.filter(fragment.getAdapterNavListItem(), newText, fragment);
        }
        return true;
    }

    public void CloseSearchView(){
        searchView.setQuery("", false);
        searchView.clearFocus();
        fragment.returnToTheValues();
    }

    public void getSharedpreferences() {
        sharedpreferences = getSharedPreferences("mypreference",
                Context.MODE_PRIVATE);
            Log.i("SHARED ",sharedpreferences.getAll()+"");
    }

}
