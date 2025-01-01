package com.rankmonkeysvc.repositories;

import com.rankmonkeysvc.constants.ActivationStatus;
import com.rankmonkeysvc.dao.JobRole;
import com.rankmonkeysvc.dao.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findBySkillNameAndStatus(
            @NotEmpty String skillName,
            ActivationStatus activationStatus
    );
}