package fr.forge.json.datafile;

public interface Database<T> {
    void save(T object) throws JsonDatabaseException;
    T load() throws JsonDatabaseException;
}
