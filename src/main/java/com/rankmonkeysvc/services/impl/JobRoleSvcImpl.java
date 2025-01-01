package com.rankmonkeysvc.services.impl;

import com.rankmonkeysvc.constants.ActivationStatus;
import com.rankmonkeysvc.constants.Role;
import com.rankmonkeysvc.constants.UserStatus;
import com.rankmonkeysvc.dao.JobRole;
import com.rankmonkeysvc.dto.common.MessageResponse;
import com.rankmonkeysvc.dto.jobRole.JobRoleRequest;
import com.rankmonkeysvc.exceptions.DuplicateRecordException;
import com.rankmonkeysvc.exceptions.InvalidAuthorizationException;
import com.rankmonkeysvc.exceptions.InvalidRequestException;
import com.rankmonkeysvc.repositories.JobRoleRepository;
import com.rankmonkeysvc.repositories.UserInfoRepository;
import com.rankmonkeysvc.services.EventLogSvc;
import com.rankmonkeysvc.services.JobRoleSvc;
import com.rankmonkeysvc.utils.EventLogHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.rankmonkeysvc.messages.ErrorLogs.*;
import static com.rankmonkeysvc.messages.ErrorMessages.*;
import static com.rankmonkeysvc.messages.SuccessMessages.JOB_ROLE_CREATED_SUCCESSFULLY;
import static com.rankmonkeysvc.messages.SuccessMessages.JOB_ROLE_UPDATED_SUCCESSFULLY;

@Component
@Slf4j
public class JobRoleSvcImpl implements JobRoleSvc {
    private final UserInfoRepository userInfoRepository;
    private final JobRoleRepository jobRoleRepository;

    @Autowired
    public JobRoleSvcImpl(
            UserInfoRepository userInfoRepository,
            JobRoleRepository jobRoleRepository
    ) {
        this.userInfoRepository = userInfoRepository;
        this.jobRoleRepository = jobRoleRepository;
    }

    @Override
    public MessageResponse createJobRole(
            JobRoleRequest request, String userId
    ) {
        AtomicReference<String> email = new AtomicReference<>();
        validateUserPermission(userId, email);

        jobRoleRepository.findByRoleNameAndStatus(request.getRoleName(), ActivationStatus.ACTIVE).ifPresentOrElse(
                jobRole -> {
                    log.error(JOB_ROLE_ALREADY_EXISTS, request.getRoleName());
                    throw new DuplicateRecordException(JOB_ROLE_DUPLICATE_CREATION_NOT_ALLOWED);
                },
                () -> {
                    jobRoleRepository.findByRoleNameAndStatus(request.getRoleName(), ActivationStatus.DE_ACTIVATED).ifPresentOrElse(
                            jobRole -> {
                                jobRole.setStatus(ActivationStatus.ACTIVE)
                                        .setUpdatedBy(email.get());
                                jobRoleRepository.save(jobRole);
                            },
                            () -> {
                                JobRole jobRole = new JobRole()
                                        .setRoleName(request.getRoleName())
                                        .setStatus(ActivationStatus.ACTIVE)
                                        .setCreatedBy(email.get());

                                jobRoleRepository.save(jobRole);
                            }
                    );
                }
        );

        return new MessageResponse().setMessage(JOB_ROLE_CREATED_SUCCESSFULLY);
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

    @Override
    public MessageResponse updateJobRole(
            JobRoleRequest jobRoleRequest, String userId
    ) {
        AtomicReference<String> email = new AtomicReference<>();
        validateUserPermission(userId, email);

        jobRoleRepository.findById(jobRoleRequest.getId()).ifPresentOrElse(
                jobRole -> {
                    String previousStatus = jobRole.getStatus().toString();
                    String currentStatus = jobRoleRequest.getStatus().toString();

                    if (previousStatus.equals(currentStatus)) {
                        log.error(PREVIOUS_STATUS_AND_CURRENT_STATUS_ARE_SAME, jobRoleRequest.getStatus());
                        throw new InvalidRequestException(CURRENT_STATUS_AND_PREVIOUS_STATUS_IS_SAME);
                    }

                    jobRole.setStatus(jobRoleRequest.getStatus())
                            .setUpdatedBy(email.get());
                    jobRoleRepository.save(jobRole);
                },
                () -> {
                    log.error(JOB_ROLE_ID_IS_INVALID, jobRoleRequest.getId());
                    throw new InvalidRequestException(JOB_ROLE_ID_IS_INVALID_MESSAGE);
                }
        );
        return new MessageResponse().setMessage(JOB_ROLE_UPDATED_SUCCESSFULLY);
    }
}