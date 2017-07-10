package vn.name.hohoanghai.h3kfc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import vn.name.hohoanghai.model.CartItem;
import vn.name.hohoanghai.util.DatabaseHelper;
import vn.name.hohoanghai.util.Utility;

public class DoneActivity extends AppCompatActivity {

    TextView txtOrderFullname, txtOrderAddress, txtOrderPhone, txtTotalPrice;
    Button btnPlaceOrder;
    LinearLayout llCheckoutTab;

    String email, name, address, phone;
    ArrayList<CartItem> cartItems;
    DatabaseHelper databaseHelper;
    long totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });

        llCheckoutTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendOrder() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.wait));
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.orders), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.startsWith("\uFEFF")) {
                    response = response.substring(1);
                }
                progress.dismiss();
                if(response.equals("true")) {
                    Toast.makeText(DoneActivity.this, getResources().getString(R.string.success_place_order), Toast.LENGTH_SHORT).show();
                    databaseHelper.clearCart();
                    startActivity(new Intent(DoneActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(DoneActivity.this, "Error, please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(DoneActivity.this, "Error, please try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected HashMap<String, String> getParams() {
                Utility utility = new Utility();
                String orderID = utility.randomID(10);
                HashMap<String, String>  params = new HashMap<>();
                params.put("OrderID", orderID);
                params.put("Email", email);
                params.put("Name", name);
                params.put("Address", address);
                params.put("Phone", phone);
                params.put("Price", String.valueOf(totalPrice));

                JSONArray jsonArray = new JSONArray();
                for(int i = 0; i < cartItems.size(); i++) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("ProductID", cartItems.get(i).getProductID());
                        object.put("Quantity", cartItems.get(i).getQuantity());

                        jsonArray.put(object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                params.put("Detail", jsonArray.toString());
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers == null) {
                    // cant just set a new empty map because the member is final.
                    response = new NetworkResponse(
                            response.statusCode,
                            response.data,
                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                            response.notModified,
                            response.networkTimeMs);
                }

                return super.parseNetworkResponse(response);
            }
        };
        queue.add(stringRequest);
    }

    private void addControls() {
        txtOrderFullname = (TextView) findViewById(R.id.txtOrderFullname);
        txtOrderAddress = (TextView) findViewById(R.id.txtOrderAddress);
        txtOrderPhone = (TextView) findViewById(R.id.txtOrderPhone);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        llCheckoutTab = (LinearLayout) findViewById(R.id.llCheckoutTab);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");

        databaseHelper = new DatabaseHelper(this);
        cartItems = databaseHelper.getCartList();

        totalPrice = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            long thisPrice = cartItems.get(i).getPrice() * cartItems.get(i).getQuantity();
            totalPrice += thisPrice;
        }

        txtOrderFullname.setText(name);
        txtOrderAddress.setText(address);
        txtOrderPhone.setText(phone);
        Utility utility = new Utility();
        txtTotalPrice.setText(utility.currencyFormat(totalPrice));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoneActivity.this, CheckoutInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}