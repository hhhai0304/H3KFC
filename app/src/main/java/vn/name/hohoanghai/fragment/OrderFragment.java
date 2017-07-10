package vn.name.hohoanghai.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import vn.name.hohoanghai.h3kfc.R;
import vn.name.hohoanghai.model.MainGSON;
import vn.name.hohoanghai.model.Order;
import vn.name.hohoanghai.model.OrderDetail;
import vn.name.hohoanghai.util.Utility;

/**
 * Created by hhhai0304 on 24-06-2016.
 */
public class OrderFragment extends Fragment {

    TextView txtPending, txtShipping, txtCompleted;
    LinearLayout llPending, llShipping, llCompleted;
    ArrayList<Order> orders;
    int pendingCount, shippingCount, completedCount;
    String email, detail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        txtPending = (TextView) view.findViewById(R.id.txtPending);
        txtShipping = (TextView) view.findViewById(R.id.txtShipping);
        txtCompleted = (TextView) view.findViewById(R.id.txtCompleted);
        llPending = (LinearLayout) view.findViewById(R.id.llPending);
        llShipping = (LinearLayout) view.findViewById(R.id.llShipping);
        llCompleted = (LinearLayout) view.findViewById(R.id.llCompleted);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
    }

    @Override
    public void onResume() {
        super.onResume();
        final Utility utility = new Utility();
        email = utility.getEmailasGuest(getActivity());
        if(email == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = View.inflate(getActivity(), R.layout.dialog_input_email, null);
            final EditText txtOrderViewEmail = (EditText) view.findViewById(R.id.txtOrderViewEmail);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    email = txtOrderViewEmail.getText().toString();
                    getOrders();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.btn_cancel), null);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        } else {
            getOrders();
        }
    }

    private void getOrders() {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage(getResources().getString(R.string.wait));
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.get_orders), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.startsWith("\uFEFF")) {
                    response = response.substring(1);
                }
                progress.dismiss();
                MainGSON gson = new Gson().fromJson(response, MainGSON.class);
                orders = gson.getOrders();
                createListView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), "Error, please try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String>  params = new HashMap<>();
                params.put("Email", email);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void createListView() {
        if(orders == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.noorders), Toast.LENGTH_SHORT).show();
        } else {
            pendingCount = 0;
            shippingCount = 0;
            completedCount = 0;

            for(int i = 0; i < orders.size(); i++) {
                if(orders.get(i).getOrderStatus().equals("0")) {
                    createDetailView(llPending, orders.get(i));
                    pendingCount++;
                } else if(orders.get(i).getOrderStatus().equals("1")) {
                    createDetailView(llShipping, orders.get(i));
                    shippingCount++;
                } else if(orders.get(i).getOrderStatus().equals("2")) {
                    createDetailView(llCompleted, orders.get(i));
                    completedCount++;
                }
            }

            txtPending.setText(getResources().getString(R.string.pending) + " (" + pendingCount + ")");
            txtShipping.setText(getResources().getString(R.string.shipping) + " (" + shippingCount + ")");
            txtCompleted.setText(getResources().getString(R.string.completed) + " (" + completedCount + ")");
        }
    }

    private void createDetailView(LinearLayout linearLayout, final Order order) {
        Utility utility = new Utility();
        View view = View.inflate(getActivity(), R.layout.item_order, null);
        TextView txtOrderID = (TextView) view.findViewById(R.id.txtOrderID);
        TextView txtOrderPrice = (TextView) view.findViewById(R.id.txtOrderPrice);
        TextView txtOrderDate = (TextView) view.findViewById(R.id.txtOrderDate);

        txtOrderID.setText(getResources().getString(R.string.order_order) + " " + order.getOrderID());
        txtOrderDate.setText(getResources().getString(R.string.orderdate) + " " + order.getOrderDate());
        txtOrderPrice.setText(utility.currencyFormat(order.getPrice()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDetailDialog(order.getOrderID());
            }
        });

        linearLayout.addView(view);
    }

    private void createDetailDialog(final String orderID) {
        detail = "";

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage(getResources().getString(R.string.wait));
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.get_orders_detail), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.startsWith("\uFEFF")) {
                    response = response.substring(1);
                }
                progress.dismiss();
                MainGSON gson = new Gson().fromJson(response, MainGSON.class);
                ArrayList<OrderDetail> orderDetails = gson.getOrderDetails();
                for(int i = 0; i < orderDetails.size(); i++) {
                    detail += orderDetails.get(i).getProductName() + "  x" + orderDetails.get(i).getQuantity() + "\n";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.order_order) + " " + orderID);
                builder.setMessage(detail);
                builder.setPositiveButton("OK", null);

                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), "Error, please try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String>  params = new HashMap<>();
                params.put("OrderID", orderID);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}