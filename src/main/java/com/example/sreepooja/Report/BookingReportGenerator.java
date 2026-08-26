package com.example.sreepooja.Report;

import com.example.sreepooja.DTO.Request.Report.ExportReportRequest;
import com.example.sreepooja.Entity.Bookings.Booking;
import com.example.sreepooja.Entity.Payments;
import com.example.sreepooja.Enum.Report.ReportType;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.Repository.Bookings.BookingRepository;
import com.example.sreepooja.Repository.PaymentRepository;
import com.example.sreepooja.Specification.BookingSpecification;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookingReportGenerator
        implements ReportGenerator {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public ReportType getSupportedReport() {
        return ReportType.BOOKINGS;
    }

    @Override
    public byte[] generatePdf(
            ExportReportRequest request
    ) {

        Specification<Booking> specification =
                BookingSpecification.filterBookings(request);

        List<Booking> bookings =
                bookingRepository.findAll(specification);

        /*
         * Fetch all payments in one query.
         * Avoids N+1 queries while generating the report.
         */
        List<Long> bookingIds = bookings.stream()
                .map(Booking::getId)
                .toList();

        Map<Long, List<Payments>> paymentsByBookingId =
                bookingIds.isEmpty()
                        ? Map.of()
                        : paymentRepository
                        .findByBookingIdInOrderByBookingIdAscCreatedAtAsc(
                                bookingIds
                        )
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        payment ->
                                                payment.getBooking().getId()
                                )
                        );

        try (
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            Document document =
                    new Document(PageSize.A4.rotate());

            PdfWriter.getInstance(
                    document,
                    outputStream
            );

            document.open();

            // -------------------------------------------------
            // Title
            // -------------------------------------------------

            Font titleFont = new Font(
                    Font.HELVETICA,
                    18,
                    Font.BOLD
            );

            Paragraph title =
                    new Paragraph(
                            "SreePooja",
                            titleFont
                    );

            title.setAlignment(
                    Element.ALIGN_CENTER
            );

            document.add(title);

            // -------------------------------------------------
            // Heading
            // -------------------------------------------------

            Font headingFont = new Font(
                    Font.HELVETICA,
                    14,
                    Font.BOLD
            );

            Paragraph heading =
                    new Paragraph(
                            "Booking Report",
                            headingFont
                    );

            heading.setAlignment(
                    Element.ALIGN_CENTER
            );

            heading.setSpacingAfter(20);

            document.add(heading);

            // -------------------------------------------------
            // Generated On
            // -------------------------------------------------

            Paragraph generatedOn =
                    new Paragraph(
                            "Generated On : "
                                    + LocalDate.now()
                    );

            generatedOn.setSpacingAfter(20);

            document.add(generatedOn);

            // -------------------------------------------------
            // Table
            // -------------------------------------------------

            PdfPTable table = new PdfPTable(13);

            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            table.addCell(createHeaderCell("Booking No"));
            table.addCell(createHeaderCell("Customer"));
            table.addCell(createHeaderCell("Mobile"));
            table.addCell(createHeaderCell("Pooja"));
            table.addCell(createHeaderCell("Pooja Date"));
            table.addCell(createHeaderCell("Priest"));
            table.addCell(createHeaderCell("Booking Status"));
            table.addCell(createHeaderCell("Payment Status"));
            table.addCell(createHeaderCell("Total Amount"));
            table.addCell(createHeaderCell("Amount Paid"));
            table.addCell(createHeaderCell("Balance Amount"));
            table.addCell(createHeaderCell("Transaction ID 1"));
            table.addCell(createHeaderCell("Transaction ID 2"));

            // -------------------------------------------------
            // Rows
            // -------------------------------------------------

            for (Booking booking : bookings) {

                // Customer

                String customerName =
                        booking.getUser().getFirstName()
                                + " "
                                + (
                                booking.getUser().getLastName() == null
                                        ? ""
                                        : booking.getUser().getLastName()
                        );

                // Priest

                String priestName = "-";

                if (booking.getPriest() != null) {

                    priestName =
                            booking.getPriest().getUser().getFirstName()
                                    + " "
                                    + (
                                    booking.getPriest()
                                            .getUser()
                                            .getLastName() == null
                                            ? ""
                                            : booking.getPriest()
                                            .getUser()
                                            .getLastName()
                            );

                    priestName = priestName.trim();
                }

                // Pooja Date

                String poojaDate =
                        booking.getConfirmedDate() != null
                                ? booking.getConfirmedDate()
                                + " (confirmed)"
                                : booking.getPreferredDate()
                                + " (preferred)";

                // Payments

                List<Payments> payments =
                        paymentsByBookingId.getOrDefault(
                                booking.getId(),
                                List.of()
                        );

                String transactionId1 = "-";
                String transactionId2 = "-";

                if (!payments.isEmpty()) {

                    transactionId1 =
                            payments.get(0)
                                    .getRazorpayPaymentId();

                    if (transactionId1 == null
                            || transactionId1.isBlank()) {

                        transactionId1 = "-";
                    }
                }

                if (payments.size() > 1) {

                    transactionId2 =
                            payments.get(1)
                                    .getRazorpayPaymentId();

                    if (transactionId2 == null
                            || transactionId2.isBlank()) {

                        transactionId2 = "-";
                    }
                }

                // -------------------------------------------------
                // Add cells
                // -------------------------------------------------

                table.addCell(
                        createContentCell(
                                booking.getBookingNumber()
                        )
                );

                table.addCell(
                        createContentCell(
                                customerName.trim()
                        )
                );

                table.addCell(
                        createContentCell(
                                booking.getUser().getMobileNo()
                        )
                );

                table.addCell(
                        createContentCell(
                                booking.getService().getServiceName()
                        )
                );

                table.addCell(
                        createContentCell(
                                poojaDate
                        )
                );

                table.addCell(
                        createContentCell(
                                priestName
                        )
                );

                table.addCell(
                        createContentCell(
                                booking.getBookingStatus().name()
                        )
                );

                table.addCell(
                        createContentCell(
                                booking.getPaymentStatus().name()
                        )
                );

                table.addCell(
                        createContentCell(
                                booking.getTotalAmount().toString()
                        )
                );

                table.addCell(
                        createContentCell(
                                booking.getAdvanceAmount().toString()
                        )
                );

                table.addCell(
                        createContentCell(
                                booking.getBalanceAmount().toString()
                        )
                );

                table.addCell(
                        createContentCell(
                                transactionId1
                        )
                );

                table.addCell(
                        createContentCell(
                                transactionId2
                        )
                );
            }

            document.add(table);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new BadRequestException(
                    "Unable to generate PDF report."
            );
        }
    }

    @Override
    public byte[] generateExcel(
            ExportReportRequest request
    ) {

        Specification<Booking> specification =
                BookingSpecification.filterBookings(request);

        List<Booking> bookings =
                bookingRepository.findAll(specification);

        List<Long> bookingIds = bookings.stream()
                .map(Booking::getId)
                .toList();

        Map<Long, List<Payments>> paymentsByBookingId =
                bookingIds.isEmpty()
                        ? Map.of()
                        : paymentRepository
                        .findByBookingIdInOrderByBookingIdAscCreatedAtAsc(
                                bookingIds
                        )
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        payment ->
                                                payment.getBooking().getId()
                                )
                        );


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
            header.createCell(5).setCellValue("Pooja Date");
            header.createCell(6).setCellValue("Booking Status");
            header.createCell(7).setCellValue("Payment Status");
            header.createCell(8).setCellValue("Total Amount");
            header.createCell(9).setCellValue("Advance Amount");
            header.createCell(10).setCellValue("Balance Amount");
            header.createCell(11).setCellValue("Transaction ID 1");
            header.createCell(12).setCellValue("Transaction ID 2");
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
                                booking.getConfirmedDate() != null
                                ? booking.getConfirmedDate() + " (confirmed)"
                                        : booking.getPreferredDate() +" (preferred)"
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

                row.createCell(9)
                        .setCellValue(
                                booking.getAdvanceAmount().doubleValue()
                        );

                row.createCell(10)
                        .setCellValue(
                                booking.getBalanceAmount().doubleValue()
                        );

                List<Payments> payments =
                        paymentsByBookingId.getOrDefault(
                                booking.getId(),
                                List.of()
                        );

                String transactionId1 = "-";
                String transactionId2 = "-";

                if (!payments.isEmpty()) {

                    transactionId1 =
                            payments.get(0).getRazorpayPaymentId();

                    if (transactionId1 == null
                            || transactionId1.isBlank()) {

                        transactionId1 = "-";
                    }
                }

                if (payments.size() > 1) {

                    transactionId2 =
                            payments.get(1).getRazorpayPaymentId();

                    if (transactionId2 == null
                            || transactionId2.isBlank()) {

                        transactionId2 = "-";
                    }
                }

                row.createCell(11)
                        .setCellValue(transactionId1);

                row.createCell(12)
                        .setCellValue(transactionId2);
            }

            for (int i = 0; i < 13; i++) {
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

    private PdfPCell createHeaderCell(String value) {

        Font font = new Font(
                Font.HELVETICA,
                8,
                Font.BOLD
        );

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(value, font)
                );

        cell.setPadding(4);

        return cell;
    }

    private PdfPCell createContentCell(String value) {

        Font font = new Font(
                Font.HELVETICA,
                7,
                Font.NORMAL
        );

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                value != null ? value : "-",
                                font
                        )
                );

        cell.setPadding(3);

        return cell;
    }

}