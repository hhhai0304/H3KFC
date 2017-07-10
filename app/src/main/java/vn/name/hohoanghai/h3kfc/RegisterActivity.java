package vn.name.hohoanghai.h3kfc;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import vn.name.hohoanghai.util.Utility;

public class RegisterActivity extends AppCompatActivity {

    TextView txtUpSignup;
    EditText txtSignupEmail, txtSignupPassowrd, txtConfirmPassowrd, txtUsername, txtUserAddress, txtPhone, txtAvatar;
    Spinner spnCity, spnDistrict;
    Button btnSignupDone;
    Utility utility;
    String userID, email, password, name, address, district, city, phone, avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        addControls();
        addEvents();
    }

    private void addEvents() {
        txtUpSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignupDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(utility.isValidEmail(txtSignupEmail.getText().toString())) {
                    if(txtSignupPassowrd.getText().toString().equals(txtConfirmPassowrd.getText().toString())) {
                        if(txtUsername.length() > 0 && txtUserAddress.length() > 0 && txtPhone.length() > 0) {
                            doSignup();
                        } else {
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.nullfield), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.confirmerror), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.validemail), Toast.LENGTH_SHORT).show();
                }
            }
        });

        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTypeface(Typeface.SERIF);
                setSpnDistrict(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTypeface(Typeface.SERIF);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doSignup() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.wait));
        progress.setCancelable(false);
        progress.show();

        userID = utility.randomID(8);
        email = txtSignupEmail.getText().toString();
        password = utility.getSHA256(txtSignupPassowrd.getText().toString());
        name = txtUsername.getText().toString();
        address = txtUserAddress.getText().toString();
        district = spnDistrict.getSelectedItem().toString();
        city = spnCity.getSelectedItem().toString();
        phone = txtPhone.getText().toString();
        avatar = txtAvatar.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.register), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.startsWith("\uFEFF")) {
                    response = response.substring(1);
                }
                progress.dismiss();
                if(response.equals("true")) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    createSession();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.alreadyregistered), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(RegisterActivity.this, "Error, please try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String>  params = new HashMap<>();
                params.put("userID", userID);
                params.put("email", email);
                params.put("password", password);
                params.put("name", name);
                params.put("address", address);
                params.put("district", district);
                params.put("city", city);
                params.put("phone", phone);
                params.put("avatar", avatar);
                params.put("RegID", "");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void createSession() {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.user_file), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userID", userID);
        editor.putString("email", email);
        editor.putString("name", name);
        editor.putString("address", address);
        editor.putString("district", district);
        editor.putString("city", city);
        editor.putString("phone", phone);
        editor.putString("avatar", avatar);
        editor.apply();
    }

    private void addControls() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.BLACK);
        }

        txtUpSignup = (TextView) findViewById(R.id.txtUpSignup);
        txtSignupEmail = (EditText) findViewById(R.id.txtSignupEmail);
        txtSignupPassowrd = (EditText) findViewById(R.id.txtSignupPassowrd);
        txtConfirmPassowrd = (EditText) findViewById(R.id.txtConfirmPassowrd);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtUserAddress = (EditText) findViewById(R.id.txtUserAddress);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtAvatar = (EditText) findViewById(R.id.txtAvatar);
        spnCity = (Spinner) findViewById(R.id.spnCity);
        spnDistrict = (Spinner) findViewById(R.id.spnDistrict);
        btnSignupDone = (Button) findViewById(R.id.btnSignupDone);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(cityAdapter);
        spnCity.setSelection(0);

        setHintType(Color.WHITE, Typeface.SERIF);

        utility = new Utility();
    }

    private void setSpnDistrict(int position) {
        if(position == 0) {
            ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(RegisterActivity.this, R.array.district_hcm, android.R.layout.simple_spinner_item);
            districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnDistrict.setAdapter(districtAdapter);
        } else {
            ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(RegisterActivity.this, R.array.district_hn, android.R.layout.simple_spinner_item);
            districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnDistrict.setAdapter(districtAdapter);
        }
    }

    private void setHintType(int color, Typeface typeface) {
        txtSignupEmail.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        txtSignupEmail.setTypeface(typeface);
        txtSignupPassowrd.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        txtSignupPassowrd.setTypeface(typeface);
        txtConfirmPassowrd.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        txtConfirmPassowrd.setTypeface(typeface);
        txtUsername.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        txtUsername.setTypeface(typeface);
        txtUserAddress.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        txtUserAddress.setTypeface(typeface);
        txtPhone.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        txtPhone.setTypeface(typeface);
        txtAvatar.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        txtAvatar.setTypeface(typeface);
    }
}