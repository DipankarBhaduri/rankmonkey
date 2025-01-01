package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.dto.jobRole.JobRoleRequest;
import javax.validation.Valid;

public interface JobRoleSvc {
    public MessageResponse createJobRole(
            JobRoleRequest request, String userId
    );

    MessageResponse updateJobRole(
            @Valid JobRoleRequest jobRoleRequest,
            String userId
    );
}