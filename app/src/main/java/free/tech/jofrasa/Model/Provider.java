package free.tech.jofrasa.Model;

/**
 * Created by root on 21-10-17.
 */

public class Provider extends item{
    private int id;
    private int image;
    private String brand;


    public Provider() {
    }

    public Provider(int id, int image, String brand) {
        this.id = id;
        this.image = image;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
