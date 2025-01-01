package com.rankmonkeysvc.controllers;

import com.rankmonkeysvc.auth.JwtSvc;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.dto.jobRole.JobRoleRequest;
import com.rankmonkeysvc.services.JobRoleSvc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import static com.rankmonkeysvc.messages.InfoLogs.ONBOARDING_JOB_ROLE;
import static com.rankmonkeysvc.messages.InfoLogs.UPDATE_JOB_ROLE;

@Slf4j
@RestController
@RequestMapping("/v1/job-role")
public class JobRoleController {
    private final JobRoleSvc jobRoleSvc;
    private final JwtSvc jwtSvc;

    @Autowired
    public JobRoleController(
            JobRoleSvc jobRoleSvc,
            JwtSvc jwtSvc
    ) {
        this.jobRoleSvc = jobRoleSvc;
        this.jwtSvc = jwtSvc;
    }

    @PostMapping("/create")
    public MessageResponse createJobRole(
            @RequestBody @Valid JobRoleRequest jobRoleRequest,
            HttpServletRequest request
    ) {
        String userId = jwtSvc.getUser(request);
        log.info(ONBOARDING_JOB_ROLE, jobRoleRequest.getRoleName());
        return jobRoleSvc.createJobRole(jobRoleRequest, userId);
    }

    @PutMapping("/update/status")
    public MessageResponse updateJobRole(
            @RequestBody @Valid JobRoleRequest jobRoleRequest,
            HttpServletRequest request
    ) {
        String userId = jwtSvc.getUser(request);
        log.info(UPDATE_JOB_ROLE, jobRoleRequest.getId(), jobRoleRequest.getStatus());
        return jobRoleSvc.updateJobRole(jobRoleRequest, userId);
    }
}