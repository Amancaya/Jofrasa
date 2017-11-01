package free.tech.jofrasa.Interface;

import java.util.List;

import free.tech.jofrasa.Adapters.AdapterNav;
import free.tech.jofrasa.Model.item;
import free.tech.jofrasa.Nav_central;

/**
 * Created by root on 26-10-17.
 */

public interface SendView {
    public void filter(List<item> models, String query, Nav_central nav_central);
}
