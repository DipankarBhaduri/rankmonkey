package com.rankmonkeysvc.dto.onboarding;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterRequest {
	@NotEmpty
    private String firstName;
	@NotEmpty
    private String lastName;
	@NotEmpty
    private String email;
	@NotEmpty
    private String password;
}