package com.kostenko.reportgeneratorservice.service.reportsaver;

import com.kostenko.reportgeneratorservice.model.Report;

import java.io.IOException;

public interface ReportSaver {
    void save(Report report) throws IOException;
}
