package free.tech.jofrasa;


import java.util.ArrayList;
import java.util.List;

import free.tech.jofrasa.Model.Purchase;
import free.tech.jofrasa.Model.Producto;
import free.tech.jofrasa.Model.Provider;
import free.tech.jofrasa.Model.item;

/**
 * Created by root on 21-10-17.
 */

public class StaticData {

    public static List<item> ListProduct(){
        List<item> itemList = new ArrayList<>();
        Producto producto = new Producto(1, "YACOBS Frutillas al Jugo 12x850g", 384, 12, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(1, "YACOBS Frutillas al Jugo 12x850g", 384, 12, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(2, "YLIZ Shampoo Aloe Vera y Pepino 12x950mL", 255, 5, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(3, "OLA Brillante Lustramuebles Orig 12x850mL", 384, 12, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(4, "OLA Brillante Betun Liquido Neutro 12x60mL", 384, 12, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(5, "BEBIN PREMIUM  Panales 4x38  S", 260, 4, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(6, "OLA Pack Jabon de Lavar Floral 10x5x180g", 384, 12, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        return itemList;
    }

    public static List<item> LisProductUnique(){
        List<item> itemList = new ArrayList<>();
        Producto producto = new Producto(1, "Fideo 800 grs Paloma", 3.50, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(1, "Fideo Cortado 10 kilos Paloma", 70, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(1, "Aceite Refinado de soya en bidon de 4.5 lts.", 17.5, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(1, "OLA Pack Jabon de Lavar Floral 10x5x180g", 9, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(1, "Harina Paloma de 50 kilos", 181, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        producto = new Producto(1, "Fideo 800 grs Paloma", 3.50, R.drawable.yacobs_ciruela);
        itemList.add(producto);
        return itemList;
    }

    public static List<Purchase> ListPurchase(){
        List<Purchase> purchaseList = new ArrayList<>();
        Purchase purchase = new Purchase("Harina Paloma 1 kilo", 3, 17.50);
        purchaseList.add(purchase);
        purchase = new Purchase("OLA Poder Activo Remoción Floral 15x850g", 2, 22);
        purchaseList.add(purchase);
        purchase = new Purchase("OLA Pack Jabon de Lavar Floral 10x5x180g", 3, 360);
        purchaseList.add(purchase);
        purchase = new Purchase("LIZ Shampoo Anticaspa 12x900mL", 3, 42);
        purchaseList.add(purchase);
        purchase = new Purchase("YACOBS LLajwa 12x250g", 1, 13);
        purchaseList.add(purchase);
        purchase = new Purchase("YACOBS Aceite de Oliva Clasico 12x500mL", 3, 34.5);
        purchaseList.add(purchase);
        purchase = new Purchase("YACOBS Aceite de Oliva Premium 12x500mL", 2, 96);
        purchaseList.add(purchase);
        purchase = new Purchase("Liz Jab Tocador Lavanda Relajante 72x125g", 1, 360);
        purchaseList.add(purchase);
        return purchaseList;
    }
}
