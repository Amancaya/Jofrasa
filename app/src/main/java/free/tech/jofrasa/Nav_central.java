package free.tech.jofrasa;



import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
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
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import free.tech.jofrasa.Adapters.AdapterNav;
import free.tech.jofrasa.Interface.SendView;
import free.tech.jofrasa.Model.Producto;
import free.tech.jofrasa.Model.Provider;
import free.tech.jofrasa.Model.item;

/**
 * Created by root on 21-10-17.
 */

public class Nav_central extends Fragment implements SendView{
    public static final String TAG = "Nav_central";
    public static final String PRODUCT_UNIQUE = "Prod Unitarios";
    public static final String PRODUCT_PACKAGE = "Prod Por paquete";
    public static final String MAIN = "MainActivity";

    private RecyclerView recyclerView;
    private AdapterNav adapterNav;
    private List<item> itemList;
    private Nav_central nav_central = null;
    private View rootView;

    static public Nav_central createInstance(String fragmentName){
        Nav_central nav_central = new Nav_central();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, fragmentName);
        nav_central.setArguments(bundle);
        return nav_central;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nav, container, false);
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LoadData();
        return rootView;
    }

    public void setNav_central(Nav_central nav_central){
        this.nav_central = nav_central;
    }

    private void LoadData(){
        switch (getArguments().getString(TAG)){
            case MAIN:
                itemList = StaticData.ListOfProviders();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapterNav = new AdapterNav(itemList, getActivity());
                recyclerView.setAdapter(adapterNav);
                break;
            case PRODUCT_UNIQUE:
                itemList = StaticData.LisProductUnique();
                LoadRecycLerProduct();
                adapterNav = new AdapterNav(itemList, getActivity());
                recyclerView.setAdapter(adapterNav);
                break;
            case PRODUCT_PACKAGE:
                itemList = StaticData.ListProduct();
                LoadRecycLerProduct();
                adapterNav = new AdapterNav(itemList, getActivity());
                recyclerView.setAdapter(adapterNav);
                break;
        }
    }

    public List<item> getAdapterNavListItem(){
        return adapterNav.getItemList();
    }

    public void returnToTheValues(){
        String key = nav_central.getArguments().getString(Nav_central.TAG);
        adapterNav.CLear();
        if (nav_central == null || key.equals(MAIN)){
            Log.e(TAG, itemList.toString());
            adapterNav.addAll(StaticData.ListOfProviders());
        }else {
            switch (key){
                case PRODUCT_PACKAGE: adapterNav.addAll(StaticData.ListProduct());break;
                case PRODUCT_UNIQUE: adapterNav.addAll(StaticData.LisProductUnique());break;
            }
        }
    }

    private void LoadRecycLerProduct(){
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), false));
    }

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
}
