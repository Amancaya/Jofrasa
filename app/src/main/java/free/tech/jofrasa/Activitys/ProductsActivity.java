package free.tech.jofrasa.Activitys;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.andremion.counterfab.CounterFab;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import free.tech.jofrasa.Adapters.AdapterPager;
import free.tech.jofrasa.ExtraClass.QueryRealm;
import free.tech.jofrasa.Interface.MySendView;
import free.tech.jofrasa.Interface.UpdateCountShoppingCart;
import free.tech.jofrasa.Model.Provider;
import free.tech.jofrasa.ExtraClass.MySearchView;
import free.tech.jofrasa.Nav_central;
import free.tech.jofrasa.R;
import io.realm.Realm;

public class ProductsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, TabLayout.OnTabSelectedListener,
                                    UpdateCountShoppingCart {

    private static final String TAG = "ProductsActivity";
    private static final String PROD_NAME = "name";
    private static final String PROD_PICTURE = "picture";
    private static final String PROD_ID = "id";

    private Provider provider;
    private ImageView imageView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CounterFab counterFab;
    private List<Fragment> fragmentList;
    private MySearchView searchView;
    private MenuItem searchItem;
    private MySendView mySendView;
    private String[] labels = {Nav_central.PRODUCT_UNIQUE, Nav_central.PRODUCT_PACKAGE};
    private Nav_central currentFragment;
    private int currentPosition = 0;
    private AdapterPager adapterPager;
    private QueryRealm queryRealm;
    private Realm realm;

    public static void createInstance(Activity activity, Provider provider){
        Intent intent = getLaunchIntent(activity, provider)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                activity.findViewById(R.id.picture), "MyTransition");
        activity.startActivity(intent, activityOptionsCompat.toBundle());
    }

    public static Intent getLaunchIntent(Activity activity, Provider provider){
        Log.e(TAG, provider.getId()+"");
        Intent intent = new Intent(activity, ProductsActivity.class);
        intent.putExtra(PROD_NAME, provider.getBrand());
        intent.putExtra(PROD_PICTURE, provider.getImage());
        intent.putExtra(PROD_ID, provider.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getData();
        getSupportActionBar().setTitle(provider.getBrand());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentList = new ArrayList<>();
        initCollapsingToolbar();
        realm = Realm.getDefaultInstance();
        queryRealm = new QueryRealm(realm, this);
        currentFragment = (Nav_central) fragmentList.get(0);
        counterFab = (CounterFab) findViewById(R.id.fab_product);
        counterFab.setCount(queryRealm.countCart());
        imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(provider.getImage()).placeholder(R.drawable.paloma).into(imageView);

        counterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mySendView = (MySendView) fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentList.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new MySearchView(this, fragmentList);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        searchItem.setActionView(searchView);
        searchView.SetPosition(currentPosition);

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CloseSearchView();
            }
        });
        return true;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.cola);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appBarLayout.setExpanded(true);
        // hiding & showing the title when toolbar expanded & collapsed
        LoadTabViewPager();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                   // relativeLayout.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    //relativeLayout.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

    }
    private void getData(){
        provider = new Provider();
        provider.setBrand(getIntent().getStringExtra(PROD_NAME));
        provider.setImage(getIntent().getStringExtra(PROD_PICTURE));
        provider.setId(getIntent().getIntExtra(PROD_ID, 0));
    }
    private void LoadTabViewPager(){
        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabTextColors(Color.GRAY, getResources().getColor(R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.icons));

        viewPager.setOffscreenPageLimit(2);
        LoadPagers(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }
    private void LoadPagers(ViewPager viewPager){
        for(int i = 0; i< labels.length; i++){
            Nav_central nav_central = Nav_central.createInstance(labels[i], provider.getId());
            nav_central.setNav_central(nav_central);
            fragmentList.add(nav_central);
        }
        adapterPager = new AdapterPager(getSupportFragmentManager(), fragmentList, labels);
        viewPager.setAdapter(adapterPager);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mySendView = currentFragment;
        if (currentFragment != null){
           mySendView.filter(currentFragment.getAdapterNavListItem(), newText, currentFragment);
        }
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentFragment = (Nav_central) fragmentList.get(tab.getPosition());
        currentPosition = tab.getPosition();
        searchView.SetPosition(currentPosition);
        if (searchView != null && searchItem != null){
            searchView.clearFocus();
            searchItem.collapseActionView();
        }

    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    public void CloseSearchView(){
        searchView.clearFocus();
        currentFragment.returnToTheValues();
    }

    @Override
    public void UpdateCount(int quantity) {
        counterFab.setCount(quantity);
    }
}
