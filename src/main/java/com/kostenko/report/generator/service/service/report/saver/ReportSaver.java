package com.kostenko.report.generator.service.service.report.saver;

import com.kostenko.report.generator.service.model.Report;

import java.io.IOException;

public interface ReportSaver {
    void save(Report report) throws IOException;
}
