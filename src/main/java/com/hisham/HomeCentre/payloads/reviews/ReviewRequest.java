package com.hisham.HomeCentre.payloads.reviews;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    @NotNull
    @Min(1)
    @Max(5)
    private Float rating;

    @NotBlank
    @Size(max = 500)
    private String comment;
}
