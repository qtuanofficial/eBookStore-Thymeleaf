package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Wishlist;
import java.util.List;

public interface WishlistService {
    Wishlist createWishlist(Wishlist wishlist);
    Wishlist updateWishlist(int id, Wishlist wishlist);
    void deleteWishlist(int id);
    Wishlist getWishlistById(int id);
    List<Wishlist> getAllWishlists();
}