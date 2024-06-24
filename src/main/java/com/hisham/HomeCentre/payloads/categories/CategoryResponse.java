package com.hisham.HomeCentre.payloads.categories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hisham.HomeCentre.payloads.UserSummary;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    private Long id;
    private String name;
    private Instant createdAt;
    private Instant lastModifiedAt;
    private UserSummary createdBy;
    private UserSummary lastModifiedBy;
}
