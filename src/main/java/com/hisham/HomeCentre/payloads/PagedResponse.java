package com.hisham.HomeCentre.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;
    private boolean isLast;
}
