package vn.name.hohoanghai.h3kfc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import vn.name.hohoanghai.util.Utility;

public class SessionActivity extends AppCompatActivity {

    TextView txtCheckoutEmail;
    RadioGroup rbgChoose;
    RadioButton rbGuest, rbSignin;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        addControls();
        addEvents();
    }

    private void addEvents() {
        rbgChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rbGuest.isChecked()) {
                    btnContinue.setText(getResources().getString(R.string.cartcontinue));
                } else if(rbSignin.isChecked()) {
                    btnContinue.setText(getResources().getString(R.string.action_sign_in).toUpperCase());
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility utility = new Utility();
                if(utility.isValidEmail(txtCheckoutEmail.getText())) {
                    if(rbGuest.isChecked()) {
                        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.user_file), MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email", txtCheckoutEmail.getText().toString());
                        editor.apply();
                        Intent intent = new Intent(SessionActivity.this, CheckoutInfoActivity.class);
                        startActivity(intent);
                    } else if(rbSignin.isChecked()) {
                        Intent intent = new Intent(SessionActivity.this, LoginActivity.class);
                        intent.putExtra("email", txtCheckoutEmail.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(SessionActivity.this, getResources().getString(R.string.validemail), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addControls() {
        txtCheckoutEmail = (TextView) findViewById(R.id.txtCheckoutEmail);
        rbgChoose = (RadioGroup) findViewById(R.id.rbgChoose);
        rbGuest = (RadioButton) findViewById(R.id.rbGuest);
        rbSignin = (RadioButton) findViewById(R.id.rbSignin);
        btnContinue = (Button) findViewById(R.id.btnContinue);
    }
}