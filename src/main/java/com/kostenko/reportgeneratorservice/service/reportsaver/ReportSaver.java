package com.kostenko.reportgeneratorservice.service.reportsaver;

import java.io.IOException;

public interface ReportSaver {
    void save(String report, String id) throws IOException;
}
