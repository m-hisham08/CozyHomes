package com.hisham.HomeCentre.payloads.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CategoryRequest {
    @NotBlank
    @Size(max = 40)
    private String name;
}
