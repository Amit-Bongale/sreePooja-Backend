package com.example.sreepooja.Specification;

import com.example.sreepooja.Entity.Priests.Priest;
import com.example.sreepooja.Enum.Priests.PriestExperience;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PriestSpecification {

    public static Specification<Priest>
    filterPriests(

            Boolean active,

            String name,

            String mobileNumber,

            Long communityId,

            PriestExperience experience,

            String languageName,

            String cityName
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            if (active != null) {

                predicates.add(
                        cb.equal(
                                root.get("active"),
                                active
                        )
                );
            }

            if (name != null &&
                    !name.isBlank()) {

                String search =
                        "%" +
                                name.toLowerCase()
                                + "%";

                predicates.add(

                        cb.or(

                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "firstName"
                                                )
                                        ),
                                        search
                                ),

                                cb.like(
                                        cb.lower(
                                                root.get(
                                                        "lastName"
                                                )
                                        ),
                                        search
                                ),

                                cb.like(
                                        cb.lower(
                                                cb.concat(

                                                        cb.concat(
                                                                root.get(
                                                                        "firstName"
                                                                ),
                                                                " "
                                                        ),

                                                        root.get(
                                                                "lastName"
                                                        )
                                                )
                                        ),
                                        search
                                )
                        )
                );
            }

            if (mobileNumber != null &&
                    !mobileNumber.isBlank()) {

                predicates.add(
                        cb.like(
                                root.get(
                                        "mobileNumber"
                                ),
                                "%" +
                                        mobileNumber +
                                        "%"
                        )
                );
            }

            if (communityId != null) {

                predicates.add(
                        cb.equal(
                                root.get("community")
                                        .get("id"),
                                communityId
                        )
                );
            }

            if (experience != null) {

                predicates.add(
                        cb.equal(
                                root.get(
                                        "experience"
                                ),
                                experience
                        )
                );
            }

            if (languageName != null) {

                predicates.add(
                        cb.like(
                                cb.lower(
                                        root.get(
                                                "languagesSpoken"
                                        )
                                ),
                                "%" +
                                        languageName.toLowerCase()
                                        + "%"
                        )
                );
            }

            if (cityName != null) {

                predicates.add(
                        cb.like(
                                cb.lower(
                                        root.get(
                                                "city"
                                        )
                                ),
                                "%" +
                                cityName.toLowerCase()
                                + "%"
                        )
                );
            }

            return cb.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }
}
