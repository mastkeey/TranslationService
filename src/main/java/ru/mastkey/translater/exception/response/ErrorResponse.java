package ru.mastkey.translater.exception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Integer status;
    private String code;
    private String message;
    private Map<String, String> info;

}
