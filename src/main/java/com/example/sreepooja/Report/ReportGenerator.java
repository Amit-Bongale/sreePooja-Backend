package com.example.sreepooja.Report;

import com.example.sreepooja.DTO.Request.Report.ExportReportRequest;
import com.example.sreepooja.Enum.Report.ReportType;

public interface ReportGenerator {

    ReportType getSupportedReport();

    byte[] generatePdf(ExportReportRequest request);

    byte[] generateExcel(ExportReportRequest request);

}