package com.hisham.HomeCentre.payloads.categories;

import com.hisham.HomeCentre.payloads.UserSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private Instant createdAt;
    private Instant lastModifiedAt;
    private UserSummary createdBy;
    private UserSummary lastModifiedBy;
}
