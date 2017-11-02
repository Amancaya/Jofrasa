package free.tech.jofrasa.Interface;

import free.tech.jofrasa.ExtraClass.Model.Client;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by root on 31-10-17.
 */

public interface ApiInterface {
//    @GET("location/listLodgings/")
//    Call<HotelMessage> getHotelMessageCall();

    @POST("client/insertClient/")
    @FormUrlEncoded
    Call<Client> savePost(@Field("_name") String _name,
                          @Field("_surname") String _surname,
                          @Field("_nit") String _nit,
                          @Field("_email") String _email,
                          @Field("_adress") String _adress,
                          @Field("_cell_number") String _cell_number,
                          @Field("_phone_number") String _phone_number);
}
