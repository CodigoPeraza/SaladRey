package com.mr21082pm18077.saladrey.data.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mr21082pm18077.saladrey.data.local.Salad;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartRepository {
    private static CartRepository INSTANCE;
    private final MutableLiveData<List<CartItem>> cart = new MutableLiveData<>(new ArrayList<>());

    public static CartRepository get() {
        if (INSTANCE == null) { INSTANCE = new CartRepository(); }
        return INSTANCE;
    }
    public LiveData<List<CartItem>> liveCart() { return cart; }
    // métodos add/remove/clear aquí …
    public void add(Salad s) {
        List<CartItem> current = new ArrayList<>(cart.getValue());
        Optional<CartItem> found = current.stream()
                .filter(ci -> ci.saladId == s.id).findFirst();
        if (found.isPresent()) {
            CartItem ci = found.get();
            ci.qty++;
        } else {
            current.add(new CartItem(s.id, s.name, 1, s.price));
        }
        cart.postValue(current);
    }
    public void remove(CartItem ci) {
        List<CartItem> cur = new ArrayList<>(cart.getValue());
        cur.remove(ci);
        cart.postValue(cur);
    }
    public void clear() { cart.postValue(new ArrayList<>()); }
    public double total() {
        return cart.getValue()
                .stream()
                .mapToDouble(CartItem::subtotal)
                .sum();
    }
}