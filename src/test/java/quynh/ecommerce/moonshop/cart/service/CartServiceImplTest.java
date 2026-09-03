package quynh.ecommerce.moonshop.cart.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import quynh.ecommerce.moonshop.cart.dto.request.AddCartItemRequest;
import quynh.ecommerce.moonshop.cart.entity.Cart;
import quynh.ecommerce.moonshop.cart.repository.CartRepository;
import quynh.ecommerce.moonshop.cart.service.impl.CartServiceImpl;
import quynh.ecommerce.moonshop.category.entity.Category;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.enums.CartStatus;
import quynh.ecommerce.moonshop.common.enums.ProductStatus;
import quynh.ecommerce.moonshop.common.exception.AppException;
import quynh.ecommerce.moonshop.product.entity.Product;
import quynh.ecommerce.moonshop.product.repository.ProductRepository;
import quynh.ecommerce.moonshop.user.entity.User;
import quynh.ecommerce.moonshop.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CartServiceImplTest {
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartServiceImpl cartService = new CartServiceImpl(cartRepository, productRepository, userRepository);

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u1", null));
        when(userRepository.findById("u1")).thenReturn(Optional.of(User.builder().id("u1").build()));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addItemCreatesActiveCartAndTotalsSelectedItems() {
        Product product = product("p1", 10);
        AddCartItemRequest request = addRequest("p1", 2);

        when(cartRepository.findByUserIdAndStatus("u1", CartStatus.ACTIVE)).thenReturn(Optional.empty());
        when(productRepository.findByIdAndProductStatus("p1", ProductStatus.ACTIVE)).thenReturn(Optional.of(product));

        var result = cartService.addItem(request);

        assertThat(result.getItemCount()).isEqualTo(2);
        assertThat(result.getSelectedCount()).isEqualTo(2);
        assertThat(result.getSelectedSubtotal()).isEqualByComparingTo("200");
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addItemRejectsQuantityAboveStock() {
        when(cartRepository.findByUserIdAndStatus("u1", CartStatus.ACTIVE)).thenReturn(Optional.empty());
        when(productRepository.findByIdAndProductStatus("p1", ProductStatus.ACTIVE)).thenReturn(Optional.of(product("p1", 1)));

        assertThatThrownBy(() -> cartService.addItem(addRequest("p1", 2)))
                .isInstanceOfSatisfying(AppException.class,
                        ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR));
    }

    @Test
    void addItemRejectsInactiveOrMissingProduct() {
        when(cartRepository.findByUserIdAndStatus("u1", CartStatus.ACTIVE)).thenReturn(Optional.empty());
        when(productRepository.findByIdAndProductStatus("p1", ProductStatus.ACTIVE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addItem(addRequest("p1", 1)))
                .isInstanceOfSatisfying(AppException.class,
                        ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private AddCartItemRequest addRequest(String productId, int quantity) {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setProductId(productId);
        request.setQuantity(quantity);
        return request;
    }

    private Product product(String id, int stock) {
        return Product.builder()
                .id(id)
                .name("Ao")
                .slug("ao")
                .image("image.jpg")
                .price(BigDecimal.valueOf(100))
                .stock(stock)
                .category(Category.builder().id("c1").name("Ao nu").build())
                .productStatus(ProductStatus.ACTIVE)
                .build();
    }
}
