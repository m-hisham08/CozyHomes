package com.hisham.HomeCentre.payloads;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSummary {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
}
