package quynh.ecommerce.moonshop.admin.category.dto.response;

import lombok.*;
import quynh.ecommerce.moonshop.common.enums.CategoryStatus;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCategoryResponse {
    private String id;
    private String name;
    private String description;
    private String icon;
    private CategoryStatus status;
    private LocalDate createdAt;
    private long productCount;
}
