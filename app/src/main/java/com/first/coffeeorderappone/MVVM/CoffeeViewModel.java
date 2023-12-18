package com.first.coffeeorderappone.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.first.coffeeorderappone.Model.CoffeeModel;

import java.util.List;

public class CoffeeViewModel  extends ViewModel implements Repository.CoffeeList {
    MutableLiveData<List<CoffeeModel>> mutableLiveData = new MutableLiveData<List<CoffeeModel>>();
    Repository repository = new Repository(this);

    public CoffeeViewModel() {
        repository.getCoffee();
    }

    public LiveData<List<CoffeeModel>> getCoffeeList(){
        return mutableLiveData;
    }
    @Override
    public void coffeeLists(List<CoffeeModel> coffeeModels) {
        mutableLiveData.setValue(coffeeModels);
    }
}
