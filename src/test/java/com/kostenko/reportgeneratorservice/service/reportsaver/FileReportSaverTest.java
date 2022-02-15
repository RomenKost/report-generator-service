package com.kostenko.reportgeneratorservice.service.reportsaver;

import com.kostenko.reportgeneratorservice.service.reportsaver.impl.FileReportSaver;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileReportSaverTest {
    private File dir;
    private FileReportSaver fileReportSaver;

    @BeforeEach
    public void initialize() throws IOException {
        dir = Files.createTempDirectory(null).toFile();

        fileReportSaver = new FileReportSaver(
                "", "report_test_%s%s.rt", dir.getAbsolutePath()
        );
    }

    @AfterEach
    public void clear() throws IOException {
        FileUtils.deleteDirectory(dir);
    }

    @Test
    void saveTest() throws IOException {
        fileReportSaver.save("any_report", "any_id");
        assertEquals(1, Files.list(dir.toPath()).count());

        Optional<Path> optionalFilePath =  Files.list(dir.toPath()).findFirst();
        assertTrue(optionalFilePath.isPresent());

        Path filePath = optionalFilePath.get();

        assertEquals("report_test_any_id.rt", filePath.getFileName().toString());
        assertEquals("any_report", Files.readString(filePath));
    }
    
    @Test
    void saveAlreadySavedReportTest() throws IOException {
        File report = dir.toPath().resolve("report_test_any_id.rt").toFile();
        report.createNewFile();
        IOException exception = assertThrows(
                IOException.class,
                () -> fileReportSaver.save("any_report", "any_id")
        );

        String expected = "Impossible to create file, because it's already created.";

        assertEquals(expected, exception.getMessage());
    }
}
