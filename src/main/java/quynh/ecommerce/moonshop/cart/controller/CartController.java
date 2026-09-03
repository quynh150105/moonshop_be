package quynh.ecommerce.moonshop.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quynh.ecommerce.moonshop.cart.dto.request.AddCartItemRequest;
import quynh.ecommerce.moonshop.cart.dto.request.MergeCartRequest;
import quynh.ecommerce.moonshop.cart.dto.request.SelectCartItemRequest;
import quynh.ecommerce.moonshop.cart.dto.request.UpdateCartItemRequest;
import quynh.ecommerce.moonshop.cart.dto.response.CartResponse;
import quynh.ecommerce.moonshop.cart.service.CartService;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;

@RestApiV1
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping(UrlConstant.Cart.CART)
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping(UrlConstant.Cart.CART)
    public ResponseEntity<CartResponse> addItem(@Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(request));
    }

    @PutMapping(UrlConstant.Cart.CART_ITEM)
    public ResponseEntity<CartResponse> updateQuantity(@PathVariable String productId,
                                                       @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateQuantity(productId, request));
    }

    @DeleteMapping(UrlConstant.Cart.CART_ITEM)
    public ResponseEntity<CartResponse> removeItem(@PathVariable String productId) {
        return ResponseEntity.ok(cartService.removeItem(productId));
    }

    @PutMapping(UrlConstant.Cart.CART_ITEM_SELECTED)
    public ResponseEntity<CartResponse> selectItem(@PathVariable String productId,
                                                   @Valid @RequestBody SelectCartItemRequest request) {
        return ResponseEntity.ok(cartService.selectItem(productId, request.getSelected()));
    }

    @PutMapping(UrlConstant.Cart.SELECT_ALL)
    public ResponseEntity<CartResponse> selectAll(@RequestParam(defaultValue = "true") boolean selected) {
        return ResponseEntity.ok(cartService.selectAll(selected));
    }

    @DeleteMapping(UrlConstant.Cart.CART)
    public ResponseEntity<CartResponse> clearCart() {
        return ResponseEntity.ok(cartService.clearCart());
    }

    @PostMapping(UrlConstant.Cart.MERGE)
    public ResponseEntity<CartResponse> mergeCart(@Valid @RequestBody MergeCartRequest request) {
        return ResponseEntity.ok(cartService.mergeCart(request));
    }

    @PostMapping(UrlConstant.Cart.VALIDATE)
    public ResponseEntity<CartResponse> validateCart() {
        return ResponseEntity.ok(cartService.validateCart());
    }
}
