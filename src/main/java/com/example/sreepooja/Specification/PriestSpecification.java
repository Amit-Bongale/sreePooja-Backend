package com.example.sreepooja.Specification;

import com.example.sreepooja.Entity.Priests.Priest;
import com.example.sreepooja.Enum.Priests.PriestExperience;
import com.example.sreepooja.Enum.Priests.Trimathastharu;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PriestSpecification {

    public static Specification<Priest>
    filterPriests(

            Boolean active,

            String mobileNumber,

            Trimathastharu trimathastharu,

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

            if (trimathastharu != null) {

                predicates.add(
                        cb.equal(
                                root.get(
                                        "trimathastharu"
                                ),
                                trimathastharu
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
