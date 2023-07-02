package fr.forge.json.datafile.spring;

import fr.forge.json.datafile.Database;
import fr.forge.json.datafile.JsonDatabase;
import fr.forge.json.datafile.JsonDatabaseException;
import fr.forge.json.datafile.model.FakeObject1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class JsonDatabaseSpringTest {

    private static final Path DATABASE_PATH = Paths.get("src/test/resources/spring/database.json");

    @Autowired
    private Database<FakeObject1> database;

    @TestConfiguration
    static class contextConfiguration {
        @Bean
        Database<FakeObject1> database() {
            String fileName = "src/test/resources/spring/database.json";
            return new JsonDatabase<>(FakeObject1.class, fileName);
        }
    }

    @Test
    void test_spring_givenFakeObject1_whenSaveObject_thenJsonDataFile() throws JsonDatabaseException, IOException {
        // Init
        if(Files.exists(DATABASE_PATH)) Files.delete(DATABASE_PATH);

        // Given
        FakeObject1 objectToSave = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        database.save(objectToSave);

        // Then
        assertTrue(Files.exists(DATABASE_PATH));
        assertTrue(Files.isRegularFile(DATABASE_PATH));
        assertEquals(Files.mismatch(Paths.get("src/test/resources/spring/expected/expected_fake_object_1.json"), DATABASE_PATH), -1L);
    }

    @Test
    void test_spring_givenFakeObject1_whenLoadObject_thenGetObject() throws JsonDatabaseException, IOException {
        // Init
        if(Files.exists(DATABASE_PATH)) Files.delete(DATABASE_PATH);
        Path input = Paths.get("src/test/resources/spring/input/input_fake_object_1.json");
        Files.copy(input, DATABASE_PATH, StandardCopyOption.REPLACE_EXISTING);

        // Given
        FakeObject1 expect = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        FakeObject1 result = database.load();

        // Then
        assertNotNull(result);
        assertEquals(expect, result);
        assertEquals(Files.mismatch(Paths.get("src/test/resources/spring/expected/expected_fake_object_1.json"), DATABASE_PATH), -1L);
    }

    @Test
    void test_spring_givenFakeObject1_whenSaveAndLoadObject_thenJsonDataFileCreateAndLoad() throws JsonDatabaseException, IOException {
        // Init
        if(Files.exists(DATABASE_PATH)) Files.delete(DATABASE_PATH);
        Path input = Paths.get("src/test/resources/spring/input/input_fake_object_1.json");
        Files.copy(input, DATABASE_PATH, StandardCopyOption.REPLACE_EXISTING);

        // Given
        FakeObject1 objectToSave = FakeObject1.builder()
                .field1("string")
                .field2(1)
                .field3(BigDecimal.TEN)
                .field4(LocalDate.of(2023, Month.MAY, 5))
                .field5(LocalDateTime.of(2023, Month.MAY, 5, 11, 30))
                .build();

        // When
        database.save(objectToSave);
        FakeObject1 result = database.load();

        // Then
        assertTrue(Files.exists(DATABASE_PATH));
        assertTrue(Files.isRegularFile(DATABASE_PATH));
        assertEquals(Files.mismatch(Paths.get("src/test/resources/spring/expected/expected_fake_object_1.json"), DATABASE_PATH), -1L);
        assertNotNull(result);
        assertEquals(result, objectToSave);
    }
}
