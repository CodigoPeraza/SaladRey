package com.mr21082pm18077.saladrey.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.mr21082pm18077.saladrey.data.repo.CartItem;
import com.mr21082pm18077.saladrey.data.repo.CartRepository;
import java.util.List;

public class CartViewModel extends ViewModel {
    private final CartRepository repo = CartRepository.get();
    public LiveData<List<CartItem>> items = repo.liveCart();

    public void delete(CartItem ci){ repo.remove(ci); }
    public void clear(){ repo.clear(); }
    public double total(){ return repo.total(); }
}
