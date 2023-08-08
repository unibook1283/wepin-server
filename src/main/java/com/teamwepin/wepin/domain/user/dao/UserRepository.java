package com.teamwepin.wepin.domain.user.dao;

import com.teamwepin.wepin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
