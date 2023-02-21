package com.telsoft.libcore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessageDTO {
    private Object processGroups;
}
