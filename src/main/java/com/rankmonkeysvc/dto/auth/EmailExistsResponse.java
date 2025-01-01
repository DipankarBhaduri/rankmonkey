package com.rankmonkeysvc.dto.auth;

import com.rankmonkeysvc.constants.Role;
import lombok.Data;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailExistsResponse {
    private Boolean emailExists;
    private String status;
    private Role role;
    private String message;
}