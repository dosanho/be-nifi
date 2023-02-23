package com.telsoft.nifiservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
public class VariablesDTO {
    private String id;
    private Map<String, String> variables;
}
