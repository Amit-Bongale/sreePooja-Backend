package com.example.sreepooja.Report;

import com.example.sreepooja.DTO.Request.Report.ExportReportRequest;
import com.example.sreepooja.Entity.Bookings.Booking;
import com.example.sreepooja.Enum.Report.ReportType;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.Repository.Bookings.BookingRepository;
import com.example.sreepooja.Specification.BookingSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BookingReportGenerator
        implements ReportGenerator {

    private final BookingRepository bookingRepository;

    @Override
    public ReportType getSupportedReport() {
        return ReportType.BOOKINGS;
    }

    @Override
    public byte[] generatePdf(
            ExportReportRequest request
    ) {

        return new byte[0];
    }

    @Override
    public byte[] generateExcel(
            ExportReportRequest request
    ) {

        Specification<Booking> specification =
                BookingSpecification.filterBookings(request);

        List<Booking> bookings =
                bookingRepository.findAll(specification);

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            Sheet sheet =
                    workbook.createSheet("Bookings");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Booking Number");
            header.createCell(1).setCellValue("Customer");
            header.createCell(2).setCellValue("Mobile Number");
            header.createCell(3).setCellValue("Priest");
            header.createCell(4).setCellValue("Pooja");
            header.createCell(5).setCellValue("Preferred Date");
            header.createCell(6).setCellValue("Booking Status");
            header.createCell(7).setCellValue("Payment Status");
            header.createCell(8).setCellValue("Total Amount");
            int rowNumber = 1;

            for (Booking booking : bookings) {

                Row row = sheet.createRow(rowNumber++);

                row.createCell(0)
                        .setCellValue(booking.getBookingNumber());

                row.createCell(1)
                        .setCellValue(
                                booking.getUser().getFirstName() + " " +
                                        (booking.getUser().getLastName() == null ? "" : booking.getUser().getLastName())
                        );

                row.createCell(2)
                        .setCellValue(
                                booking.getUser().getMobileNo()
                        );

                row.createCell(3)
                        .setCellValue(
                                booking.getPriest() != null
                                        ? booking.getPriest().getUser().getFirstName() + " " +
                                        booking.getPriest().getUser().getLastName()
                                        : "-"
                        );

                row.createCell(4)
                        .setCellValue(
                                booking.getService().getServiceName()
                        );

                row.createCell(5)
                        .setCellValue(
                                booking.getPreferredDate().toString()
                        );

                row.createCell(6)
                        .setCellValue(
                                booking.getBookingStatus().name()
                        );

                row.createCell(7)
                        .setCellValue(
                                booking.getPaymentStatus().name()
                        );

                row.createCell(8)
                        .setCellValue(
                                booking.getTotalAmount().doubleValue()
                        );
            }

            for (int i = 0; i < 10; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {

            throw new BadRequestException(
                    "Unable to generate excel report."
            );
        }

    }

}