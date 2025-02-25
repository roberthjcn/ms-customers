package com.sofka.ms_customers.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiToResponse<T> {
    private String mensaje;
    private T data;
}
