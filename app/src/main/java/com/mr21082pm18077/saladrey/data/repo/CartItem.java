package com.mr21082pm18077.saladrey.data.repo;

import org.jspecify.annotations.NonNull;

public class CartItem {
    public long saladId;
    public String name;
    public int qty;
    public double price;   // precio unitario

    public CartItem(long id, String n, int q, double p){
        saladId=id; name=n; qty=q; price=p;
    }
    public double subtotal(){ return qty * price; }
}
