package com.rankmonkeysvc.controllers;

import com.rankmonkeysvc.auth.JwtSvc;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.dto.skill.SkillCreationRequest;
import com.rankmonkeysvc.services.SkillSvc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import static com.rankmonkeysvc.messages.InfoLogs.*;

@Slf4j
@RestController
@RequestMapping("/v1/skill")
public class SkillController {
    private final SkillSvc skillSvc;
    private final JwtSvc jwtSvc;

    @Autowired
    public SkillController(
            SkillSvc skillSvc,
            JwtSvc jwtSvc
    ) {
        this.skillSvc = skillSvc;
        this.jwtSvc = jwtSvc;
    }

    @PostMapping("/create")
    public MessageResponse createSkill(
            @RequestBody @Valid SkillCreationRequest skillCreationRequest,
            HttpServletRequest request
    ) {
        String userId = jwtSvc.getUser(request);
        log.info(ONBOARDING_SKILL, skillCreationRequest.getSkillName());
        return skillSvc.createSkill(skillCreationRequest, userId);
    }

//    @PutMapping("/update/status")
//    public MessageResponse updateJobRole(
//            @RequestBody @Valid JobRoleRequest jobRoleRequest,
//            HttpServletRequest request
//    ) {
//        String userId = jwtSvc.getUser(request);
//        log.info(UPDATE_JOB_ROLE, jobRoleRequest.getId(), jobRoleRequest.getStatus());
//        return jobRoleSvc.updateJobRole(jobRoleRequest, userId);
//    }
}