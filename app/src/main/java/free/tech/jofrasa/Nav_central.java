package free.tech.jofrasa;



import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import free.tech.jofrasa.Activitys.ProductsActivity;
import free.tech.jofrasa.Activitys.ShoppingCartActivity;
import free.tech.jofrasa.Adapters.AdapterNav;
import free.tech.jofrasa.ExtraClass.ApiClient;
import free.tech.jofrasa.ExtraClass.ExtraFunctions;
import free.tech.jofrasa.ExtraClass.QueryRealm;
import free.tech.jofrasa.Interface.UpdateCountShoppingCart;
import free.tech.jofrasa.Interface.UpdateValues;
import free.tech.jofrasa.Response.ResponseProduct;
import free.tech.jofrasa.Response.ResponseProvider;
import free.tech.jofrasa.Interface.ApiInterface;
import free.tech.jofrasa.Interface.MySendView;
import free.tech.jofrasa.Model.Producto;
import free.tech.jofrasa.Model.Provider;
import io.realm.Realm;
import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 21-10-17.
 */

public class Nav_central extends Fragment implements MySendView{
    public static final String TAG = "Nav_central";
    public static final String TAG_ID = "Id_Provider";
    public static final String PRODUCT_UNIQUE = "Prod Unitarios";
    public static final String PRODUCT_PACKAGE = "Prod Por paquete";
    public static final String MAIN = "MainActivity";
    public static final String CART = "ShopingCart";

    private RecyclerView recyclerView;
    private AdapterNav adapterNav;
    private List<RealmObject> itemList, itemListMain, itemListUnique, itemListPackage;
    private Nav_central nav_central = null;
    private View rootView;
    private AlertDialog LoadDialog;
    private int idProvider;
    private ApiInterface apiInterface;
    private int controlThreadRetrofit = 0;
    private Realm realm;
    private QueryRealm queryRealm;
    private UpdateCountShoppingCart updateCountShoppingCart;
    private UpdateValues updateValues;

