package free.tech.jofrasa.ExtraClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import free.tech.jofrasa.Model.Provider;
import free.tech.jofrasa.Model.item;

/**
 * Created by root on 01-11-17.
 */

public class Response {

    @SerializedName("data")
    @Expose
    private List<Provider> data = null;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("response")
    @Expose
    private Boolean response;

    public List<Provider> getData() {
        return data;
    }

    public void setData(List<Provider> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

}
