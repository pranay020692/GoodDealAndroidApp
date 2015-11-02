package com.example.tonyhuang.gooddealapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by puneet on 11/2/15.
 */
public class ProductsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PRODUCT_ID, MySQLiteHelper.COLUMN_PRODUCT_NAME, MySQLiteHelper.COLUMN_PRODUCT_RATING, MySQLiteHelper.COLUMN_PRODUCT_PRICE };

    public ProductsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Product createProduct(String prodId, String prodName, String prodRating, String prodPrice) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PRODUCT_ID, prodId);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_NAME, prodName);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_RATING, prodRating);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_PRICE, prodPrice);

        long insertId = database.insert(MySQLiteHelper.TABLE_PRODUCTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Product newProduct = cursorToProduct(cursor);
        cursor.close();
        return newProduct;
    }

    public void deleteProduct(Product product) {
        long id = product.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PRODUCTS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Product> getAllComments() {
        List<Product> products = new ArrayList<Product>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCTS,
                allColumns, null, null, null, null, null);

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

    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.setId(cursor.getInt(0));

        product.setProductId(cursor.getString(1));
        product.setProductName(cursor.getString(2));
        product.setProductAvgRating(cursor.getString(3));
        product.setProductPrice(cursor.getString(4));
        return product;
    }

}
