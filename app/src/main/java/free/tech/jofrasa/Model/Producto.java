package free.tech.jofrasa.Model;

/**
 * Created by root on 21-10-17.
 */

public class Producto extends item {
    private int id;
    private String product_name;
    private double price;
    private double package_price;
    private int package_quantity;
    private int photo;
    private int id_provider;


    public Producto() {
    }

    public Producto(int id, String product_name, double package_price, int package_quantity, int photo) {
        this.id = id;
        this.product_name = product_name;
        this.package_price = package_price;
        this.package_quantity = package_quantity;
        this.photo = photo;
    }

    public Producto(int id, String product_name, double price, int photo) {
        this.id = id;
        this.product_name = product_name;
        this.price = price;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPackage_price() {
        return package_price;
    }

    public void setPackage_price(double package_price) {
        this.package_price = package_price;
    }

    public int getPackage_quantity() {
        return package_quantity;
    }

    public void setPackage_quantity(int package_quantity) {
        this.package_quantity = package_quantity;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getId_provider() {
        return id_provider;
    }

    public void setId_provider(int id_provider) {
        this.id_provider = id_provider;
    }
}
