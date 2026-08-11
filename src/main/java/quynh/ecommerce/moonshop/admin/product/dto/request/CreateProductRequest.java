package quynh.ecommerce.moonshop.admin.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    private String image;

    private String imagePublicId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Original price must be greater than or equal to 0")
    private BigDecimal originalPrice;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private int stock;

    @NotBlank(message = "Category is required")
    private String categoryId;

    private String status;

    @JsonProperty("isNew")
    private Boolean isNew;

    @JsonProperty("isFeatured")
    private Boolean isFeatured;

    @JsonProperty("isBestSeller")
    private Boolean isBestSeller;
}
