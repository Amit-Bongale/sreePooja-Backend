package com.example.sreepooja.Report;

import com.example.sreepooja.DTO.Request.Report.ExportReportRequest;
import com.example.sreepooja.Enum.Report.ReportType;
import org.springframework.stereotype.Component;

@Component
public class StaffReportGenerator
        implements ReportGenerator {

    @Override
    public ReportType getSupportedReport() {
        return ReportType.STAFF;
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
        return new byte[0];
    }

}