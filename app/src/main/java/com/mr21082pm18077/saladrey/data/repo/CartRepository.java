package com.mr21082pm18077.saladrey.data.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private static CartRepository INSTANCE;
    private final MutableLiveData<List<CartItem>> cart = new MutableLiveData<>(new ArrayList<>());

    public static CartRepository get() {
        if (INSTANCE == null) { INSTANCE = new CartRepository(); }
        return INSTANCE;
    }
    public LiveData<List<CartItem>> liveCart() { return cart; }
    // métodos add/remove/clear aquí …
}
