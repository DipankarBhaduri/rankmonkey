package com.rankmonkeysvc.repositories;

import com.rankmonkeysvc.constants.Role;
import com.rankmonkeysvc.constants.UserStatus;
import com.rankmonkeysvc.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndEmailVerifiedTrue(String email);
    Optional<User> findByEmailAndPasswordIsNull(String email);
    Optional<User> findByIdAndStatusAndRole(UUID uuid, UserStatus userStatus, Role role);
}