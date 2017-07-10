package vn.name.hohoanghai.util;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import vn.name.hohoanghai.model.CartItem;
import vn.name.hohoanghai.model.Product;

/**
 * Created by hhhai0304 on 27/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "hohoangh_shopapp";
    private static final int DATABASE_VERSION = 1;

    private static final String productTable = "Product";
    private static final String productID = "ProductID";
    private static final String productName = "ProductName";
    private static final String detail = "Detail";
    private static final String avatar = "Avatar";
    private static final String price = "Price";
    private static final String categoryID = "CategoryID";
    private static final String productEnglishName = "KhongDau";

    private static final String cartTable = "Cart";
    private static final String cartProductID = "ProductID";
    private static final String cartProductName = "ProductName";
    private static final String quantity = "Quantity";
    private static final String cartProductPrice = "Price";
    private static final String cartAvatar = "Avatar";

    Utility utility = new Utility();

    public DatabaseHelper(Activity context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Khởi tạo các bảng trong Database
        String create_products_table = "create table " + productTable + "(" + productID
                + " text primary key, " + productName + " text, " + detail + " text, "
                + avatar + " text, " + price + " int, " + productEnglishName + " text, "
                + categoryID + " text)";
        String create_orders_table = "create table " + cartTable + "(" + cartProductID
                + " text primary key, " + cartProductName + " text, " + quantity + " int, "
                + cartProductPrice + " int, " + cartAvatar + " text)";

        db.execSQL(create_products_table);
        db.execSQL(create_orders_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + productTable);
        db.execSQL("drop table if exists " + cartTable);
        onCreate(db);
    }

    //Thêm product mới vào database
    public void addNewProduct(Product pd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(productID, pd.getProductID());
        values.put(productName, pd.getProductName());
        values.put(detail, pd.getDetail());
        values.put(avatar, pd.getAvatar());
        values.put(price, pd.getPrice());
        values.put(categoryID, pd.getCategoryID());
        values.put(productEnglishName, utility.englishString(pd.getProductName()));

        db.insert(productTable, null, values);
        db.close();
    }

    //Thêm Món ăn vào Giỏ hàng
    public void addNewCartItem(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(cartProductID, product.getProductID());
        values.put(cartProductName, product.getProductName());
        values.put(quantity, 1);
        values.put(cartProductPrice, product.getPrice());
        values.put(cartAvatar, product.getAvatar());

        try {
            db.insertOrThrow(cartTable, null, values);
        } catch (SQLiteConstraintException e) {
            int thisQuantity = 0;
            String selectQuery = "SELECT " + quantity + " FROM " + cartTable + " WHERE " + cartProductID + " = '" + product.getProductID() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);

            cursor.moveToFirst();
            thisQuantity = cursor.getInt(0);
            thisQuantity++;

            updateCartQuantity(product.getProductID(), thisQuantity);
        }
        db.close();
    }

    //Lấy Món ăn lên Giỏ hàng
    public ArrayList<CartItem> getCartList() {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String[] colunm = {cartProductID, cartProductName, quantity, cartProductPrice, cartAvatar};
        Cursor cursor = db.query(true, cartTable, colunm, null, null, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            do {

                CartItem cart = new CartItem();
                cart.setProductID(cursor.getString(0));
                cart.setProductName(cursor.getString(1));
                cart.setQuantity(cursor.getInt(2));
                cart.setPrice(cursor.getLong(3));
                cart.setAvatar(cursor.getString(4));
                cartItems.add(cart);
            }	while (cursor.moveToNext());
        }
        db.close();
        return cartItems;
    }

    //Xóa tất cả Products
    public void clearAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(productTable, null, null);
        db.close();
    }

    //Xóa tất cả Products
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(cartTable, null, null);
        db.close();
    }

    //Lấy danh sách các Món ăn
    public ArrayList<Product> getProductList(int sortMode, String category, String search) {
        String column, sort;

        if(sortMode == 0 || sortMode == 1)
            column = "ProductName";
        else
            column = "Price";

        if (sortMode == 0 || sortMode == 2)
            sort = "ASC";
        else
            sort = "DESC";

        ArrayList<Product> pd = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "", and = " WHERE (";

        if (!category.equals("")) {
            where = " WHERE " + categoryID + " = '" + category + "'";
        }

        if (!search.equals("")) {
            if(!where.equals("")) {
                and = " AND (";
            }
            where = where + and + productEnglishName + " like '%" + search + "%' OR " + productName + " like '%" + search + "%')";
        }
        String sortQuery = "SELECT * FROM " + productTable + where + " ORDER BY " + column + " " + sort;
        Cursor cursor = db.rawQuery(sortQuery, null);

        if(cursor != null && cursor.moveToFirst()) {
            pd = new ArrayList<>();
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String detail = cursor.getString(2);
                String avatar = cursor.getString(3);
                long price = cursor.getInt(4);
                String caId = cursor.getString(6);
                Product product = new Product(id, name, detail, avatar, price, caId);
                pd.add(product);
            }	while (cursor.moveToNext());
        }
        db.close();
        return pd;
    }

    public void clearCartItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(cartTable, cartProductID + " = ?", new String[] { id });
        db.close();
    }

    public void updateCartQuantity(String id, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.quantity, quantity);
        db.update(cartTable, cv, cartProductID + " = '" + id + "'", null);
        db.close();
    }
}