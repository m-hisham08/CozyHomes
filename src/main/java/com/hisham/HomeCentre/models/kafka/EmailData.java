package com.hisham.HomeCentre.models.kafka;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailData {
    private String to;
    private String subject;
    private String content;
}
