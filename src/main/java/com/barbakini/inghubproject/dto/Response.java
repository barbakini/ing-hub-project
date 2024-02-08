package com.barbakini.inghubproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<E> {
    private boolean success;
    private String msg;
    private E data;
    private String errMsg;
}
