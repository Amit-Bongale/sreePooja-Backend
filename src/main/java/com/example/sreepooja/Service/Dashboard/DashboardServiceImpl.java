package com.example.sreepooja.Service.Dashboard;

import com.example.sreepooja.DTO.Response.Dashboard.DashboardResponse;
import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Repository.Bookings.BookingRepository;
import com.example.sreepooja.Repository.PaymentRepository;
import com.example.sreepooja.Repository.Priests.PriestRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentsRepository;
    private final UsersRepository usersRepository;
    private final PriestRepository priestRepository;

    public final static class DashboardConstants {

        public static final Set<UserRoles> STAFF_ROLES =
                Set.of(
                        UserRoles.ADMIN,
                        UserRoles.OPERATIONS_MANAGER,
                        UserRoles.ACCOUNTS_MANAGER,
                        UserRoles.CONTENT_MANAGER,
                        UserRoles.CUSTOMER_SERVICE_EXECUTIVE
                );

        private DashboardConstants() {
        }
    }

    @Override
    public DashboardResponse getDashboard() {

        return DashboardResponse.builder()

                .totalBookings(
                        bookingRepository.countByBookingStatusNotIn(
                                List.of(
                                        BookingStatus.PENDING_PAYMENT,
                                        BookingStatus.CANCELLED
                                )
                        )
                )

                .totalRevenue(
                        paymentsRepository.calculateTotalRevenue(
                                PaymentStatus.PAID
                        )
                )

                .totalCustomers(
                        usersRepository.countUsersByRole(
                                UserRoles.USER
                        )
                )

                .totalStaff(
                        usersRepository.countStaff(
                                DashboardConstants.STAFF_ROLES
                        )
                )

                .totalPriests(
                        priestRepository.count()
                )

                .pendingBookings(
                        bookingRepository.countByBookingStatusIn(
                                List.of(
                                        BookingStatus.PAYMENT_RECEIVED,
                                        BookingStatus.CONFIRMED
                                )
                        )
                )

                .build();
    }
}