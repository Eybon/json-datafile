package fr.forge.json.datafile;

import fr.forge.json.datafile.datafile.JsonDataFile;
import fr.forge.json.datafile.datafile.JsonDataFileException;
import fr.forge.json.datafile.fake.FakeObject1;
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

public class JsonDataFileTest {

    @Test
    void test_givenFakeObject1_whenSaveObject_thenJsonDataFile() throws JsonDataFileException {
        // Given
        FakeObject1 input = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        var jsonDataFile = new JsonDataFile<>(FakeObject1.class, "src/test/resources/output/fake_object_1.json");
        jsonDataFile.saveObject(input);

        // Then
        Path result = Paths.get("src/test/resources/output/fake_object_1.json");
        assertTrue(Files.exists(result));
        assertTrue(Files.isRegularFile(result));
    }

    @Test
    void test_givenFakeObject1_whenSaveObjectWithWrongPath_thenJsonDataFileException() {
        // When
        var jsonDataFile = new JsonDataFile<>(FakeObject1.class, "src/test/wrong_path/fake_object_1.json");
        Exception resultException = assertThrows(JsonDataFileException.class, () -> {
            jsonDataFile.saveObject(FakeObject1.builder().build());
        });

        // Then
        assertNotNull(resultException);
        assertEquals(FileNotFoundException.class, resultException.getCause().getClass());
    }

    @Test
    void test_givenFakeObject1_whenLoadObject_thenGetObject() throws JsonDataFileException {
        // Given
        FakeObject1 output = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        var jsonDataFile = new JsonDataFile<>(FakeObject1.class, "src/test/resources/output/fake_object_1.json");
        FakeObject1 result = jsonDataFile.loadObject();

        // Then
        assertNotNull(result);
        assertEquals(output, result);
    }

    @Test
    void test_givenFakeObject1_whenLoadObjectWithWrongPath_thenJsonDataFileException() {
        // When
        var jsonDataFile = new JsonDataFile<>(FakeObject1.class, "src/test/wrong_path/fake_object_1.json");
        Exception resultException = assertThrows(JsonDataFileException.class, jsonDataFile::loadObject);

        // Then
        assertNotNull(resultException);
        assertEquals(FileNotFoundException.class, resultException.getCause().getClass());
    }

    @Test
    void test_givenFakeObject1_whenSaveAndLoadObject_thenJsonDataFileCreateAndLoad() throws JsonDataFileException {
        // Given
        FakeObject1 input = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        var jsonDataFile = new JsonDataFile<>(FakeObject1.class, "src/test/resources/fake_object_1_full.json");
        jsonDataFile.saveObject(input);
        FakeObject1 result = jsonDataFile.loadObject();

        // Then
        Path fileResult = Paths.get("src/test/resources/fake_object_1_full.json");
        assertTrue(Files.exists(fileResult));
        assertTrue(Files.isRegularFile(fileResult));
        assertNotNull(result);
        assertEquals(result, input);
    }
}
