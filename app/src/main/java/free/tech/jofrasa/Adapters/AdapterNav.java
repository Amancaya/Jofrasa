package free.tech.jofrasa.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import free.tech.jofrasa.Activitys.ProductsActivity;
import free.tech.jofrasa.ExtraClass.ImageDialog;
import free.tech.jofrasa.ExtraClass.QueryRealm;
import free.tech.jofrasa.Interface.ItemclickListener;
import free.tech.jofrasa.Interface.UpdateCountShoppingCart;
import free.tech.jofrasa.Interface.UpdateValues;
import free.tech.jofrasa.Model.Producto;
import free.tech.jofrasa.Model.Provider;
import free.tech.jofrasa.Model.Purchase;
import free.tech.jofrasa.R;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by root on 21-10-17.
 */

public class AdapterNav extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemclickListener{
    private static final int TYPE_PROVIDER = 1;
    private static final int TYPE_PRODUCT = 2;
    private static final int TYPE_PURCHASE = 3;
    private static final String TAG = "AdapterNav";

    private Activity activity;
    private List<RealmObject> itemList;
    private Realm realm;
    private QueryRealm queryRealm;
    private UpdateCountShoppingCart updateCountShoppingCart;
    private UpdateValues updateValues;

    public AdapterNav(List<RealmObject> itemList, Activity activity){
        this.activity = activity;
        this.itemList = itemList;
    }

    public AdapterNav(List<RealmObject> itemList, Activity activity, Realm realm, UpdateValues updateValues){
        this.activity = activity;
        this.itemList = itemList;
        this.realm = realm;
        this.updateValues = updateValues;//actualiza los datos de subtotal y total
        queryRealm = new QueryRealm(realm);
    }
    public AdapterNav(List<RealmObject> itemList, Activity activity, QueryRealm queryRealm,
                      UpdateCountShoppingCart updateCountShoppingCart){
        this.activity = activity;
        this.itemList = itemList;
        realm = Realm.getDefaultInstance();
        this.queryRealm = queryRealm;
        this.updateCountShoppingCart = updateCountShoppingCart;
    }

