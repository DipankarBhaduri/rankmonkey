package com.rankmonkeysvc.dto.skill;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SkillCreationRequest {
    private String skillName;
}
