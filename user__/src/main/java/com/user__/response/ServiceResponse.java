package com.user__.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServiceResponse {
    @JsonProperty("message")
    public String message;
    @JsonProperty("errorMessage")
    public String errorMessage;
    @JsonProperty("status")
    public String status;
}
