package free.tech.jofrasa.ExtraClass;

import free.tech.jofrasa.Interface.ApiInterface;

/**
 * Created by benji on 31/10/17.
 */

public class ApiUtils {
    private ApiUtils() {}

   // public static final String BASE_URL = "http://jsonplaceholder.typicode.com/";

    public static ApiInterface getAPIService() {

        return ApiClient.getClient().create(ApiInterface.class);
    }
}
