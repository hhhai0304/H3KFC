package vn.name.hohoanghai.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import vn.name.hohoanghai.h3kfc.MainActivity;
import vn.name.hohoanghai.h3kfc.R;
import vn.name.hohoanghai.h3kfc.SplashScreenActivity;

public class SettingFragment extends Fragment {

    RadioGroup rbgLanguage;
    RadioButton rbVietnamese, rbEnglish;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        rbgLanguage = (RadioGroup) view.findViewById(R.id.rbgLanguage);
        rbVietnamese = (RadioButton) view.findViewById(R.id.rbVietnamese);
        rbEnglish = (RadioButton) view.findViewById(R.id.rbEnglish);

        rbgLanguage.setOnCheckedChangeListener(null);

        if(getChecked().equals("vi")) {
            rbVietnamese.setChecked(true);
        } else {
            rbEnglish.setChecked(true);
        }

        rbgLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rbVietnamese.isChecked()) {
                    changeLanguage("vi", "VN");
                } else if (rbEnglish.isChecked()) {
                    changeLanguage("en", "US");
                }
            }
        });

        return view;
    }

    private String getChecked() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.language_file), Context.MODE_PRIVATE);
        String language = preferences.getString("language", "vi");
        return language;
    }

    private void changeLanguage(String language, String country) {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.language_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", language);
        editor.putString("country", country);
        editor.apply();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.restart));
        builder.setPositiveButton(getResources().getString(R.string.btn_restart), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                startActivity(new Intent(getActivity(), SplashScreenActivity.class));
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.btn_cancel), null);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();


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
}