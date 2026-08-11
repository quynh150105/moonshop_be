package quynh.ecommerce.moonshop.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private List<T> items;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;

    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .items(page.getContent())
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
