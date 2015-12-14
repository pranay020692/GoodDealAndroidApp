package com.example.tonyhuang.gooddealapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tonyhuang.gooddealapplication.models.History;
import com.example.tonyhuang.gooddealapplication.models.Product;
import com.example.tonyhuang.gooddealapplication.models.WishList;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by puneet on 11/2/15.
 */
public class ProductsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumnsProducts = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PRODUCT_ID, MySQLiteHelper.COLUMN_PRODUCT_NAME, MySQLiteHelper.COLUMN_PRODUCT_RATING, MySQLiteHelper.COLUMN_PRODUCT_PRICE, MySQLiteHelper.COLUMN_PRODUCT_IMAGE_URL};

    private String[] allColumnsHistory = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PRODUCT_NAME, MySQLiteHelper.COLUMN_PRODUCT_PRICE};

    private String[] allColumnsWishlist = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PRODUCT_NAME, MySQLiteHelper.COLUMN_PRODUCT_IMAGE_URL, MySQLiteHelper.COLUMN_PRODUCT_PRICE};

    // private String[] allColumnsEbay = {MySQLiteHelper.COLUMN_ID ,MySQLiteHelper.COLUMN_PRODUCT};
    public ProductsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Product createProduct(String prodId, String prodName, String prodRating, String prodPrice, String prodImageURL) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PRODUCT_ID, prodId);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_NAME, prodName);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_RATING, prodRating);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_PRICE, prodPrice);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_IMAGE_URL, prodImageURL);

        long insertId = database.insert(MySQLiteHelper.TABLE_PRODUCTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                allColumnsProducts, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Product newProduct = cursorToProduct(cursor);
        cursor.close();
        return newProduct;
    }

    public History createHistory(String prodName, String prodPrice) {

        ContentValues values = new ContentValues();

        Cursor checkingCursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_HISTORY + " WHERE " + MySQLiteHelper.COLUMN_PRODUCT_NAME + "= " + "'" + prodName + "';", null);
        if (checkingCursor.moveToFirst()) {
            History alreadyInHistory = cursorToHistory(checkingCursor);
            checkingCursor.close();
            return alreadyInHistory;
        }
        values.put(MySQLiteHelper.COLUMN_PRODUCT_NAME, prodName);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_PRICE, prodPrice);
        long insertId = database.insert(MySQLiteHelper.TABLE_HISTORY, null,
                values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_HISTORY,
                allColumnsHistory, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        History newHistory = cursorToHistory(cursor);
        cursor.close();

        return newHistory;
    }

    public WishList createWishList(String prodName, String imageUrl, String price) {

        ContentValues values = new ContentValues();
/*        Cursor checkingCursor = database.rawQuery("SELECT * FROM "+ MySQLiteHelper.TABLE_WISHLIST+" WHERE " +MySQLiteHelper.COLUMN_PRODUCT_NAME+"= "+"'"+prodName+"';", null);
        if(checkingCursor.moveToFirst()){
            WishList alreadyInHistory = cursorToWishList(checkingCursor);
            checkingCursor.close();
            return alreadyInHistory;
        }*/

        values.put(MySQLiteHelper.COLUMN_PRODUCT_NAME, prodName);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_IMAGE_URL, imageUrl);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_PRICE, price);

        long insertId = database.insert(MySQLiteHelper.TABLE_WISHLIST, null,
                values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_WISHLIST,
                allColumnsWishlist, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        WishList newWishList = cursorToWishList(cursor);
        cursor.close();
        return newWishList;
    }

    public void deleteProduct(Product product) {
        long id = product.getId();
        System.out.println("Product deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PRODUCTS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public History getHistory(long id) {

        Cursor cursor = database.query(MySQLiteHelper.TABLE_HISTORY,
                allColumnsHistory, null, null, null, null, null);
        long counter = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (counter == id) {

                //  String productNameForDeletion = cursor.getString(1);

                break;

            }
            counter++;
            cursor.moveToNext();

        }
        History history = cursorToHistory(cursor);
        cursor.close();
        return history;
    }

    public void deleteHistory(long id) {
        //long id = history.getId();
        //System.out.println("Product "+history.getName()+"from the history");

        Cursor cursor = database.query(MySQLiteHelper.TABLE_HISTORY,
                allColumnsHistory, null, null, null, null, null);
        long counter = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (counter == id) {

                String productNameForDeletion = cursor.getString(1);
                database.delete(MySQLiteHelper.TABLE_HISTORY, MySQLiteHelper.COLUMN_PRODUCT_NAME
                        + " = '" + productNameForDeletion + "';", null);
                //cursor.close();

                break;

            }
            counter++;
            cursor.moveToNext();

        }
        cursor.close();
        //database.delete(MySQLiteHelper.TABLE_HISTORY, MySQLiteHelper.COLUMN_ID
        //        + " = " + id, null);
    }

    public void deleteWishList(long id) {


        Cursor cursor = database.query(MySQLiteHelper.TABLE_WISHLIST,
                allColumnsWishlist, null, null, null, null, null);
        long counter = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (counter == id) {

                String productNameForDeletion = cursor.getString(1);
                database.delete(MySQLiteHelper.TABLE_WISHLIST, MySQLiteHelper.COLUMN_PRODUCT_NAME
                        + " = '" + productNameForDeletion + "';", null);
                //cursor.close();

                break;

            }
            counter++;
            cursor.moveToNext();

        }
        cursor.close();
        //database.delete(MySQLiteHelper.TABLE_HISTORY, MySQLiteHelper.COLUMN_ID
        //        + " = " + id, null);
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<Product>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                allColumnsProducts, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return products;
    }

    public ArrayList<History> getAllHistory() {
        ArrayList<History> histories = new ArrayList<History>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_HISTORY, allColumnsHistory, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            History history = cursorToHistory(cursor);
            histories.add(history);
            cursor.moveToNext();
        }

        cursor.close();
        return histories;

    }

    public ArrayList<WishList> getAllWishList() {
        ArrayList<WishList> wishlists = new ArrayList<WishList>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_WISHLIST, allColumnsWishlist, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            WishList wishlist = cursorToWishList(cursor);
            wishlists.add(wishlist);
            cursor.moveToNext();
        }

        cursor.close();
        return wishlists;

    }


    public void deleteAllProducts() {

        ArrayList<Product> products = getAllProducts();

        if (products.size() != 0) {
            database.delete(MySQLiteHelper.TABLE_PRODUCTS, null, null);
        }
    }

    public void deleteAllHistory() {

        ArrayList<History> histories = getAllHistory();

        if (histories.size() != 0) {

            database.delete(MySQLiteHelper.TABLE_HISTORY, null, null);
        }
    }

    public void deleteAllWishList() {

        ArrayList<WishList> wishLists = getAllWishList();

        if (wishLists.size() != 0) {

            database.delete(MySQLiteHelper.TABLE_WISHLIST, null, null);
        }
    }

    public Product cursorToProduct(Cursor cursor) {
        Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        /*
        product.setId(cursor.getInt(0));
        product.setProductId(cursor.getString(1));
        product.setProductName(cursor.getString(2));
        product.setProductAvgRating(cursor.getString(3));
        product.setProductPrice(cursor.getString(4));
        */
        return product;

    }

    public History cursorToHistory(Cursor cursor) {
        History history = new History(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        return history;
    }


    public WishList cursorToWishList(Cursor cursor) {
        WishList wishList = new WishList(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return wishList;
    }


    /*
    public void enterEBAY(String productURL){

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PRODUCT, productURL);


        long insertId = database.insert(MySQLiteHelper.TABLE_EBAY, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EBAY,
                allColumnsEbay, MySQLiteHelper.COLUMN_PRODUCT + " = " + insertId, null,
                null, null, null);

       cursor.close();

    }
     */

    /*
    public String[] getALLEbay(){

        String[] eBayStrings = new String[5];

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EBAY, allColumnsEbay, null, null, null, null, null);
        cursor.moveToFirst();

        int counter = 0;
        while (!cursor.isAfterLast()) {
            String oneURL = cursor.getString(1);
            eBayStrings[counter] = oneURL;
            counter++;
            cursor.moveToNext();
        }

        cursor.close();
        return eBayStrings;


    }
    */
}
