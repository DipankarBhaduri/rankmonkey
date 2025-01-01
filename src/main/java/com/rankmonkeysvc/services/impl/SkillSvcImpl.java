package com.rankmonkeysvc.services.impl;

import com.rankmonkeysvc.constants.ActivationStatus;
import com.rankmonkeysvc.constants.Role;
import com.rankmonkeysvc.constants.UserStatus;
import com.rankmonkeysvc.dao.JobRole;
import com.rankmonkeysvc.dao.Skill;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.dto.skill.SkillCreationRequest;
import com.rankmonkeysvc.exceptions.DuplicateRecordException;
import com.rankmonkeysvc.exceptions.InvalidAuthorizationException;
import com.rankmonkeysvc.repositories.SkillRepository;
import com.rankmonkeysvc.repositories.UserInfoRepository;
import com.rankmonkeysvc.services.SkillSvc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.rankmonkeysvc.messages.ErrorLogs.*;
import static com.rankmonkeysvc.messages.ErrorMessages.*;
import static com.rankmonkeysvc.messages.SuccessMessages.JOB_ROLE_CREATED_SUCCESSFULLY;
import static com.rankmonkeysvc.messages.SuccessMessages.SKILL_CREATED_SUCCESSFULLY;

@Slf4j
@Component
public class SkillSvcImpl implements SkillSvc {
    private final SkillRepository skillRepository;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public SkillSvcImpl(
            SkillRepository skillRepository,
            UserInfoRepository userInfoRepository
    ) {
        this.skillRepository = skillRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public MessageResponse createSkill(SkillCreationRequest skillCreationRequest, String userId) {
        AtomicReference<String> email = new AtomicReference<>();
        validateUserPermission(userId, email);

        skillRepository.findBySkillNameAndStatus(skillCreationRequest.getSkillName(), ActivationStatus.ACTIVE).ifPresentOrElse(
                skill -> {
                    log.error(SKILL_ALREADY_EXISTS, skillCreationRequest.getSkillName());
                    throw new DuplicateRecordException(SKILL_DUPLICATE_CREATION_NOT_ALLOWED);
                },
                () -> {
                    skillRepository.findBySkillNameAndStatus(skillCreationRequest.getSkillName(), ActivationStatus.DE_ACTIVATED).ifPresentOrElse(
                            skill -> {
                                skill.setStatus(ActivationStatus.ACTIVE)
                                        .setUpdatedBy(email.get());
                                skillRepository.save(skill);
                            },
                            () -> {
                                Skill skill = new Skill()
                                        .setSkillName(skillCreationRequest.getSkillName())
                                        .setStatus(ActivationStatus.ACTIVE)
                                        .setCreatedBy(email.get());

                                skillRepository.save(skill);
                            }
                    );
                }
        );

        return new MessageResponse().setMessage(SKILL_CREATED_SUCCESSFULLY);
    }

    private void validateUserPermission(String userId, AtomicReference<String> email) {
        userInfoRepository.findByIdAndStatusAndRole(UUID.fromString(userId), UserStatus.VERIFIED, Role.SUPER_ADMIN).ifPresentOrElse(
                user -> {
                    email.set(user.getEmail());
                },
                () -> {
                    log.error(String.format(USER_DOES_NOT_HAVE_CREATION_ACCESS, userId));
                    throw new InvalidAuthorizationException(USER_DOES_NOT_HAVE_CREATION_ACCESS_MESSAGE);
                }
        );
    }
}
