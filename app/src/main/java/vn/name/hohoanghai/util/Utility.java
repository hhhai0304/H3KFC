package vn.name.hohoanghai.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import vn.name.hohoanghai.h3kfc.R;
import vn.name.hohoanghai.model.Account;

/**
 * Created by hhhai0304 on 27/05/2016.
 */
public class Utility {
    static String fullchar = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
    static char[] SOURCE_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự', };

    static char[] DESTINATION_CHARACTERS = { 'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u', };

    //Bỏ dấu 1 chuỗi
    public String englishString(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, englishChar(sb.charAt(i)));
        }
        return sb.toString();
    }

    //Bỏ dấu 1 ký tự
    public char englishChar(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    //Thêm dấu phẩy và đơn vị cho giá tiền
    public String currencyFormat(long s) {
        return ("đ " + NumberFormat.getNumberInstance(Locale.US).format(s));
    }

    public String randomID(int size) {
        String s = "";
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            s = s + fullchar.charAt(r.nextInt(fullchar.length()));
        }
        return s;
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public String getSHA256(String s)  {
        String returnString = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());
            byte[] newS = digest.digest();
            returnString = Base64.encodeToString(newS, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public Account getSession(Activity context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.user_file), Context.MODE_PRIVATE);
        String userID = preferences.getString("userID", "");
        if(userID.isEmpty()) {
            return null;
        } else {
            Account account = new Account();
            account.setUserID(userID);
            account.setName(preferences.getString("name", ""));
            account.setAddress(preferences.getString("address", ""));
            account.setDistrict(preferences.getString("district", ""));
            account.setCity(preferences.getString("city", ""));
            account.setPhone(preferences.getString("phone", ""));
            account.setAvatar(preferences.getString("avatar", ""));
            account.setEmail(preferences.getString("email", ""));
            return account;
        }
    }

    public String getEmailasGuest(Activity context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.user_file), Context.MODE_PRIVATE);
        String email = preferences.getString("email", "");
        if(email.isEmpty()) {
            return null;
        } else {
            return email;
        }
    }

    public void setLanguage(Activity context) {
        Locale locale = new Locale("vi", "VN");
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}