package quynh.ecommerce.moonshop.cart.service;

import quynh.ecommerce.moonshop.cart.dto.request.AddCartItemRequest;
import quynh.ecommerce.moonshop.cart.dto.request.MergeCartRequest;
import quynh.ecommerce.moonshop.cart.dto.request.UpdateCartItemRequest;
import quynh.ecommerce.moonshop.cart.dto.response.CartResponse;

public interface CartService {
    CartResponse getCart();

    CartResponse addItem(AddCartItemRequest request);

    CartResponse updateQuantity(String productId, UpdateCartItemRequest request);

    CartResponse removeItem(String productId);

    CartResponse selectItem(String productId, boolean selected);

    CartResponse selectAll(boolean selected);

    CartResponse clearCart();

    CartResponse mergeCart(MergeCartRequest request);

    CartResponse validateCart();
}
