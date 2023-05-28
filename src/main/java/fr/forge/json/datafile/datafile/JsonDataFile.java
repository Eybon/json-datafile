package fr.forge.json.datafile.datafile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.util.stream.Collectors;

public class JsonDataFile<T> {

    private final ObjectMapper mapper;
    private final String fileName;
    private Class<T> type;

    public JsonDataFile(Class<T> type, String fileName) {
        this.type = type;
        this.fileName = fileName;
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveObject(T objectToSave) throws JsonDataFileException {
        try {
            this.saveFile(this.mapper.writeValueAsString(objectToSave));
        } catch (IOException e) {
            throw new JsonDataFileException(e);
        }
    }

    private void saveFile(String textToSave) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(this.fileName))) {
            writer.write(textToSave);
        }
    }

    public T loadObject() throws JsonDataFileException {
        try {
            return this.mapper.readValue(
                    this.loadFile(this.fileName),
                    this.type
            );
        } catch (IOException e) {
            throw new JsonDataFileException(e);
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
