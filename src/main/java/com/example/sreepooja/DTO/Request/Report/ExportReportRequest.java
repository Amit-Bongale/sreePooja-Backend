package com.example.sreepooja.DTO.Request.Report;

import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentOption;
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

    private LocalDate fromDate;

    private LocalDate toDate;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private PaymentOption paymentOption;

}