    public void addAll(List<RealmObject> itemList){
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void CLear(){
        this.itemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof Provider){
            return TYPE_PROVIDER;
        }else if (itemList.get(position) instanceof Producto){
            return TYPE_PRODUCT;
        }else if (itemList.get(position) instanceof Purchase){
            return TYPE_PURCHASE;
        }
        else {
            throw new RuntimeException("ItemViewType, unknown");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case TYPE_PROVIDER:
                viewHolder = new ProviderHolder(inflater.inflate(R.layout.item_nav, parent, false), this, activity);
                break;
            case TYPE_PRODUCT:
                viewHolder = new ProductHolder(inflater.inflate(R.layout.item_product, parent, false), activity, this);
                break;
            case TYPE_PURCHASE:
                viewHolder = new PurchaseHolder(inflater.inflate(R.layout.item_pre_shopping_cart, parent, false), this, activity);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PROVIDER){
            ((ProviderHolder)holder).binData((Provider)itemList.get(position));
        }else if (getItemViewType(position) == TYPE_PRODUCT){
            ((ProductHolder)holder).binData((Producto)itemList.get(position));
        }else if (getItemViewType(position) == TYPE_PURCHASE){
            ((PurchaseHolder)holder).binData((Purchase)itemList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    private double reCaculatePrice(Purchase purchase){
        return purchase.getPrice()/purchase.getQuantity();
    }

    @Override
    public void onItemClick(View view, final int position) {
        switch (getItemViewType(position)){
            case TYPE_PROVIDER:
                ProductsActivity.createInstance(activity, (Provider)itemList.get(position)); break;
            case TYPE_PRODUCT:
                final Producto producto = (Producto) itemList.get(position);
                switch (view.getId()){
                    case R.id.image:
                        Log.e("Adapter", "Imagen");
                        ImageDialog.newInstance(producto.getPhoto()).show(activity.getFragmentManager(), null);
                        break;
                    case R.id.button_car:
                        Log.e("Adapter", "button_car");
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Purchase purchase = realm.createObject(Purchase.class, UUID.randomUUID().toString());
                                purchase.setIdProduct(producto.getId());
                                purchase.setName(producto.getProductName());
                                purchase.setPhoto(producto.getPhoto());
                                purchase.setPrice(Double.parseDouble(producto.getPrice()));
                                purchase.setQuantity(1);
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(activity, R.string.insert, Toast.LENGTH_LONG).show();
                                updateCountShoppingCart.UpdateCount(queryRealm.countCart());
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                Log.e(TAG, error.getMessage());
                                Toast.makeText(activity, R.string.Notinsert, Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                }
                break;

            case TYPE_PURCHASE:
                final Purchase purchase = (Purchase) itemList.get(position);
                switch (view.getId()){
                    case R.id.button_up:
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                int quantity = purchase.getQuantity();
                                double price = reCaculatePrice(purchase);
                                purchase.setQuantity(quantity+1);
                                purchase.setPrice((quantity+1)*price);
                                CLear();
                                addAll(queryRealm.getListPurchases());
                                updateValues.ChangeValues();
                            }
                        });
                        break;
                    case R.id.button_down:
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                int quantity = purchase.getQuantity();
                                double price = reCaculatePrice(purchase);
                                if (quantity > 1){
                                    purchase.setQuantity(quantity-1);
                                    purchase.setPrice((quantity-1)*price);
                                }
                                CLear();
                                addAll(queryRealm.getListPurchases());
                                updateValues.ChangeValues();
                            }
                        });
                        break;
                    case R.id.button_delete:

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if (purchase.isValid()) {
                                    purchase.deleteFromRealm();
                                    CLear();
                                    addAll(queryRealm.getListPurchases());
                                    updateValues.ChangeValues();
                                } else Log.e("executeTransaction", "no valido");
                            }
                        });
                        break;
                }
                break;
        }
    }

    public void setFilter(List<RealmObject> itemFilter) {
        itemList = new ArrayList<>();
        itemList.addAll(itemFilter);
        notifyDataSetChanged();
    }

    public List<RealmObject> getItemList(){
        return itemList;
    }

    private static class ProviderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView id_provider;
        Activity activity;
        private ItemclickListener listener;
        public ProviderHolder(View itemView, ItemclickListener listener, Activity activity) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture);
            id_provider = itemView.findViewById(R.id.id_provider);
            this.listener = listener;
            this.activity = activity;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }

        private void binData(Provider provider){
           id_provider.setText(String.valueOf(provider.getId()));
           Picasso.with(activity).load(provider.getImage()).into(imageView);
        }
    }

    private static class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        LinearLayout button_car;
        TextView title, price, count;
        Producto producto;
        Activity activity;
        ItemclickListener listener;
        public ProductHolder(View itemView, Activity activity, ItemclickListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            button_car = itemView.findViewById(R.id.button_car);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            count = itemView.findViewById(R.id.count);

            image.setOnClickListener(this);
            button_car.setOnClickListener(this);

            this.activity = activity;
            this.listener = listener;
        }


        private void binData(Producto producto){
            this.producto = producto;
            Picasso.with(activity).load(producto.getPhoto()).into(image);
            title.setText(producto.getProductName());
            if (producto.getPackageQuantity() == 0){
                count.setVisibility(View.GONE);
                price.setText("Bs "+String.valueOf(producto.getPrice()));
            }
            else {
                price.setText("Bs "+String.valueOf(producto.getPrice()));
                count.setVisibility(View.VISIBLE);
                count.setText(String.valueOf(producto.getPackageQuantity())+" Unidades");
            }
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }

    private static class PurchaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image_product, button_up, button_down, button_delete;
        TextView name_product, price_product;
        EditText quantity_product;
        ItemclickListener listener;
        Activity activity;

        PurchaseHolder(View itemView, ItemclickListener listener, Activity activity) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            button_up = itemView.findViewById(R.id.button_up);
            button_down = itemView.findViewById(R.id.button_down);
            button_delete = itemView.findViewById(R.id.button_delete);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            quantity_product = itemView.findViewById(R.id.quantity_product);

            button_delete.setOnClickListener(this);
            button_down.setOnClickListener(this);
            button_up.setOnClickListener(this);

            this.listener = listener;
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            this.listener.onItemClick(view, getAdapterPosition());
        }

        private void binData(Purchase purchase){
            Picasso.with(activity).load(purchase.getPhoto()).into(image_product);
            name_product.setText(purchase.getName());
            price_product.setText("Bs "+String.valueOf(purchase.getPrice()));
            quantity_product.setText(String.valueOf(purchase.getQuantity()));
        }
    }
}
