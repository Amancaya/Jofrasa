package free.tech.jofrasa;



import android.app.AlertDialog;
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
import free.tech.jofrasa.Adapters.AdapterNav;
import free.tech.jofrasa.ExtraClass.ApiClient;
import free.tech.jofrasa.ExtraClass.ExtraFunctions;
import free.tech.jofrasa.Response.ResponseProvider;
import free.tech.jofrasa.Interface.ApiInterface;
import free.tech.jofrasa.Interface.SendView;
import free.tech.jofrasa.Model.Producto;
import free.tech.jofrasa.Model.Provider;
import free.tech.jofrasa.Model.item;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by root on 21-10-17.
 */

public class Nav_central extends Fragment implements SendView{
    public static final String TAG = "Nav_central";
    public static final String TAG_ID = "Id_Provider";
    public static final String PRODUCT_UNIQUE = "Prod Unitarios";
    public static final String PRODUCT_PACKAGE = "Prod Por paquete";
    public static final String MAIN = "MainActivity";

    private RecyclerView recyclerView;
    private AdapterNav adapterNav;
    private List<item> itemList, itemListPackageInitial, itemListMain, itemListUnique, itemListPackage;
    private Nav_central nav_central = null;
    private View rootView;
    private AlertDialog LoadDialog;
    private int idProvider;
    private ApiInterface apiInterface;
    private int controlThreadRetrofit = 0;

    static public Nav_central createInstance(String fragmentName, int idProvider){
        Nav_central nav_central = new Nav_central();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, fragmentName);
        bundle.putInt(TAG_ID, idProvider);
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
        idProvider = getArguments().getInt(TAG_ID, 0);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LoadMainDataServer();
        return rootView;
    }

    //return actual nav_central
    public void setNav_central(Nav_central nav_central){
        this.nav_central = nav_central;
    }

    private void LoadDatainRecycler(){
        switch (getArguments().getString(TAG)){
            case MAIN:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapterNav = new AdapterNav(itemList, getActivity());
                recyclerView.setAdapter(adapterNav);
                break;
            case PRODUCT_UNIQUE:
                itemListUnique = itemList;
                LoadRecycLerProduct();
                adapterNav = new AdapterNav(itemList, getActivity());
                recyclerView.setAdapter(adapterNav);
                break;
            case PRODUCT_PACKAGE:
                itemListPackage = itemList;
                LoadRecycLerProduct();
                adapterNav = new AdapterNav(itemList, getActivity());
                recyclerView.setAdapter(adapterNav);
                break;
        }
    }

    //return actual list in the adapter
    public List<item> getAdapterNavListItem(){
        return adapterNav.getItemList();
    }

    //load data in tabs
    private void LoadRecycLerProduct(){
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), false));
    }

    //return old values
    public void returnToTheValues(){
        String key = nav_central.getArguments().getString(Nav_central.TAG);
        adapterNav.CLear();
        if (nav_central == null || key.equals(MAIN)){
            Log.e(TAG, itemList.toString());
            adapterNav.addAll(itemListMain);
        }else {
            switch (key){
                case PRODUCT_PACKAGE: adapterNav.addAll(itemListPackage);break;
                case PRODUCT_UNIQUE: adapterNav.addAll(itemListUnique);break;
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
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
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
    public void filter(List<item> models, String query, Nav_central nav_central) {
        this.nav_central = nav_central;
        this.itemList = filterData(models, query, nav_central);
        adapterNav.setFilter(itemList);
    }

    private List<item> filterData(List<item> models, String query, Nav_central nav_central) {
        final List<item> filteredModelList = new ArrayList<>();
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
                    final String text = producto.getProduct_name().toLowerCase();
                    if (text.contains(query)) {
                        filteredModelList.add(producto);
                    }
                }
                break;
            case PRODUCT_UNIQUE:
                Log.e(TAG, query);
                for (int i = 0; i < models.size(); i++) {
                    Producto producto = (Producto) models.get(i);
                    final String text = producto.getProduct_name().toLowerCase();
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
                    for (int i =0; i< response.body().getData().size(); i++){
                        Provider provider = response.body().getData().get(i);
                        Log.e(TAG, provider.getImage());
                        itemList.add(provider);
                    }
                    ShowList();
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

    private synchronized void ShowList(){
        if (controlThreadRetrofit == 1) {
            LoadDatainRecycler();
            LoadDialog.dismiss();
        }
    }

}

