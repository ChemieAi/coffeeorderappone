package com.first.coffeeorderappone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.first.coffeeorderappone.Adapter.CartAdapter;
import com.first.coffeeorderappone.Model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    CartAdapter mAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    Button orderbutton;
    TextView orderSummary;
    NavController navController;

    List<CartModel> cartModelList = new ArrayList<>();
    int totalOrderCost=0;
    List<Integer> saveTotalCost=new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);
        firestore = FirebaseFirestore.getInstance();
        mAdapter = new CartAdapter();
        orderbutton = view.findViewById(R.id.orderNow);
        recyclerView = view.findViewById(R.id.cartRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderSummary = view.findViewById(R.id.orderSummary);


        firestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                cartModelList.clear(); //it will be added twice if this not added
                if (task.isSuccessful()){

                    for (DocumentSnapshot ds: task.getResult().getDocuments()){


                        CartModel cartModel = ds.toObject(CartModel.class);
                        cartModelList.add(cartModel);

                        mAdapter.setCartModelList(cartModelList);
                        recyclerView.setAdapter(mAdapter);

                    }
                }

            }
        });

        //adding the total price off all orders
        firestore.collection("Cart").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {

                if (value!=null){

                    for (DocumentSnapshot ds: value.getDocuments()){
                        CartModel cartModel = ds.toObject(CartModel.class);
                        //we are adding all prices into a list of an integers
                        int valueOfAllPrices = cartModel.getTotalprice();
                        saveTotalCost.add(valueOfAllPrices);

                    }

                    for (int i=0; i<saveTotalCost.size(); i++){
                        totalOrderCost += Integer.parseInt(String.valueOf(saveTotalCost.get(i)));
                    }

                    orderSummary.setText("Total is " + String.valueOf(totalOrderCost));

                    totalOrderCost = 0;
                    saveTotalCost.clear();


                }
            }
        });

        //cart should be empty after order now clicked

        orderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Resetting the quantities after the order done.
                firestore.collection("Coffies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            QuerySnapshot tasks = task.getResult();

                            for (DocumentSnapshot ds: tasks.getDocuments()){
                                ds.getReference().update("quantity",0);



                            }
                        }

                    }
                });

                //clearing the cart
                firestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            QuerySnapshot tasks = task.getResult();

                            for (DocumentSnapshot ds: tasks.getDocuments()){
                                ds.getReference().delete();



                            }
                        }

                    }
                });

                navController.navigate(R.id.action_cartFragment_to_allCoffeeListFragment);
                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();


            }
        });

    }
}