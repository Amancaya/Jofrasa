package free.tech.jofrasa.Activitys;

import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import free.tech.jofrasa.Model.Purchase;
import free.tech.jofrasa.Nav_central;
import free.tech.jofrasa.R;
import free.tech.jofrasa.StaticData;

public class ShoppingCartActivity extends AppCompatActivity {

    private TableLayout tabLayout;
    private TableRow tableRow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        tabLayout = (TableLayout) findViewById(R.id.tablayout);
        insertValueToRow(StaticData.ListPurchase());
    }

    private void insertValueToRow(List<Purchase> purchases){
        double total = 0;
        for (Purchase purchase: purchases) {
            total = total + purchase.getPrice();
            createRows(purchase);
        }

        createExtraRows(total);
    }

    private void createRows(Purchase purchase){
        tableRow = new TableRow(this);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.column = 0;
        layoutParams.weight = Float.parseFloat("0.4");
        TextView txtName = createTextViewText(layoutParams, purchase.getName(), Gravity.START);
        tableRow.addView(txtName);

        layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.column = 2;
        layoutParams.weight = Float.parseFloat("0.25");
        TextView txtQuantity = createTextViewText(layoutParams, String.valueOf(purchase.getQuantity()), Gravity.CENTER);
        tableRow.addView(txtQuantity);

        layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.column = 3;
        layoutParams.weight = Float.parseFloat("0.25");
        TextView txtPrice = createTextViewText(layoutParams, String.valueOf(purchase.getPrice()), Gravity.END);
        tableRow.addView(txtPrice);

        tabLayout.addView(tableRow);
    }

    private void createExtraRows(double total){
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        View view = new View(this);
        view.setLayoutParams(layoutParams);
        view.setBackground(getResources().getDrawable(R.color.divider));
        tabLayout.addView(view);

        //fila del subtotal de la lista
        tableRow = new TableRow(this);
        layoutParams = new LayoutParams(2);
        tableRow.addView(createTextViewText(layoutParams, "Subtotal", Gravity.START));

        layoutParams = new LayoutParams(3);
        tableRow.addView(createTextViewText(layoutParams, String.valueOf(total), Gravity.END));

        tabLayout.addView(tableRow);

        //fila del costo de envio
        tableRow = new TableRow(this);
        layoutParams = new LayoutParams(2);
        tableRow.addView(createTextViewText(layoutParams, "Costo de envio", Gravity.START));

        layoutParams = new LayoutParams(3);
        tableRow.addView(createTextViewText(layoutParams, "10.0", Gravity.END));

        tabLayout.addView(tableRow);

        //fila del total a pagar
        tableRow = new TableRow(this);
        layoutParams = new LayoutParams(2);
        tableRow.addView(createTextViewText(layoutParams, "Total", Gravity.START));

        layoutParams = new LayoutParams(3);
        TextView txtTotalPrice = createTextViewText(layoutParams, String.valueOf((total+10)), Gravity.END);
        tableRow.addView(txtTotalPrice);

        tabLayout.addView(tableRow);

    }

    private TextView createTextViewText(LayoutParams layoutParams, String value, int gravity){
        TextView textView = new TextView(this);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(3,3, 3, 3);
        textView.setText(value);
        textView.setGravity(gravity);
        return textView;
    }

}
