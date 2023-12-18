package com.first.coffeeorderappone.MVVM;

import android.util.Log;

import androidx.annotation.NonNull;

import com.first.coffeeorderappone.Model.CoffeeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    List<CoffeeModel> coffeeModelList = new ArrayList<>();

    public void  getCoffee(){

        firebaseFirestore.collection("Coffies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {

                coffeeModelList.clear();
                if (task.isSuccessful()){
                    for (DocumentSnapshot ds: task.getResult().getDocuments()){
                        CoffeeModel coffeeModel = ds.toObject(CoffeeModel.class);

                        Log.d("REPO", "testing: " + coffeeModel.toString());
                    }

                }


            }
        });
    }
}
