package com.kostenko.report.generator.service.service.report.saver.impl;

import com.kostenko.report.generator.service.mapper.report.tostring.mapper.ReportToStringMapper;
import com.kostenko.report.generator.service.model.Models;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileReportSaverTest {
    private File dir;

    private FileReportSaver fileReportSaver;
    @MockBean
    private ReportToStringMapper reportToStringMapper;

    @BeforeEach
    public void initialize() throws IOException {
        dir = Files.createTempDirectory(null).toFile();
        fileReportSaver = new FileReportSaver("", "report_test_%s%s.rt", dir.getAbsolutePath(), reportToStringMapper);
    }

    @AfterEach
    public void clear() throws IOException {
        FileUtils.deleteDirectory(dir);
    }

    @Test
    void saveTest() throws IOException {
        String anyId = "any_id", anyReport = "any_report";
        Mockito.when(reportToStringMapper.reportToString(Models.getReport(anyId)))
                .thenReturn(anyReport);
        fileReportSaver.save(Models.getReport(anyId));

        assertEquals(1, Files.list(dir.toPath()).count());
        Optional<Path> optionalFilePath = Files.list(dir.toPath()).findFirst();
        assertTrue(optionalFilePath.isPresent());

        Path filePath = optionalFilePath.get();
        assertEquals("report_test_any_id.rt", filePath.getFileName().toString());
        assertEquals("any_report", Files.readString(filePath));
    }

    @Test
    void saveAlreadySavedReportTest() throws IOException {
        File report = dir.toPath().resolve("report_test_any_id.rt").toFile();
        report.createNewFile();

        String expected = "Impossible to create file, because it's already created.";
        String actual = assertThrows(
                IOException.class,
                () -> fileReportSaver.save(Models.getReport("any_id"))
        ).getMessage();

        assertEquals(expected, actual);
    }
}
