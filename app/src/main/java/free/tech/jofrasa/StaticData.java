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
