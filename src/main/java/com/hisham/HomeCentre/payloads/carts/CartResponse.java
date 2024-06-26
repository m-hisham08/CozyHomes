package com.hisham.HomeCentre.payloads.carts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hisham.HomeCentre.payloads.UserSummary;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse {
    private Long id;
    private UserSummary user;
    private Set<CartItemResponse> items = new HashSet<>();
}
