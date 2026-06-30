package com.example.sreepooja.DTO.Response.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class DashboardResponse {

    private Long totalBookings;

    private BigDecimal totalRevenue;

    private Long totalCustomers;

    private Long totalStaff;

    private Long totalPriests;

    private Long pendingBookings;

}
