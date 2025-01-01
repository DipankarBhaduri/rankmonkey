package com.rankmonkeysvc.dto.jobRole;

import com.rankmonkeysvc.constants.ActivationStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class JobRoleRequest {
    @NotEmpty
    private String roleName;
    private Long id;
    private ActivationStatus status;
}