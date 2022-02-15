package com.kostenko.reportgeneratorservice.service.reportsaver.impl;

import com.kostenko.reportgeneratorservice.service.reportsaver.ReportSaver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FileReportSaver implements ReportSaver {
    private final DateFormat dateFormat;
    private final String reportPattern;

    private final String reportsDirName;

    public FileReportSaver(@Value("${application.patterns.time}") String dateFormat,
                           @Value("${application.patterns.report}") String reportPattern,
                           @Value("${application.reports-folder}") String reportsDirName) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
        this.reportPattern = reportPattern;
        this.reportsDirName = reportsDirName;
    }

    @Override
    public void save(String report, String id) throws IOException {
        File reportFile = generateFile(id, new Date());
        writeReport(reportFile, report);
    }

    private File generateFile(String id, Date date) throws IOException {
        File reportDir = new File(reportsDirName);
        Path reportDirPath = reportDir.toPath();

        String reportFileName = String.format(reportPattern, id, dateFormat.format(date));
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
