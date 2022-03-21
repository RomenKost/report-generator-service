package com.kostenko.report.generator.service.report.saver.impl;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.kostenko.report.generator.dto.DTOs;
import com.kostenko.report.generator.dto.report.ReportDto;
import com.kostenko.report.generator.mapper.report.ReportMapper;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.model.report.Report;
import com.kostenko.report.generator.property.report.ReportSaverProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = FileReportSaver.class)
class FileReportSaverTest {
    @MockBean
    private ObjectWriter writer;
    @MockBean
    private ReportMapper reportMapper;
    @MockBean
    private ReportSaverProperties properties;

    @Mock
    private ReportSaverProperties.Patterns patterns;

    private FileReportSaver fileReportSaver;

    @BeforeEach
    void initialize() {
        fileReportSaver = spy(new FileReportSaver(
                writer,
                reportMapper,
                properties
        ));
    }

    @Test
    void saveTest() throws IOException {
        Report report = Models.getReport();
        ReportDto reportDto = DTOs.getReport();
        File reportFile = new File("report file name");

        doReturn("report file name")
                .when(fileReportSaver)
                .generateFilename(eq("any id"), any());
        doReturn(reportFile)
                .when(fileReportSaver)
                .createFileForReport("report file name");
        when(reportMapper.reportToReportDto(report))
                .thenReturn(reportDto);

        fileReportSaver.save(report);

        verify(writer, times(1))
                .writeValue(reportFile, reportDto);
    }

    @Test
    void saveWhenThrowsIOExceptionThenDoNothingTest() throws IOException {
        Report report = Models.getReport();

        doReturn("report file name")
                .when(fileReportSaver)
                .generateFilename(eq("any id"), any());
        doThrow(new IOException())
                .when(fileReportSaver)
                .createFileForReport("report file name");

        fileReportSaver.save(report);

        verify(writer, times(0))
                .writeValue((File) any(), any());
    }

    @Test
    void generateFilenameTest() {
        String expected = "id = 'any id', date = '20200913152640000'";
        when(properties.getPatterns())
                .thenReturn(patterns);
        when(patterns.getReport())
                .thenReturn("id = '%s', date = '%s'");
        when(patterns.getTime())
                .thenReturn(new SimpleDateFormat("yyyyMMddHHmmssSSS"));

        String actual = fileReportSaver.generateFilename("any id", new Date(1_600_000_000_000L));

        assertEquals(expected, actual);
    }

    @Test
    void createFileForReportTest() throws IOException {
        File tempFolder = Files.createTempDirectory(null).toFile();
        when(properties.getFolder())
                .thenReturn(tempFolder.getAbsolutePath());

        File actual = fileReportSaver.createFileForReport("filename");

        assertEquals("/filename", actual.getAbsolutePath().replace(tempFolder.getAbsolutePath(), ""));
    }

    @Test
    void createFileForReportWhenFileWasCreatedThenThrowIOExceptionTest() throws IOException {
        IOException expected = new IOException("Impossible to create file, because it's already created.");

        File tempFolder = Files.createTempDirectory(null).toFile();
        assertTrue(tempFolder.toPath().resolve("filename").toFile().createNewFile());

        when(properties.getFolder())
                .thenReturn(tempFolder.getAbsolutePath());

        IOException actual = assertThrows(
                IOException.class,
                () -> fileReportSaver.createFileForReport("filename")
        );

        assertEquals(expected.getMessage(), actual.getMessage());
    }
}
