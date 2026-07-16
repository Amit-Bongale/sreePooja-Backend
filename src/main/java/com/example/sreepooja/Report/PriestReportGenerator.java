package com.example.sreepooja.Report;

import com.example.sreepooja.DTO.Request.Report.ExportReportRequest;
import com.example.sreepooja.Entity.Priests.Priest;
import com.example.sreepooja.Enum.Report.ReportType;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.Repository.Priests.PriestRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class PriestReportGenerator
        implements ReportGenerator {

    private final PriestRepository priestRepository;

    @Override
    public ReportType getSupportedReport() {
        return ReportType.PRIESTS;
    }

    @Override
    public byte[] generatePdf(
            ExportReportRequest request
    ) {

        List<Priest> priests =
                priestRepository.findAll();

        try (
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            Document document =
                    new Document(PageSize.A2.rotate());

            PdfWriter.getInstance(
                    document,
                    outputStream
            );

            document.open();

            Font companyFont =
                    new Font(
                            Font.HELVETICA,
                            18,
                            Font.BOLD
                    );

            Paragraph company =
                    new Paragraph(
                            "SreePooja",
                            companyFont
                    );

            company.setAlignment(Element.ALIGN_CENTER);

            document.add(company);

            Paragraph generatedOn =
                    new Paragraph(
                            "Generated On : "
                                    + LocalDate.now()
                    );

            generatedOn.setSpacingAfter(15);

            document.add(generatedOn);

            PdfPTable table =
                    new PdfPTable(27);

            table.setWidthPercentage(100);

            table.setSpacingBefore(10);


            table.addCell("Priest");
            table.addCell("Mobile");
            table.addCell("Email");
            table.addCell("WhatsApp");
            table.addCell("Gothra");
            table.addCell("Pravara");
            table.addCell("Native");
            table.addCell("Aadhaar");
            table.addCell("Address 1");
            table.addCell("Address 2");
            table.addCell("State");
            table.addCell("City");
            table.addCell("Pincode");
            table.addCell("Languages");
            table.addCell("Community");
            table.addCell("Experience");
            table.addCell("Referred");
            table.addCell("Banking Name");
            table.addCell("Bank");
            table.addCell("Branch");
            table.addCell("IFSC");
            table.addCell("Account");
            table.addCell("UPI");
            table.addCell("Photo");
            table.addCell("Aadhaar PDF");
            table.addCell("Status");
            table.addCell("Created");

            for (Priest priest : priests) {

                table.addCell(
                        priest.getUser().getFirstName()
                                + " "
                                + priest.getUser().getLastName()
                );

                table.addCell(priest.getUser().getMobileNo());

                table.addCell(priest.getUser().getEmail());

                table.addCell(priest.getWhatsappNumber());

                table.addCell(priest.getGothra());

                table.addCell(
                        priest.getPravara() == null
                                ? ""
                                : priest.getPravara()
                );

                table.addCell(priest.getNativePlace());

                table.addCell(priest.getAadhaarNumber());

                table.addCell(priest.getAddressLine1());

                table.addCell(
                        priest.getAddressLine2() == null
                                ? ""
                                : priest.getAddressLine2()
                );

                table.addCell(
                        priest.getState().getStateName()
                );

                table.addCell(
                        priest.getCity().getCityName()
                );

                table.addCell(
                        priest.getPincode().getPincode()
                );

                String languages =
                        priest.getLanguages()
                                .stream()
                                .map(language ->
                                        language.getLanguage().getLanguageName())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("");

                table.addCell(languages);

                table.addCell(
                        priest.getCommunity().getCommunityName()
                );

                table.addCell(
                        priest.getExperience().name()
                );

                table.addCell(
                        priest.getReferredBy() == null
                                ? ""
                                : priest.getReferredBy()
                );

                table.addCell(priest.getBankingName());

                table.addCell(priest.getBankName());

                table.addCell(priest.getBankBranchName());

                table.addCell(priest.getBankIfscCode());

                table.addCell(priest.getBankAccountNumber());

                table.addCell(priest.getUpiId());

                table.addCell(priest.getPriestPhotoUrl());

                table.addCell(priest.getAadhaarPdfUrl());

                table.addCell(
                        priest.getActive()
                                ? "ACTIVE"
                                : "INACTIVE"
                );

                table.addCell(
                        priest.getCreatedAt().toString()
                );
            }

            document.add(table);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new BadRequestException(
                    "Unable to generate priest PDF report."
            );
        }
    }

    @Override
    public byte[] generateExcel(
            ExportReportRequest request
    ) {
        List<Priest> priests =
                priestRepository.findAll();

        try (
                Workbook workbook =
                        new XSSFWorkbook();

                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            Sheet sheet =
                    workbook.createSheet("Priests");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Priest Name");
            header.createCell(1).setCellValue("Mobile Number");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("WhatsApp");
            header.createCell(4).setCellValue("Gothra");
            header.createCell(5).setCellValue("Pravara");
            header.createCell(6).setCellValue("Native Place");
            header.createCell(7).setCellValue("Aadhaar");
            header.createCell(8).setCellValue("Address Line 1");
            header.createCell(9).setCellValue("Address Line 2");
            header.createCell(10).setCellValue("State");
            header.createCell(11).setCellValue("City");
            header.createCell(12).setCellValue("Pincode");
            header.createCell(13).setCellValue("Languages");
            header.createCell(14).setCellValue("Community");
            header.createCell(15).setCellValue("Experience");
            header.createCell(16).setCellValue("Referred By");
            header.createCell(17).setCellValue("Banking Name");
            header.createCell(18).setCellValue("Bank Name");
            header.createCell(19).setCellValue("Branch");
            header.createCell(20).setCellValue("IFSC");
            header.createCell(21).setCellValue("Account Number");
            header.createCell(22).setCellValue("UPI ID");
            header.createCell(23).setCellValue("Photo URL");
            header.createCell(24).setCellValue("Aadhaar PDF");
            header.createCell(25).setCellValue("Status");
            header.createCell(26).setCellValue("Created On");
            int rowNumber = 1;

            for (Priest priest : priests) {

                Row row =
                        sheet.createRow(rowNumber++);

                row.createCell(0).setCellValue(
                        priest.getUser().getFirstName()
                                + " "
                                + priest.getUser().getLastName()
                );

                row.createCell(1).setCellValue(
                        priest.getUser().getMobileNo()
                );

                row.createCell(2).setCellValue(
                        priest.getUser().getEmail()
                );

                row.createCell(3).setCellValue(
                        priest.getWhatsappNumber()
                );

                row.createCell(4).setCellValue(
                        priest.getGothra()
                );

                row.createCell(5).setCellValue(
                        priest.getPravara() == null
                                ? ""
                                : priest.getPravara()
                );

                row.createCell(6).setCellValue(
                        priest.getNativePlace()
                );

                row.createCell(7).setCellValue(
                        priest.getAadhaarNumber()
                );

                row.createCell(8).setCellValue(
                        priest.getAddressLine1()
                );

                row.createCell(9).setCellValue(
                        priest.getAddressLine2() == null
                                ? ""
                                : priest.getAddressLine2()
                );

                row.createCell(10).setCellValue(
                        priest.getState().getStateName()
                );

                row.createCell(11).setCellValue(
                        priest.getCity().getCityName()
                );

                row.createCell(12).setCellValue(
                        priest.getPincode().getPincode()
                );

                String languages =
                        priest.getLanguages()
                                .stream()
                                .map(mapping ->
                                        mapping.getLanguage().getLanguageName())
                                .reduce(
                                        (a, b) -> a + ", " + b
                                )
                                .orElse("");

                row.createCell(13)
                        .setCellValue(languages);

                row.createCell(14).setCellValue(
                        priest.getCommunity().getCommunityName()
                );

                row.createCell(15).setCellValue(
                        priest.getExperience().name()
                );

                row.createCell(16).setCellValue(
                        priest.getReferredBy() == null
                                ? ""
                                : priest.getReferredBy()
                );

                row.createCell(17).setCellValue(
                        priest.getBankingName()
                );

                row.createCell(18).setCellValue(
                        priest.getBankName()
                );

                row.createCell(19).setCellValue(
                        priest.getBankBranchName()
                );

                row.createCell(20).setCellValue(
                        priest.getBankIfscCode()
                );

                row.createCell(21).setCellValue(
                        priest.getBankAccountNumber()
                );

                row.createCell(22).setCellValue(
                        priest.getUpiId()
                );

                row.createCell(23).setCellValue(
                        priest.getPriestPhotoUrl()
                );

                row.createCell(24).setCellValue(
                        priest.getAadhaarPdfUrl()
                );

                row.createCell(25).setCellValue(
                        priest.getActive()
                                ? "ACTIVE"
                                : "INACTIVE"
                );

                row.createCell(26).setCellValue(
                        priest.getCreatedAt().toString()
                );
            }

            for (int i = 0; i <= 26; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {

            throw new BadRequestException(
                    "Unable to generate priest excel report."
            );
        }
    }
}