package vn.name.hohoanghai.h3kfc;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import vn.name.hohoanghai.model.Account;
import vn.name.hohoanghai.util.Utility;

public class CheckoutInfoActivity extends AppCompatActivity {

    Utility utility;
    Account account;
    boolean isCheckLogin;

    Spinner spnAddressCity, spnAddressDistrict;
    Button btnAddressContinue;
    EditText txtAddressFullname, txtAddressPhone, txtAddress;
    LinearLayout llDoneTab;
    TextView txtAddressEmail;

    ArrayAdapter<CharSequence> districtAdapter;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_info);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        addControls();
        if(!isCheckLogin) {
            checkLogin();
        }
        addEvents();
    }

    private void addEvents() {
        spnAddressCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.parseColor("#890000"));
                setSpnDistrict(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddressContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinue();
            }
        });

        llDoneTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinue();
            }
        });
    }

    private void onContinue() {
        if(txtAddressPhone.length() > 0 && txtAddress.length() > 0 && txtAddressFullname.length() > 0) {
            Intent intent = new Intent(this, DoneActivity.class);
            intent.putExtra("email", txtAddressEmail.getText().toString());
            intent.putExtra("name", txtAddressFullname.getText().toString());
            intent.putExtra("address", txtAddress.getText().toString() + ", " + spnAddressDistrict.getSelectedItem().toString() + ", " + spnAddressCity.getSelectedItem().toString());
            intent.putExtra("phone", txtAddressPhone.getText().toString());
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        } else {
            Toast.makeText(CheckoutInfoActivity.this, getResources().getString(R.string.nullfield), Toast.LENGTH_SHORT).show();
        }
    }

    private void setTextForFullSignin() {
        txtAddress.setText(account.getAddress());
        txtAddressFullname.setText(account.getName());
        txtAddressPhone.setText(account.getPhone());
        txtAddressEmail.setText(account.getEmail());

        if(account.getCity().equals("Hồ Chí Minh")) {
            spnAddressCity.setSelection(0);
            setSpnDistrict(0);
        } else {
            spnAddressCity.setSelection(1);
            setSpnDistrict(1);
        }

        spnAddressDistrict.setSelection(districtAdapter.getPosition(account.getDistrict()));
    }

    private void addControls() {
        spnAddressCity = (Spinner) findViewById(R.id.spnAddressCity);
        spnAddressDistrict = (Spinner) findViewById(R.id.spnAddressDistrict);
        btnAddressContinue = (Button) findViewById(R.id.btnAddressContinue);
        txtAddressFullname = (EditText) findViewById(R.id.txtAddressFullname);
        txtAddressPhone = (EditText) findViewById(R.id.txtAddressPhone);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        llDoneTab = (LinearLayout) findViewById(R.id.llDoneTab);
        txtAddressEmail = (TextView) findViewById(R.id.txtAddressEmail);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAddressCity.setAdapter(cityAdapter);
        spnAddressCity.setSelection(0);

        isCheckLogin = false;
    }

    private void checkLogin() {
        utility = new Utility();
        account = utility.getSession(this);
        if(account == null) {
            email = utility.getEmailasGuest(this);
            if(email == null) {
                Intent intent = new Intent(this, SessionActivity.class);
                startActivity(intent);
                finish();
            } else {
                txtAddressEmail.setText(email);
            }
        } else {
            setTextForFullSignin();
        }
        isCheckLogin = true;
    }

    private void setSpnDistrict(int position) {
        if(position == 0) {
            districtAdapter = ArrayAdapter.createFromResource(CheckoutInfoActivity.this, R.array.district_hcm, android.R.layout.simple_spinner_item);
            districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnAddressDistrict.setAdapter(districtAdapter);
            if(account != null) {
                spnAddressDistrict.setSelection(districtAdapter.getPosition(account.getDistrict()));
            }
        } else {
            districtAdapter = ArrayAdapter.createFromResource(CheckoutInfoActivity.this, R.array.district_hn, android.R.layout.simple_spinner_item);
            districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnAddressDistrict.setAdapter(districtAdapter);
            if(account != null) {
                spnAddressDistrict.setSelection(districtAdapter.getPosition(account.getDistrict()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}