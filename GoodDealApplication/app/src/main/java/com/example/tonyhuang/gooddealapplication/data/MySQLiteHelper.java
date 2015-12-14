package com.example.tonyhuang.gooddealapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by puneet on 11/2/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{


    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_HISTORY = "history";
    public static final String TABLE_WISHLIST = "wishlist";
    public static final String TABLE_EBAY = "ebay";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PRODUCT_RATING = "product_rating";
    public static final String COLUMN_PRODUCT_PRICE = "product_price";
    public static final String COLUMN_PRODUCT_IMAGE_URL = "product_image";
    public static final String COLUMN_PRODUCT = "product_column";


    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_PRODUCT_CREATE = "create table "
            + TABLE_PRODUCTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_PRODUCT_ID + " text not null, "+COLUMN_PRODUCT_NAME +" text not null, " + COLUMN_PRODUCT_RATING +" text not null, " +COLUMN_PRODUCT_PRICE +"  text not null, " + COLUMN_PRODUCT_IMAGE_URL +" text not null);";

    private static final String DATABASE_HISTORY_CREATE = "create table "
            + TABLE_HISTORY + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PRODUCT_NAME + " text not null, "+ COLUMN_PRODUCT_PRICE+" text not null);";

    private static final String DATABASE_WISHLIST_CREATE = "create table "
            + TABLE_WISHLIST + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PRODUCT_NAME + " text not null, " + COLUMN_PRODUCT_IMAGE_URL+ " text not null, "+ COLUMN_PRODUCT_PRICE + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*

     private static final String DATABASE_EBAY_CREATE = "create table "
            + TABLE_EBAY + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PRODUCT + " text not null );";
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_PRODUCT_CREATE);
        database.execSQL(DATABASE_HISTORY_CREATE);
        database.execSQL(DATABASE_WISHLIST_CREATE);
        //database.execSQL(DATABASE_EBAY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
        onCreate(db);
    }
}
