package com.example.tonyhuang.gooddealapplication.fragments;

/**
 * Created by TonyHuang on 10/29/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.tonyhuang.gooddealapplication.R;
import com.example.tonyhuang.gooddealapplication.adapters.WishListAdapter;
import com.example.tonyhuang.gooddealapplication.data.ProductsDataSource;
import com.example.tonyhuang.gooddealapplication.models.WishList;

import java.sql.SQLException;
import java.util.ArrayList;

public class WishListTab extends Fragment {


    private ArrayList<WishList> wishList;
    private ProductsDataSource productsDataSource;
    public WishListTab CustomListView = null;
    private ListView wishListListView;
    private WishListAdapter wishListAdapter;
    private Activity mActivity;
    private Button deleteWishList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wishlist_list, container, false);
        deleteWishList = (Button) v.findViewById(R.id.delete_all_wishList);
        wishListListView = (ListView) v.findViewById(R.id.list_for_wishlist);

        productsDataSource = new ProductsDataSource(getActivity());
        CustomListView = this;
        try {
            productsDataSource.open();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        wishList = productsDataSource.getAllWishList();
        //productsDataSource.close();

        Resources res = getResources();

        wishListAdapter = new WishListAdapter(mActivity, wishList, res);
        wishListListView.setAdapter(wishListAdapter);


        deleteWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    productsDataSource.open();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                productsDataSource.deleteAllWishList();
                wishList.clear();
                wishList = productsDataSource.getAllWishList();

                wishListAdapter.notifyDataSetChanged();

                // TODO : Add code here to refresh the WIshListTab

            }


        });

        wishListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, final long id) {
                try {
                    productsDataSource.open();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                //final long new_id = id+1;
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select the action:");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Resources res = getResources();

                        productsDataSource.deleteWishList(id);
                        wishList.clear();
                        wishList = productsDataSource.getAllWishList();
                        wishListAdapter = new WishListAdapter(mActivity, wishList, res);

                        wishListAdapter.notifyDataSetChanged();
                        wishListListView.setAdapter(wishListAdapter);
                        // TODO : Add code here to refresh WishListTab page

                    }
                });


                builder.show();
            }
        });
        productsDataSource.close();
        return v;


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
