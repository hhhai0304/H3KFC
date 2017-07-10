package vn.name.hohoanghai.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import vn.name.hohoanghai.h3kfc.MainActivity;
import vn.name.hohoanghai.h3kfc.R;
import vn.name.hohoanghai.h3kfc.SplashScreenActivity;

/**
 * Created by hhhai0304 on 25/05/2016.
 */
public class ConnectionChecker {

    Activity context;

    public ConnectionChecker(Activity context) {
        this.context = context;
    }

    public void createNetworkStateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.no_internet_title));
        builder.setMessage(context.getResources().getString(R.string.no_internet_message));

        builder.setPositiveButton(context.getResources().getString(R.string.call_hotline), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callHotline();
            }
        });
        builder.setNeutralButton(context.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, SplashScreenActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public void callHotline() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + context.getResources().getString(R.string.hotline)));
        context.startActivity(intent);
        context.finish();
    }
}