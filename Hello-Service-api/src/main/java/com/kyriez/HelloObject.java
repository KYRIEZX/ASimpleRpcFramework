package com.kyriez;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
