package com.kyriez.Serializer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializerCode {
    KYRO(0),
    JSON(1);
    private final int code;
}
