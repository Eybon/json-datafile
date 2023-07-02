package fr.forge.json.datafile.java;

import fr.forge.json.datafile.JsonDatabase;
import fr.forge.json.datafile.JsonDatabaseException;
import fr.forge.json.datafile.model.FakeObject1;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class JsonDatabaseTest {

    @Test
    void test_givenFakeObject1_whenSaveObject_thenJsonDataFile() throws JsonDatabaseException {
        // Given
        FakeObject1 input = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        var jsonDataFile = new JsonDatabase<>(FakeObject1.class, "src/test/resources/java/output/fake_object_1.json");
        jsonDataFile.save(input);

        // Then
        Path result = Paths.get("src/test/resources/java/output/fake_object_1.json");
        assertTrue(Files.exists(result));
        assertTrue(Files.isRegularFile(result));
    }

    @Test
    void test_givenFakeObject1_whenSaveObjectWithWrongPath_thenJsonDataFileException() {
        // When
        var jsonDataFile = new JsonDatabase<>(FakeObject1.class, "src/test/wrong_path/fake_object_1.json");
        Exception resultException = assertThrows(JsonDatabaseException.class, () -> {
            jsonDataFile.save(FakeObject1.builder().build());
        });

        // Then
        assertNotNull(resultException);
        assertEquals(FileNotFoundException.class, resultException.getCause().getClass());
    }

    @Test
    void test_givenFakeObject1_whenLoadObject_thenGetObject() throws JsonDatabaseException {
        // Given
        FakeObject1 output = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        var jsonDataFile = new JsonDatabase<>(FakeObject1.class, "src/test/resources/java/output/fake_object_1.json");
        FakeObject1 result = jsonDataFile.load();

        // Then
        assertNotNull(result);
        assertEquals(output, result);
    }

    @Test
    void test_givenFakeObject1_whenLoadObjectWithWrongPath_thenJsonDataFileException() {
        // When
        var jsonDataFile = new JsonDatabase<>(FakeObject1.class, "src/test/wrong_path/fake_object_1.json");
        Exception resultException = assertThrows(JsonDatabaseException.class, jsonDataFile::load);

        // Then
        assertNotNull(resultException);
        assertEquals(FileNotFoundException.class, resultException.getCause().getClass());
    }

    @Test
    void test_givenFakeObject1_whenSaveAndLoadObject_thenJsonDataFileCreateAndLoad() throws JsonDatabaseException {
        // Given
        FakeObject1 input = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        var jsonDataFile = new JsonDatabase<>(FakeObject1.class, "src/test/resources/java/fake_object_1_full.json");
        jsonDataFile.save(input);
        FakeObject1 result = jsonDataFile.load();

        // Then
        Path fileResult = Paths.get("src/test/resources/java/fake_object_1_full.json");
        assertTrue(Files.exists(fileResult));
        assertTrue(Files.isRegularFile(fileResult));
        assertNotNull(result);
        assertEquals(result, input);
    }
}
