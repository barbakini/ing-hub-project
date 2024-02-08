package com.barbakini.inghubproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class StockExchangeCreateRequest {
    @NotBlank(message = "The name must not be blank")
    @Size(min = 3, max = 255, message = "The name must be between 3 and 255 characters.")
    private String name;
    @NotBlank(message = "The description must not be blank")
    @Size(min = 3, max = 255, message = "The description must be between 3 and 255 characters.")
    private String description;
}
