package com.email.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Email dto.
 */
@Getter
@Setter
@Builder
public class EmailDto {
    private String from;
    private String subject;
    private String date;
}
