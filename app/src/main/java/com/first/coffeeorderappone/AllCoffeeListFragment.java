package com.first.coffeeorderappone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.first.coffeeorderappone.Adapter.CoffeeAdapter;
import com.first.coffeeorderappone.MVVM.CoffeeViewModel;
import com.first.coffeeorderappone.Model.CartModel;
import com.first.coffeeorderappone.Model.CoffeeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllCoffeeListFragment extends Fragment implements CoffeeAdapter.GetOneCoffee {

    FirebaseFirestore firebaseFirestore;
    CoffeeAdapter mAdapter;
    RecyclerView recyclerView;
    CoffeeViewModel viewModel;
    NavController navController;
    int quantity= 0;
    TextView quantityOnFab;
    FloatingActionButton fab;
    List<Integer> savequantity = new ArrayList<>();
    int quantitysum= 0;

    public AllCoffeeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_coffee_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recViewAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CoffeeAdapter(this);
        navController = Navigation.findNavController(view);
        quantityOnFab = view.findViewById(R.id.quantityOnFab);
        fab = view.findViewById(R.id.fab);
        viewModel = new ViewModelProvider(getActivity()).get(CoffeeViewModel.class);
        viewModel.getCoffeeList().observe(getViewLifecycleOwner(), new Observer<List<CoffeeModel>>() {
            @Override
            public void onChanged(List<CoffeeModel> coffeeModels) {

                mAdapter.setCoffeeModelList(coffeeModels);
                recyclerView.setAdapter(mAdapter);

            }
        });


        quantity = AllCoffeeListFragmentArgs.fromBundle(getArguments()).getQuantity();

        firebaseFirestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (DocumentSnapshot ds: task.getResult().getDocuments()){
                        CartModel cartModel = ds.toObject(CartModel.class);
                        int initialquantity= cartModel.getQuantity();

                        savequantity.add(initialquantity);

                    }

                    for (int i=0; i< savequantity.size(); i++ ){

                        quantitysum+= Integer.parseInt(String.valueOf(savequantity.get(i)));

                    }

                    quantityOnFab.setText(String.valueOf(quantitysum));
                    quantitysum =0;
                    savequantity.clear(); //Unless we add something new to our list // previous records are cleared.
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_allCoffeeListFragment_to_cartFragment);
            }
        });



    }

    @Override
    public void clickedCoffee(int position, List<CoffeeModel> coffeeModels) {
        String coffeeid = coffeeModels.get(position).getCoffeeid();
        String description = coffeeModels.get(position).getDescription();
        String coffeename = coffeeModels.get(position).getCoffeename();
        int price = coffeeModels.get(position).getPrice();
        String imageURL = coffeeModels.get(position).getImageURL();

        AllCoffeeListFragmentDirections.ActionAllCoffeeListFragmentToCoffeeDetailFragment
                action = AllCoffeeListFragmentDirections.actionAllCoffeeListFragmentToCoffeeDetailFragment();

        action.setCoffeename(coffeename);
        action.setDescription(description);
        action.setImageurl(imageURL);
        action.setPrice(price);
        action.setId(coffeeid);

    navController.navigate(action);
    }

}