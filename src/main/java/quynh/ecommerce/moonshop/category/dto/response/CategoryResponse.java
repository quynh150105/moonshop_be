package quynh.ecommerce.moonshop.category.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private String id;

    private String name;

    private String slug;

    private String icon;

    private long productCount;
}
