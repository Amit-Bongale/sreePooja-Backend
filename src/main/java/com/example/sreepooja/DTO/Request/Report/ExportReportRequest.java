package com.example.sreepooja.DTO.Request.Report;

import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.Enum.Report.ExportFormat;
import com.example.sreepooja.Enum.Report.ReportType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExportReportRequest {

    private ReportType type;

    private ExportFormat format;

    private Long bookingId;

    private String mobileNumber;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private LocalDate fromDate;

    private LocalDate toDate;
}