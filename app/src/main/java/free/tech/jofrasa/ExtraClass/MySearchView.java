package free.tech.jofrasa.ExtraClass;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;

import java.util.List;

import free.tech.jofrasa.Nav_central;

/**
 * Created by root on 30-10-17.
 */

public class MySearchView extends SearchView {
    Nav_central nav_central ;
    List<Fragment> fragmentList;
    public MySearchView(Activity context, Nav_central fragment) {
        super(context);
        nav_central = fragment;
    }

    public MySearchView(Activity context, List<Fragment> fragmentList){
        super(context);
        this.fragmentList = fragmentList;
    }

    public void SetPosition(int i){
        nav_central = (Nav_central) fragmentList.get(i);
    }
        // The normal SearchView doesn't clear its search text when
        // collapsed, so we will do this for it.
    @Override
    public void onActionViewCollapsed() {
         setQuery("", false);
         nav_central.returnToTheValues();
         super.onActionViewCollapsed();
    }


}
