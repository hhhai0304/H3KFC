package vn.name.hohoanghai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import vn.name.hohoanghai.h3kfc.CartViewActivity;
import vn.name.hohoanghai.h3kfc.R;
import vn.name.hohoanghai.model.CartItem;
import vn.name.hohoanghai.util.DatabaseHelper;
import vn.name.hohoanghai.util.Utility;

/**
 * Created by hhhai0304 on 15-06-2016.
 */
public class CartDishAdapter extends RecyclerView.Adapter<CartDishAdapter.ViewHolder>  {

    CartViewActivity context;
    ArrayList<CartItem> objects;
    DatabaseHelper databaseHelper;

    public CartDishAdapter(CartViewActivity context, ArrayList<CartItem> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Utility utility = new Utility();
        databaseHelper = new DatabaseHelper(context);

        final TextView txtQuantity = holder.txtQuantity;
        TextView txtCartDishName = holder.txtCartDishName;
        TextView txtCartPrice = holder.txtCartPrice;
        ImageView imgCartDish = holder.imgCartDish;
        ImageButton btnPlus = holder.btnPlus;
        ImageButton btnMinus = holder.btnMinus;
        ImageButton btnDelete = holder.btnDelete;

        txtCartDishName.setText(objects.get(position).getProductName());
        txtCartPrice.setText(utility.currencyFormat(objects.get(position).getPrice()));

        setDishImage(imgCartDish, objects.get(position).getAvatar());
        txtQuantity.setText(String.valueOf(objects.get(position).getQuantity()));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = objects.get(holder.getAdapterPosition()).getQuantity();
                if(currentQuantity > 1) {
                    currentQuantity--;
                    objects.get(holder.getAdapterPosition()).setQuantity(currentQuantity);
                    txtQuantity.setText(String.valueOf(currentQuantity + ""));
                    databaseHelper.updateCartQuantity(objects.get(holder.getAdapterPosition()).getProductID(), currentQuantity);
                    context.updateTotalPrice();
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = objects.get(holder.getAdapterPosition()).getQuantity();
                if(currentQuantity < 99) {
                    currentQuantity++;
                    objects.get(holder.getAdapterPosition()).setQuantity(currentQuantity);
                    txtQuantity.setText(String.valueOf(currentQuantity + ""));
                    databaseHelper.updateCartQuantity(objects.get(holder.getAdapterPosition()).getProductID(), currentQuantity);
                    context.updateTotalPrice();
                }
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
                .showImageOnFail(R.drawable.noimage).build();
        imageLoader.displayImage(avatarUrl, imgProduct, options);
    }

    public void remove(int position) {
        databaseHelper.clearCartItem(objects.get(position).getProductID());
        objects.remove(position);
        notifyItemRemoved(position);
        context.adapter.notifyDataSetChanged();
        context.updateTotalPrice();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCartDishName, txtCartPrice, txtQuantity;
        public ImageView imgCartDish;
        public ImageButton btnDelete, btnPlus, btnMinus;
        public LinearLayout llCartView;

        public ViewHolder(View view) {
            super(view);
            llCartView = (LinearLayout) view.findViewById(R.id.llCartView);
            txtCartDishName = (TextView) view.findViewById(R.id.txtCartDishName);
            txtCartPrice = (TextView) view.findViewById(R.id.txtCartPrice);
            imgCartDish = (ImageView) view.findViewById(R.id.imgCartDish);
            txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
            btnPlus = (ImageButton) view.findViewById(R.id.btnPlus);
            btnMinus = (ImageButton) view.findViewById(R.id.btnMinus);
            btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);

        }
    }
}