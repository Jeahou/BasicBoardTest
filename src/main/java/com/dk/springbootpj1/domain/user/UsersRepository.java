package com.dk.springbootpj1.domain.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    // 1. 소셜 로그인 시 기존 등록된 사용자인지 확인하기 위한 메서드
    // (Optional을 사용하여 null 체크를 안전하게 처리합니다)
    Optional<Users> findByEmail(String email);
}
