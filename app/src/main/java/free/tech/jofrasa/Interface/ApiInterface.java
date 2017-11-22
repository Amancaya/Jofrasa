package free.tech.jofrasa.Interface;

import free.tech.jofrasa.ExtraClass.Model.Client;
import free.tech.jofrasa.Model.Purchase;
import free.tech.jofrasa.Response.ResponseProduct;
import free.tech.jofrasa.Response.ResponseProvider;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by root on 31-10-17.
 */

public interface ApiInterface {
    @GET("provider/listProviders/")
    Call<ResponseProvider> getProviderCall();

    @GET("provider/productsProvider/{providerId}")
    Call<ResponseProduct> getProductCall(@Path("providerId") int id);

    @POST("client/insertClient/")
    Call<Client> savePost(@Body Client client);

    @POST("client/insertShopping/")
    @FormUrlEncoded
    Call<Purchase> saveProduct(@Field("_id_product") int _id_product,
                          @Field("_nit") String _nit,
                          @Field("_quantity") int _quantity);


    @GET("client/verifyEmail/{email}")
    Call<Client> verifyEmail(@Path("email") String email);

}
