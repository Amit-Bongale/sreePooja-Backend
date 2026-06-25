package com.example.sreepooja.Specification;

import com.example.sreepooja.Entity.UserRole;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class StaffSpecification {

    public static Specification<Users> filterStaff(
            String search,
            UserStatus status,
            UserRoles role
    ) {

        return (root, query, cb) -> {

            query.distinct(true);

            Join<Users, UserRole> roleJoin = root.join("roles");

            var predicate = cb.conjunction();

            // Exclude CUSTOMER
            predicate = cb.and(
                    predicate,
                    cb.notEqual(roleJoin.get("role"), UserRoles.USER)
            );

            // Exclude PRIEST
            predicate = cb.and(
                    predicate,
                    cb.notEqual(roleJoin.get("role"), UserRoles.PRIEST)
            );

            // Exclude SUPER ADMIN
            predicate = cb.and(
                    predicate,
                    cb.notEqual(roleJoin.get("role"), UserRoles.SUPER_ADMIN)
            );

            if (search != null && !search.isBlank()) {

                predicate = cb.and(
                        predicate,
                        cb.or(

                                cb.like(
                                        cb.lower(root.get("firstName")),
                                        "%" + search.toLowerCase() + "%"
                                ),

                                cb.like(
                                        cb.lower(root.get("lastName")),
                                        "%" + search.toLowerCase() + "%"
                                ),

                                cb.like(
                                        root.get("mobileNo"),
                                        "%" + search + "%"
                                )
                        )
                );
            }

            if (status != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("status"), status)
                );
            }

            if (role != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(roleJoin.get("role"), role)
                );
            }

            return predicate;
        };
    }

}