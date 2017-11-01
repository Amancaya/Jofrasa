package free.tech.jofrasa.ExtraClass.Model;

/**
 * Created by benji on 31/10/17.
 */
    import com.google.gson.annotations.Expose;
    import com.google.gson.annotations.SerializedName;

public class Client {

    @SerializedName("_name")
    @Expose
    private String name;
    @SerializedName("_surname")
    @Expose
    private String surname;
    @SerializedName("_nit")
    @Expose
    private String nit;
    @SerializedName("_email")
    @Expose
    private String email;
    @SerializedName("_adress")
    @Expose
    private String adress;
    @SerializedName("_cell_number")
    @Expose
    private String cellNumber;
    @SerializedName("_phone_number")
    @Expose
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nit='" + nit + '\'' +
                ", email='" + email + '\'' +
                ", adress='" + adress + '\'' +
                ", cellNumber='" + cellNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}