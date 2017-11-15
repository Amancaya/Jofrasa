package free.tech.jofrasa.ExtraClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by root on 01-11-17.
 */

public class ExtraFunctions {
    public static boolean Conexion(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info_wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo info_datos = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(String.valueOf(info_wifi.getState()).equals("CONNECTED")){
            return true;
        } else {
            if (String.valueOf(info_datos.getState()).equals("CONNECTED")) {
                return true;
            }else {
                return false;
            }
        }
    }


}
