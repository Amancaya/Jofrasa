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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import free.tech.jofrasa.Adapters.AdapterNav;

/**
 * Created by root on 21-10-17.
 */

public class Nav_central extends Fragment {
    public static final String TAG = "Nav_central";
    public static final String PRODUCT_UNIQUE = "Unitarios";
    public static final String PRODUCT_PACKAGE = "Por paquete";
    public static final String MAIN = "MainActivity";

    View rootView;
    RecyclerView recyclerView;

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


    private void LoadData(){
        switch (getArguments().getString(TAG)){
            case MAIN:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new AdapterNav(StaticData.ListOfProviders(), getActivity()));
                break;
            case PRODUCT_UNIQUE:
                LoadRecycLerProduct();
                recyclerView.setAdapter(new AdapterNav((StaticData.LisProductUnique()), getActivity()));
                break;
            case PRODUCT_PACKAGE:
                LoadRecycLerProduct();
                recyclerView.setAdapter(new AdapterNav((StaticData.ListProduct()), getActivity()));
                break;
        }
    }

    private void LoadRecycLerProduct(){

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), false));
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
