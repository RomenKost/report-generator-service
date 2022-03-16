package com.kostenko.report.generator.service.report.saver.impl;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kostenko.report.generator.dto.report.ReportDto;
import com.kostenko.report.generator.exception.message.ErrorMessages;
import com.kostenko.report.generator.mapper.report.ReportMapper;
import com.kostenko.report.generator.model.report.Report;
import com.kostenko.report.generator.property.report.ReportSaverProperties;
import com.kostenko.report.generator.service.report.saver.ReportSaver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class FileReportSaver implements ReportSaver {
    private final ObjectWriter writer = new ObjectMapper().writer(new DefaultPrettyPrinter());

    private final ReportMapper reportMapper;
    private final ReportSaverProperties properties;

    @Override
    public void save(Report report) {
        log.info("Saving report about channel with id=" + report.getId() + "...");

        try {
            String reportFileName = generateFilename(report.getId(), new Date());
            File reportFile = createFileForReport(reportFileName);

            log.info("File for containing report with id=" + report.getId() + " was created.");

            ReportDto reportDto = reportMapper.reportToReportDto(report);
            writer.writeValue(reportFile, reportDto);

            log.info("Report about channel with id = " + report.getId() + " was saved to the file: " + reportFile.getAbsolutePath());
        } catch (IOException e) {
            log.error(ErrorMessages.REPORT_NOT_SAVED, e);
        }
    }

    private String generateFilename(String id, Date date) {
        return String.format(
                properties.getPatterns().getReport(),
                id,
                properties.getPatterns().getTime().format(date)
        );
    }

    private File createFileForReport(String reportFileName) throws IOException {
        File reportDir = new File(properties.getFolder());
        Path reportDirPath = reportDir.toPath();

        Path reportPath = reportDirPath.resolve(reportFileName);
        File reportFile = reportPath.toFile();

        if (!reportFile.createNewFile()) {
            throw new IOException("Impossible to create file, because it's already created.");
        }
        return reportFile;
    }
}
