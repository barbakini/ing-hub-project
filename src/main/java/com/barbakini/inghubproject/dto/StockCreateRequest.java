package com.barbakini.inghubproject.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@Validated
public class StockCreateRequest {
    @NotBlank(message = "The name must not be blank")
    @Size(min = 3, max = 255, message = "The name must be between 3 and 255 characters.")
    private String name;
    @NotBlank(message = "The description must not be blank")
    @Size(min = 3, max = 255, message = "The description must be between 3 and 255 characters.")
    private String description;
    @NotNull(message = "The currentPrice cannot be null")
    @DecimalMin(value = "0", inclusive = false, message = "the currentPrice must be greater than zero")
    @Digits(integer = 20, fraction = 10, message = "the currentPrice can have 20 digits and 10 fragment")
    private BigDecimal currentPrice;
}
