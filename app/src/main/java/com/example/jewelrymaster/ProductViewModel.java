package com.example.jewelrymaster;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class ProductViewModel extends ViewModel {
    private Map<String, Boolean> likedProductsMap = new HashMap<>();

    public boolean isProductLiked(String productId) {
        Boolean isLiked = likedProductsMap.get(productId);
        return isLiked != null && isLiked;
    }

    public void setProductLiked(String productId, boolean liked) {
        likedProductsMap.put(productId, liked);
    }
}

