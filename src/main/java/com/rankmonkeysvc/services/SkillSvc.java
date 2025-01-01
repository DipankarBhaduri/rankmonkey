package com.rankmonkeysvc.services;

import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.dto.skill.SkillCreationRequest;
import javax.validation.Valid;

public interface SkillSvc {

    MessageResponse createSkill(
            @Valid SkillCreationRequest skillCreationRequest,
            String userId
    );
}