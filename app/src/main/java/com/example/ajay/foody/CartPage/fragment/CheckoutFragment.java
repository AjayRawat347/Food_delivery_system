package com.example.ajay.foody.CartPage.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajay.foody.CartPage.adapter.CheckoutAdapter;
import com.example.ajay.foody.R;
import com.example.ajay.foody.controller.SPManipulation;
import com.example.ajay.foody.controller.ShoppingCartItem;


public class CheckoutFragment extends Fragment {


    // Fragment Component Initialization
    private RecyclerView mRecyclerView;
    private TextView mTextMobile, mTextTotal, mTextEditAddress, mTextEditMobil;
    public static TextView mTextAddress;
    private Button mButtonCheckout, mButtonCancel;


    public CheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_checkout);
        mRecyclerView.setAdapter(new CheckoutAdapter(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initial text
        mTextMobile = (TextView) view.findViewById(R.id.checkout_mobile);
        mTextMobile.setText(SPManipulation.getInstance(getActivity()).getMobile());
        mTextAddress = (TextView) view.findViewById(R.id.checkout_address);
        mTextAddress.setText(SPManipulation.getInstance(getContext()).getAddress());
        mTextTotal = (TextView) view.findViewById(R.id.checkout_total);
        mTextEditMobil = (TextView) view.findViewById(R.id.checkout_edit_mobile);
        mTextEditMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                Toast.makeText(getContext(), "Edit Number", Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_set_mobile,(ViewGroup) view.findViewById(R.id.dialog_mobile));
                new AlertDialog.Builder(getActivity()).setTitle("Please Input Contact Information").setIcon(
                        android.R.drawable.ic_dialog_dialer).setView(
                        layout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog dialog = (Dialog) dialogInterface;
                        EditText inputMobile = (EditText) dialog.findViewById(R.id.dialog_et_mobile);
                        if (inputMobile.getText().toString().isEmpty()){
                            return;
                        }
                        try{
                            long number = Long.valueOf(inputMobile.getText().toString());
                            SPManipulation.getInstance(getActivity()).setMobile(inputMobile.getText().toString());
                            mTextMobile.setText(inputMobile.getText().toString());
                        }catch (Exception e){
                            Toast.makeText(getActivity(), "Please Input Correct Phone Number!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", null).show();
            }
        });
        mTextEditAddress = (TextView) view.findViewById(R.id.checkout_edit_addr);

        mTextEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_set_location,(ViewGroup) view.findViewById(R.id.dialog_location));
                new AlertDialog.Builder(getActivity()).setTitle("Please Input Delivery Location").setIcon(
                        android.R.drawable.ic_dialog_dialer).setView(
                        layout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog dialog = (Dialog) dialogInterface;
                        EditText inputLocation = (EditText) dialog.findViewById(R.id.dialog_et_location);
                        if (inputLocation.getText().toString().isEmpty()){
                            return;
                        }
                        mTextAddress.setText(inputLocation.getText().toString());
                    }
                })
                        .setNeutralButton("Show Map", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent mapAct = new Intent(getActivity(), MapsActivity.class);
//                                startActivity(mapAct);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        mTextTotal.setText(String.valueOf(ShoppingCartItem.getInstance(getContext()).getPrice() * 1.06 + 1.99));

        return view;
    }

}
