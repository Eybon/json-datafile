package fr.forge.json.datafile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.stream.Collectors;

public class JsonDatabase<T> implements Database<T>{

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDatabase.class.getSimpleName());

    private final ObjectMapper mapper;
    private final String fileName;
    private Class<T> type;

    public JsonDatabase(Class<T> type, String fileName) {
        this.type = type;
        this.fileName = fileName;
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(T objectToSave) throws JsonDatabaseException {
        LOGGER.info("{}<{}> - Save value of {}", this.getClass().getSimpleName(), type.getSimpleName(), type.getSimpleName());
        try {
            this.saveFile(this.mapper.writeValueAsString(objectToSave));
        } catch (IOException e) {
            LOGGER.error("{}<{}> - Fail to save value of {}", this.getClass().getSimpleName(), type.getSimpleName(), type.getSimpleName());
            LOGGER.error(e.getMessage());
            throw new JsonDatabaseException(e);
        }
    }

    private void saveFile(String textToSave) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(this.fileName))) {
            writer.write(textToSave);
        }
    }

    public T load() throws JsonDatabaseException {
        LOGGER.info("{}<{}> - Load value of {}", this.getClass().getSimpleName(), type.getSimpleName(), type.getSimpleName());
        try {
            return this.mapper.readValue(
                    this.loadFile(this.fileName),
                    this.type
            );
        } catch (IOException e) {
            LOGGER.error("{}<{}> - Fail to load value of {}", this.getClass().getSimpleName(), type.getSimpleName(), type.getSimpleName());
            LOGGER.error(e.getMessage());
            throw new JsonDatabaseException(e);
        }
    }

    private String loadFile(String fileName) throws IOException {
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader reader = new BufferedReader(fileReader)) {
            return reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
