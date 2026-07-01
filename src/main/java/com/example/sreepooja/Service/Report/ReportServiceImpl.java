package com.example.sreepooja.Service.Report;

import com.example.sreepooja.DTO.Request.Report.ExportReportRequest;
import com.example.sreepooja.Enum.Report.ExportFormat;
import com.example.sreepooja.Enum.Report.ReportType;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.Report.ReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl
        implements ReportService {

    private final Map<ReportType, ReportGenerator> generators;

    @Override
    public byte[] exportReport(
            ExportReportRequest request
    ) {

        ReportGenerator generator =
                generators.get(request.getType());

        if (generator == null) {
            throw new BadRequestException(
                    "Unsupported report type."
            );
        }

        if (request.getFormat() == ExportFormat.PDF) {
            return generator.generatePdf(request);
        }

        return generator.generateExcel(request);
    }

}