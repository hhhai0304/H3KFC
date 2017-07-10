package vn.name.hohoanghai.util;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import vn.name.hohoanghai.h3kfc.R;
import vn.name.hohoanghai.model.Product;

/**
 * Created by hhhai0304 on 12-06-2016.
 */
public class DishDetailDialog extends AlertDialog {

    Activity context;
    Product product;

    ImageView imgDishDialog;
    TextView txtDishName, txtPrice, txtDetail;
    Button btnBack, btnAddToCart;

    public DishDetailDialog(Activity context, Product product) {
        super(context);
        this.context = context;
        this.product = product;
    }

    public void createDishDetailDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_dish_detail, null);
        builder.setView(view);

        Utility utility = new Utility();
        imgDishDialog = (ImageView) view.findViewById(R.id.imgDishDialog);
        txtDishName = (TextView) view.findViewById(R.id.txtDishName);
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtDetail = (TextView) view.findViewById(R.id.txtDetail);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage).build();
        imageLoader.displayImage(product.getAvatar(), imgDishDialog, options);

        txtDishName.setText(product.getProductName());
        txtPrice.setText(utility.currencyFormat(product.getPrice()));
        String newline = System.getProperty("line.separator");
        String detail;
        Log.i("ID", product.getCategoryID());
        if(product.getCategoryID().equals("CA01")) {
            detail = "Phần ăn bao gồm:\n" + product.getDetail().replace("\\n", newline);
        }
        else {
            detail = "Chi tiết: " + product.getDetail().replace("\\n", newline);
        }
        txtDetail.setText(detail);

        final AlertDialog alertDialog = builder.create();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), context.getResources().getString(R.string.frontcartmessage) + " " + product.getProductName() + " " + context.getResources().getString(R.string.backcartmessage), Toast.LENGTH_SHORT).show();
                addDishToCart();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void addDishToCart() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.addNewCartItem(product);
    }
}