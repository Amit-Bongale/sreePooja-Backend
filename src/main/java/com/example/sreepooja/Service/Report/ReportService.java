package com.example.sreepooja.Service.Report;

import com.example.sreepooja.DTO.Request.Report.ExportReportRequest;

public interface ReportService {

    byte[] exportReport(ExportReportRequest request);

}