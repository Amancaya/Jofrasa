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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andremion.counterfab.CounterFab;

import java.util.ArrayList;
import java.util.List;

import free.tech.jofrasa.Adapters.AdapterPager;
import free.tech.jofrasa.Model.Provider;
import free.tech.jofrasa.Nav_central;
import free.tech.jofrasa.R;

public class ProductsActivity extends AppCompatActivity {

    private static final String TAG = "ProductsActivity";
    private static final String PROD_NAME = "name";
    private static final String PROD_PICTURE = "picture";

    private Provider provider;
    private ImageView imageView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CounterFab counterFab;
    private List<Fragment> fragmentList;
    private String[] labels = {Nav_central.PRODUCT_UNIQUE, Nav_central.PRODUCT_PACKAGE};;
    private EditText editText;
    private RelativeLayout relativeLayout;
    public static void createInstance(Activity activity, Provider provider){
        Log.e("ProductsActivity", "createInstance");
        Intent intent = getLaunchIntent(activity, provider)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                activity.findViewById(R.id.picture), "MyTransition");
        activity.startActivity(intent, activityOptionsCompat.toBundle());
    }

    public static Intent getLaunchIntent(Activity activity, Provider provider){
        Log.e("ProductsActivity", "getLaunchIntent");
        Intent intent = new Intent(activity, ProductsActivity.class);
        intent.putExtra(PROD_NAME, provider.getBrand());
        intent.putExtra(PROD_PICTURE, provider.getImage());
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getData();
        getSupportActionBar().setTitle(provider.getBrand());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar();
        counterFab = (CounterFab) findViewById(R.id.fab_product);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageResource(provider.getImage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentList.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
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
        provider.setImage(getIntent().getIntExtra(PROD_PICTURE, 0));
    }
    private void LoadTabViewPager(){
        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabTextColors(Color.GRAY, getResources().getColor(R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.icons));


        viewPager.setOffscreenPageLimit(2);
        LoadPagers(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }
    private void LoadPagers(ViewPager viewPager){
        fragmentList = new ArrayList<>();
        for (int i = 0; i< labels.length; i++){
            fragmentList.add(Nav_central.createInstance(labels[i]));
        }
        viewPager.setAdapter(new AdapterPager(getSupportFragmentManager(), fragmentList, labels));
    }

}
