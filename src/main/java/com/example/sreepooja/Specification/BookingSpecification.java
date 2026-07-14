package com.example.sreepooja.Specification;

import com.example.sreepooja.DTO.Request.Report.ExportReportRequest;
import com.example.sreepooja.Entity.Bookings.Booking;
import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingSpecification {

    public static Specification<Booking> filterBookings(

            Long bookingId,

            String mobileNumber,

            BookingStatus bookingStatus,

            PaymentStatus paymentStatus,

            LocalDate fromDate,

            LocalDate toDate
    ) {

        System.out.println("BookingId = " + bookingId);
        System.out.println("Mobile = " + mobileNumber);
        System.out.println("PaymentStatus = " + paymentStatus);

        return (root, query, cb) -> {

            if (!Long.class.equals(query.getResultType())
                    && !long.class.equals(query.getResultType())) {

                query.orderBy(
                        cb.asc(
                                cb.coalesce(
                                        root.get("confirmedDate"),
                                        root.get("preferredDate")
                                )
                        )
                );
            }

            List<Predicate> predicates =
                    new ArrayList<>();

            if (bookingId != null) {

                predicates.add(
                        cb.equal(
                                root.get("id"),
                                bookingId
                        )
                );
            }

            if (mobileNumber != null &&
                    !mobileNumber.isBlank()) {

                predicates.add(
                        cb.like(
                                root.join("user")
                                        .get("mobileNo"),
                                "%" + mobileNumber + "%"
                        )
                );
            }

            if (bookingStatus != null) {

                predicates.add(
                        cb.equal(
                                root.get("bookingStatus"),
                                bookingStatus
                        )
                );
            }

            if (paymentStatus != null) {

                predicates.add(
                        cb.equal(
                                root.get("paymentStatus"),
                                paymentStatus
                        )
                );
            }

            if (fromDate != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("preferredDate"),
                                fromDate
                        )
                );
            }

            if (toDate != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("preferredDate"),
                                toDate
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

    public static Specification<Booking> filterBookings(
            ExportReportRequest request
    ) {

        return filterBookings(
                request.getBookingId(),
                request.getMobileNumber(),
                request.getBookingStatus(),
                request.getPaymentStatus(),
                request.getFromDate(),
                request.getToDate()
        );
    }
}