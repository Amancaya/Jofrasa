package free.tech.jofrasa.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import free.tech.jofrasa.Activitys.ProductsActivity;
import free.tech.jofrasa.ExtraClass.ImageDialog;
import free.tech.jofrasa.Interface.ItemclickListener;
import free.tech.jofrasa.Model.Producto;
import free.tech.jofrasa.Model.Provider;
import free.tech.jofrasa.Model.item;
import free.tech.jofrasa.R;

/**
 * Created by root on 21-10-17.
 */

public class AdapterNav extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemclickListener{
    private static final int TYPE_PROVIDER = 1;
    private static final int TYPE_PRODUCT = 2;

    private Activity activity;
    private List<item> itemList;

    public AdapterNav(List<item> itemList, Activity activity){
        this.activity = activity;
        this.itemList = itemList;
    }

    public void addAll(List<item> itemList){
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
        }else {
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
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PROVIDER){
            ((ProviderHolder)holder).binData((Provider)itemList.get(position));
        }else if (getItemViewType(position) == TYPE_PRODUCT){
            ((ProductHolder)holder).binData((Producto)itemList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (getItemViewType(position)){
            case TYPE_PROVIDER:
                ProductsActivity.createInstance(activity, (Provider)itemList.get(position)); break;
            case TYPE_PRODUCT:
                switch (view.getId()){
                    case R.id.image:
                        Log.e("Adapter", "Imagen");
                        Producto producto = (Producto) itemList.get(position);
                        ImageDialog.newInstance(producto.getPhoto()).show(activity.getFragmentManager(), null);;
                        break;
                    case R.id.button_car:
                        Log.e("Adapter", "button_car");
                        break;
                }
                break;
        }
    }

    public void setFilter(List<item> itemFilter) {
        itemList = new ArrayList<>();
        itemList.addAll(itemFilter);
        notifyDataSetChanged();
    }

    public List<item> getItemList(){
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
}
