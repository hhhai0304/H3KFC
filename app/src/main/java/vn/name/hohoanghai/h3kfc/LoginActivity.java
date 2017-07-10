package vn.name.hohoanghai.h3kfc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import vn.name.hohoanghai.util.Utility;

public class LoginActivity extends AppCompatActivity {

    EditText txtSigninEmail, txtSigninPassword;
    TextView txtUp;
    Button btnSignin, btnSignup;
    String emailIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        addControls();
        addEvents();
    }

    private void addEvents() {
        txtUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtSigninEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Utility utility = new Utility();
                    if(!utility.isValidEmail(txtSigninEmail.getText())) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.validemail), Toast.LENGTH_SHORT).show();
                        txtSigninEmail.setText("");
                    }
                }
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtSigninEmail.getText().length() > 5 && txtSigninPassword.getText().length() > 0) {
                    Utility utility = new Utility();
                    if(!utility.isValidEmail(txtSigninEmail.getText())) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.validemail), Toast.LENGTH_SHORT).show();
                        txtSigninEmail.setText("");
                    } else{
                        doSignin(txtSigninEmail.getText().toString(), utility.getSHA256(txtSigninPassword.getText().toString()));
                    }

                } else{
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.emptysignin), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void doSignin(final String email, final String password) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.wait));
        progress.setCancelable(false);
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.login), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                createSession(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, "Error, please try again!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String>  params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("RegID", "");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void createSession(String response) {
        switch (response) {
            case "0":
                Log.e("Sign in error", "Lỗi câu truy vấn");
                break;
            case "1":
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.signin_incorrect), Toast.LENGTH_LONG).show();
                break;
            default:
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);

                    SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.user_file), MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userID", object.getString("userID"));
                    editor.putString("email", object.getString("email"));
                    editor.putString("name", object.getString("name"));
                    editor.putString("address", object.getString("address"));
                    editor.putString("district", object.getString("district"));
                    editor.putString("city", object.getString("city"));
                    editor.putString("phone", object.getString("phone"));
                    editor.putString("avatar", object.getString("avatar"));
                    editor.apply();

                    if(emailIntent != null) {
                        Intent intent = new Intent(LoginActivity.this, CheckoutInfoActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.signin_incorrect), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void addControls() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.BLACK);
        }

        txtSigninEmail = (EditText) findViewById(R.id.txtSigninEmail);
        txtSigninPassword = (EditText) findViewById(R.id.txtSigninPassword);
        txtUp = (TextView) findViewById(R.id.txtUp);
        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        txtSigninEmail.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        txtSigninEmail.setTypeface(Typeface.SERIF);
        txtSigninPassword.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        txtSigninPassword.setTypeface(Typeface.SERIF);

        Intent intent = getIntent();
        emailIntent = intent.getStringExtra("email");
        txtSigninEmail.setText(emailIntent);
    }
}