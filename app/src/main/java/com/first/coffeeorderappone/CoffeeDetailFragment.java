package com.first.coffeeorderappone;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.first.coffeeorderappone.Model.CoffeeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Objects;


public class CoffeeDetailFragment extends Fragment {

    NavController navController;
    int quantity = 0;
    FirebaseFirestore firebaseFirestore;
    Button add, sub, order;
    TextView coffeename, description, quantityview, orderINFO;
    ImageView imageView;
    String coffeid, name, coffeedescription, imageURL;
    int price =0;
    int totalPrice= 0;


    public CoffeeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coffee_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.CoffeeDetailImage);
        coffeename = view.findViewById(R.id.coffeenamedetail);
        description = view.findViewById(R.id.coffeedetaildetail);
        add = view.findViewById(R.id.incrementcoffee);
        sub = view.findViewById(R.id.decrementcoffee);
        quantityview = view.findViewById(R.id.quantityDETAILNUMBER);
        firebaseFirestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);
        order = view.findViewById(R.id.orderdetail);
        orderINFO = view.findViewById(R.id.orderINFO);

        name = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getCoffeename();
        coffeid = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getId();
        imageURL = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getImageurl();
        coffeedescription = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getDescription();
        price = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getPrice();


        Glide.with(view.getContext()).load(imageURL).into(imageView);
        coffeename.setText(name + " $" + String.valueOf(price));
        description.setText(coffeedescription);

        //Fetching the recent quantity in firestore and displaying it
        firebaseFirestore.collection("Coffies").document(coffeid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {

                CoffeeModel coffeeModel = value.toObject(CoffeeModel.class);
                quantity = coffeeModel.getQuantity();
                quantityview.setText(String.valueOf(quantity));
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity == 15){
                    Toast.makeText(getContext(), "You have reached the maximum order quantity.", Toast.LENGTH_SHORT).show();
                    quantityview.setText(String.valueOf(quantity));


                    }else {
                        quantity++;

                        quantityview.setText(String.valueOf(quantity));

                        totalPrice = quantity*price;
                        orderINFO.setText(String.valueOf("Total Price is: $"+ totalPrice));

                        firebaseFirestore.collection("Coffies").document(coffeid).update("quantity",quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {

                        }
                    });

                    firebaseFirestore.collection("Cart").document(name).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });


                }

            }
        });


        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity == 0) {
                    Toast.makeText(getContext(), "Nothing in Cart!", Toast.LENGTH_SHORT).show();
                    quantityview.setText(String.valueOf(quantity));



                }

                else {

                    quantity--;

                    quantityview.setText(String.valueOf(quantity));

                    totalPrice = quantity*price;
                    orderINFO.setText(String.valueOf("Total Price is: "+ totalPrice));

                    firebaseFirestore.collection("Coffies").document(coffeid).update("quantity",quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });

                    firebaseFirestore.collection("Cart").document(name).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });

                }

            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddToCart();

            }
        });

    }

    private void AddToCart() {

        if (quantity == 0){
            navController.navigate(R.id.action_coffeeDetailFragment_to_allCoffeeListFragment);
            Toast.makeText(getContext(), "You did not order " + name, Toast.LENGTH_SHORT).show();

            //if quantity is zero delete the document ( for clearing the cart)
            firebaseFirestore.collection("Cart").document(name).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {

                }
            });

        } else {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("coffeename",name);
            hashMap.put("quantity", quantity);
            hashMap.put("totalprice", totalPrice);
            hashMap.put("coffeeid",coffeid);
            hashMap.put("description", coffeedescription);
            hashMap.put("imageURL",imageURL);

            //creting new collection for cart
            firebaseFirestore.collection("Cart").document(name).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {


                    if (task.isSuccessful()){

                        Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();

                        // sending value to be displayed on the cart icon
                        CoffeeDetailFragmentDirections.ActionCoffeeDetailFragmentToAllCoffeeListFragment
                                action = CoffeeDetailFragmentDirections.actionCoffeeDetailFragmentToAllCoffeeListFragment();
                        action.setQuantity(quantity);
                        navController.navigate(action);

                    }
                    
                }
            });


        }

    }
}