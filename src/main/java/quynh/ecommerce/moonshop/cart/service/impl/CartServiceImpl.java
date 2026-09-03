package quynh.ecommerce.moonshop.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quynh.ecommerce.moonshop.cart.dto.request.AddCartItemRequest;
import quynh.ecommerce.moonshop.cart.dto.request.MergeCartRequest;
import quynh.ecommerce.moonshop.cart.dto.request.UpdateCartItemRequest;
import quynh.ecommerce.moonshop.cart.dto.response.CartItemResponse;
import quynh.ecommerce.moonshop.cart.dto.response.CartResponse;
import quynh.ecommerce.moonshop.cart.entity.Cart;
import quynh.ecommerce.moonshop.cart.entity.CartItem;
import quynh.ecommerce.moonshop.cart.repository.CartRepository;
import quynh.ecommerce.moonshop.cart.service.CartService;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.enums.CartStatus;
import quynh.ecommerce.moonshop.common.enums.ProductStatus;
import quynh.ecommerce.moonshop.common.exception.AppException;
import quynh.ecommerce.moonshop.product.entity.Product;
import quynh.ecommerce.moonshop.product.repository.ProductRepository;
import quynh.ecommerce.moonshop.user.entity.User;
import quynh.ecommerce.moonshop.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart() {
        return toResponse(getActiveCart(getAuthenticatedUser()));
    }

    @Override
    @Transactional
    public CartResponse addItem(AddCartItemRequest request) {
        User user = getAuthenticatedUser();
        Cart cart = getActiveCart(user);
        addOrIncrement(cart, request.getProductId(), request.getQuantity());
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse updateQuantity(String productId, UpdateCartItemRequest request) {
        Cart cart = getActiveCart(getAuthenticatedUser());
        CartItem item = findItem(cart, productId);
        validateQuantity(item.getProduct(), request.getQuantity());
        item.setQuantity(request.getQuantity());
        item.setPriceSnapshot(item.getProduct().getPrice());
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse removeItem(String productId) {
        Cart cart = getActiveCart(getAuthenticatedUser());
        CartItem item = findItem(cart, productId);
        cart.getItems().remove(item);
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse selectItem(String productId, boolean selected) {
        Cart cart = getActiveCart(getAuthenticatedUser());
        findItem(cart, productId).setSelected(selected);
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse selectAll(boolean selected) {
        Cart cart = getActiveCart(getAuthenticatedUser());
        cart.getItems().forEach(item -> item.setSelected(selected));
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse clearCart() {
        Cart cart = getActiveCart(getAuthenticatedUser());
        cart.getItems().clear();
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse mergeCart(MergeCartRequest request) {
        Cart cart = getActiveCart(getAuthenticatedUser());
        request.getItems().forEach(item -> addOrIncrement(cart, item.getProductId(), item.getQuantity()));
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse validateCart() {
        Cart cart = getActiveCart(getAuthenticatedUser());
        cart.getItems().forEach(item -> {
            Product product = findActiveProduct(item.getProduct().getId());
            validateQuantity(product, item.getQuantity());
            item.setProduct(product);
            item.setPriceSnapshot(product.getPrice());
        });
        return toResponse(cartRepository.save(cart));
    }

    private void addOrIncrement(Cart cart, String productId, int quantity) {
        Product product = findActiveProduct(productId);
        CartItem item = cart.findItem(productId)
                .orElseGet(() -> {
                    CartItem newItem = CartItem.builder()
                            .product(product)
                            .quantity(0)
                            .selected(true)
                            .priceSnapshot(product.getPrice())
                            .build();
                    cart.addItem(newItem);
                    return newItem;
                });
        int newQuantity = item.getQuantity() + quantity;
        validateQuantity(product, newQuantity);
        item.setProduct(product);
        item.setQuantity(newQuantity);
        item.setPriceSnapshot(product.getPrice());
    }

    private Cart getActiveCart(User user) {
        return cartRepository.findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE)
                .orElseGet(() -> Cart.builder()
                        .user(user)
                        .status(CartStatus.ACTIVE)
                        .build());
    }

    private User getAuthenticatedUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private Product findActiveProduct(String productId) {
        return productRepository.findByIdAndProductStatus(productId, ProductStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private CartItem findItem(Cart cart, String productId) {
        return cart.findItem(productId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
    }

    private void validateQuantity(Product product, int quantity) {
        if (quantity < 1 || quantity > product.getStock()) {
            throw new AppException(ErrorCode.VALIDATION_ERROR);
        }
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        return CartResponse.builder()
                .items(items)
                .itemCount(items.stream().mapToInt(CartItemResponse::getQuantity).sum())
                .selectedCount(items.stream().filter(CartItemResponse::isSelected).mapToInt(CartItemResponse::getQuantity).sum())
                .subtotal(sum(items, false))
                .selectedSubtotal(sum(items, true))
                .build();
    }

    private CartItemResponse toItemResponse(CartItem item) {
        Product product = item.getProduct();
        BigDecimal lineTotal = item.getPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity()));
        return CartItemResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .image(product.getImage())
                .price(item.getPriceSnapshot())
                .quantity(item.getQuantity())
                .stock(product.getStock())
                .selected(item.isSelected())
                .lineTotal(lineTotal)
                .build();
    }

    private BigDecimal sum(List<CartItemResponse> items, boolean selectedOnly) {
        return items.stream()
                .filter(item -> !selectedOnly || item.isSelected())
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
