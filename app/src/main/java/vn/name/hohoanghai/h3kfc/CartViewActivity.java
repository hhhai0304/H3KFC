package vn.name.hohoanghai.h3kfc;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import vn.name.hohoanghai.adapter.CartDishAdapter;
import vn.name.hohoanghai.model.CartItem;
import vn.name.hohoanghai.util.DatabaseHelper;
import vn.name.hohoanghai.util.Utility;

public class CartViewActivity extends AppCompatActivity {

    RecyclerView rvCartView;
    TextView txtTotalPrice;
    Button btnPay;

    public CartDishAdapter adapter;
    ArrayList<CartItem> cartItems;
    DatabaseHelper databaseHelper;
    Utility utility;

    long totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartViewActivity.this, CheckoutInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        rvCartView = (RecyclerView) findViewById(R.id.rvCartView);
        rvCartView.addItemDecoration(new VerticalSpaceItemDecoration(20));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCartView.setLayoutManager(mLayoutManager);
        rvCartView.setItemAnimator(new DefaultItemAnimator());

        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        btnPay = (Button) findViewById(R.id.btnPay);

        databaseHelper = new DatabaseHelper(this);
        utility = new Utility();
        cartItems = databaseHelper.getCartList();
        adapter = new CartDishAdapter(this, cartItems);

        rvCartView.setAdapter(adapter);

        totalPrice = 0;
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        totalPrice = 0;
        cartItems = databaseHelper.getCartList();
        for (int i = 0; i < cartItems.size(); i++) {
            long thisPrice = cartItems.get(i).getPrice() * cartItems.get(i).getQuantity();
            totalPrice += thisPrice;
        }
        txtTotalPrice.setText(utility.currencyFormat(totalPrice));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}