    static public Nav_central createInstance(String fragmentName, int idProvider){
        Nav_central nav_central = new Nav_central();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, fragmentName);
        bundle.putInt(TAG_ID, idProvider);
        Log.e(TAG, idProvider+"");
        nav_central.setArguments(bundle);
        return nav_central;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nav, container, false);
        itemList = new ArrayList<>();
        LoadDialog = new SpotsDialog(getActivity());
        LoadDialog.show();
        realm = Realm.getDefaultInstance();
        queryRealm = new QueryRealm(realm);
        idProvider = getArguments().getInt(TAG_ID, 0);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LoadAllData();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProductsActivity)
            updateCountShoppingCart = (UpdateCountShoppingCart) context;
        else if (context instanceof ShoppingCartActivity)
            updateValues = (UpdateValues) context;
    }

    private void LoadAllData(){
        switch (getArguments().getString(TAG)){
            case MAIN:
                LoadMainDataServer();
                break;
            case CART:
                LoadDataShoppingCart();
                break;
            default:
                LoadProductDataServer(getArguments().getString(TAG));
        }
    }

    //return actual nav_central
    public void setNav_central(Nav_central nav_central){
        this.nav_central = nav_central;
    }

    private void LoadDatainRecycler(String key){
        switch (key){
            case MAIN:
                LoadRecyclerMainCart(MAIN);
                break;
            case CART:
                LoadRecyclerMainCart(CART);
                break;
            default:
                LoadRecycLerProduct();
                adapterNav = new AdapterNav(itemList, getActivity(), queryRealm, updateCountShoppingCart);
                recyclerView.setAdapter(adapterNav);
                break;
        }
    }

    //return actual list in the adapter
    public List<RealmObject> getAdapterNavListItem(){
        return adapterNav.getItemList();
    }

    //load data in tabs
    private void LoadRecycLerProduct() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), false));
    }

    //Load simple recycler
    private void LoadRecyclerMainCart(String tag){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        switch (tag){
            case MAIN: adapterNav = new AdapterNav(itemList, getActivity());break;
            case CART: adapterNav = new AdapterNav(itemList, getActivity(), realm, updateValues);
        }

        recyclerView.setAdapter(adapterNav);
    }

    //return old values
    public void returnToTheValues(){
        String key = nav_central.getArguments().getString(Nav_central.TAG);
        if (nav_central == null || key.equals(MAIN)){
            adapterNav.CLear();
            adapterNav.addAll(itemListMain);
        }else if (nav_central == null || key.equals(CART)){
            adapterNav.CLear();
        }
        else {
            switch (key){
                case PRODUCT_PACKAGE:
                    adapterNav.CLear();
                    adapterNav.addAll(itemListPackage);
                    break;
                case PRODUCT_UNIQUE:
                    adapterNav.CLear();
                    adapterNav.addAll(itemListUnique);
                    break;
            }
        }
    }

    //2 rows in recyclerView
    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        private GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // position
            int column = position % spanCount; // column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; //  top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //filter data in recyclerView
    @Override
    public void filter(List<RealmObject> models, String query, Nav_central nav_central) {
        this.nav_central = nav_central;
        this.itemList = filterData(models, query, nav_central);
        adapterNav.setFilter(itemList);
    }

    private List<RealmObject> filterData(List<RealmObject> models, String query, Nav_central nav_central) {
        final List<RealmObject> filteredModelList = new ArrayList<>();
        query = query.toLowerCase();
        String key = String.valueOf(nav_central.getArguments().get(Nav_central.TAG));
        switch (key) {
            case Nav_central.MAIN:
                for (int i = 0; i < models.size(); i++) {
                    Provider provider = (Provider) models.get(i);
                    final String text = provider.getBrand().toLowerCase();
                    if (text.contains(query)) {
                        filteredModelList.add(provider);
                    }
                }
                break;
            case PRODUCT_PACKAGE:
                for (int i = 0; i < models.size(); i++) {
                    Producto producto = (Producto) models.get(i);
                    final String text = producto.getProductName().toLowerCase();
                    if (text.contains(query)) {
                        filteredModelList.add(producto);
                    }
                }
                break;
            case PRODUCT_UNIQUE:
                Log.e(TAG, query);
                for (int i = 0; i < models.size(); i++) {
                    Producto producto = (Producto) models.get(i);
                    final String text = producto.getProductName().toLowerCase();
                    if (text.contains(query)) {
                        filteredModelList.add(producto);
                    }
                }
                break;
        }

        return filteredModelList;
    }
    //call retrofit for data
    //call provider
    private void LoadMainDataServer(){
        if (ExtraFunctions.Conexion(getActivity())){
            final Call<ResponseProvider> responseProvider = apiInterface.getProviderCall();
            responseProvider.enqueue(new Callback<ResponseProvider>() {
                @Override
                public void onResponse(@NonNull Call<ResponseProvider> call, @NonNull retrofit2.Response<ResponseProvider> response) {
                    controlThreadRetrofit++;
                    itemList.addAll(response.body().getData());
                    itemListMain = itemList;
                    ShowList(MAIN);
                }
                @Override
                public void onFailure(Call<ResponseProvider> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(getActivity(), R.string.connection, Toast.LENGTH_LONG).show();
        }
    }

    private void LoadDataShoppingCart(){
        controlThreadRetrofit++;//sincroniza los hilos
        itemList.addAll(queryRealm.getListPurchases());
        ShowList(CART);
    }

    //call retrofit for data
    //call products
    private void LoadProductDataServer(final String key){
        if (ExtraFunctions.Conexion(getActivity())){
            Call<ResponseProduct> responseProductCall = apiInterface.getProductCall(idProvider);
            Log.e(TAG, responseProductCall.request().toString());
            responseProductCall.enqueue(new Callback<ResponseProduct>() {
                @Override
                public void onResponse(@NonNull Call<ResponseProduct> call, @NonNull Response<ResponseProduct> response) {
                    controlThreadRetrofit++;
                    switch (key){
                        case PRODUCT_UNIQUE:
                            for (int i = 0; i< response.body().getData().size(); i++){
                                Producto producto = response.body().getData().get(i);
                                if (producto.getPackageQuantity() == 0) itemList.add(producto);
                            }
                            itemListUnique = itemList;
                            break;
                        case PRODUCT_PACKAGE:
                            for (int i = 0; i< response.body().getData().size(); i++){
                                Producto producto = response.body().getData().get(i);
                                if (producto.getPackageQuantity() != 0) itemList.add(producto);
                            }
                            itemListPackage = itemList;
                            break;
                    }

                    ShowList(key);
                }
                @Override
                public void onFailure(Call<ResponseProduct> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(getActivity(), R.string.connection, Toast.LENGTH_LONG).show();
        }
    }

    private synchronized void ShowList(String key){
        if (controlThreadRetrofit == 1) {
            LoadDatainRecycler(key);
            LoadDialog.dismiss();
        }
    }
}

