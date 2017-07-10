package vn.name.hohoanghai.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import vn.name.hohoanghai.h3kfc.R;
import vn.name.hohoanghai.model.Product;
import vn.name.hohoanghai.util.DishDetailDialog;
import vn.name.hohoanghai.util.Utility;

/**
 * Created by hhhai0304 on 02/06/2016.
 */
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {
    Activity context;
    ArrayList<Product> objects;

    public DishAdapter(Activity context, ArrayList<Product> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.context.getLayoutInflater().inflate(R.layout.item_dish, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Utility utility = new Utility();

        TextView txtDishName = holder.txtDishName;
        TextView txtPrice = holder.txtPrice;
        ImageView imgDish = holder.imgDish;

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(16);
        txtDishName.setFilters(filterArray);

        txtDishName.setText(objects.get(position).getProductName());
        txtPrice.setText(utility.currencyFormat(objects.get(position).getPrice()));

        setDishImage(imgDish, objects.get(position).getAvatar());
        imgDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DishDetailDialog dishDetailDialog = new DishDetailDialog(context, objects.get(position));
                dishDetailDialog.createDishDetailDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private void setDishImage(ImageView imgProduct, String avatarUrl) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .displayer(new RoundedBitmapDisplayer(20)).build();
        imageLoader.displayImage(avatarUrl, imgProduct, options);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtPrice, txtDishName;
        public ImageView imgDish;

        public ViewHolder(View view) {
            super(view);
            txtDishName = (TextView) view.findViewById(R.id.txtDishName);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);
            imgDish = (ImageView) view.findViewById(R.id.imgDish);
        }
    }
}