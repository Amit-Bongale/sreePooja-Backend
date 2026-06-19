package com.example.sreepooja.Repository.Users;


import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.UserRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByMobileNo(String mobile);

    @Query("""
        SELECT DISTINCT u FROM Users u
        LEFT JOIN FETCH u.roles
        WHERE u.id = :userId
    """)
    Optional<Users> findByIdWithRoles(@Param("userId") Long userId);

    @Query("""
        select DISTINCT u
        from Users u
        join u.roles r
        where r.role = :role
    """)
    List<Users> findUsersByRole(@Param("role") UserRoles role);


    @Query("""
        SELECT DISTINCT u 
        FROM Users u 
        JOIN u.roles r 
        WHERE r.role = :role
    """)
    Page<Users> findAllUsersByRole(
            @Param("role") UserRoles role,
            Pageable pageable
    );

    boolean existsByMobileNo(String mobile);

    @Query("""
       SELECT COUNT(u) > 0
       FROM Users u
       WHERE u.mobileNo = :mobile
          OR u.mobileNo = :whatsapp
       """)
    boolean existsDuplicateNumbers(
            @Param("mobile") String mobile,
            @Param("whatsapp") String whatsapp
    );

}
