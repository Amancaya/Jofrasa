package free.tech.jofrasa.Interface;

import java.util.List;


import free.tech.jofrasa.Nav_central;
import io.realm.RealmObject;

/**
 * Created by root on 26-10-17.
 */

public interface MySendView {
    public void filter(List<RealmObject> models, String query, Nav_central nav_central);
}
