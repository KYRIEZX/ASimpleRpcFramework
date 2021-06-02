package com.kyriez;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@AllArgsConstructor
@Data
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
