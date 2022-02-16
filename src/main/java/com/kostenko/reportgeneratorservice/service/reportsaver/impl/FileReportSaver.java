package com.kostenko.reportgeneratorservice.service.reportsaver.impl;

import com.kostenko.reportgeneratorservice.model.Report;
import com.kostenko.reportgeneratorservice.mapper.reporttostringmapper.ReportToStringMapper;
import com.kostenko.reportgeneratorservice.service.reportsaver.ReportSaver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class FileReportSaver implements ReportSaver {
    private final DateFormat dateFormat;
    private final String reportPattern;

    private final String reportsDirName;
    private final ReportToStringMapper reportToStringMapper;

    public FileReportSaver(@Value("${application.patterns.time}") String dateFormat,
                           @Value("${application.patterns.report}") String reportPattern,
                           @Value("${application.reports-folder}") String reportsDirName,
                           ReportToStringMapper reportToStringMapper) {
        this.reportToStringMapper = reportToStringMapper;
        this.dateFormat = new SimpleDateFormat(dateFormat);
        this.reportPattern = reportPattern;
        this.reportsDirName = reportsDirName;
    }

    @Override
    public void save(Report report) throws IOException {
        log.info("Saving report about channel with id=" + report.getId() + "...");

        String reportFileName = generateFilename(report.getId(), new Date());
        File reportFile = createFileForReport(reportFileName);
        log.info("File for containing report with id=" + report.getId() + "was created.");

        String reportString = reportToStringMapper.reportToString(report);
        writeReport(reportFile, reportString);
        log.info("Report about channel with id=" + report.getId() + " was saved to the file: " + reportFile.getAbsolutePath());
    }

    private String generateFilename(String id, Date date) {
        return String.format(reportPattern, id, dateFormat.format(date));
    }

    private File createFileForReport(String reportFileName) throws IOException {
        File reportDir = new File(reportsDirName);
        Path reportDirPath = reportDir.toPath();

        Path reportPath = reportDirPath.resolve(reportFileName);
        File reportFile = reportPath.toFile();

        if (!reportFile.createNewFile()) {
            throw new IOException("Impossible to create file, because it's already created.");
        }
        return reportFile;
    }

    private void writeReport(File file, String report) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(report);
        }
    }
}
