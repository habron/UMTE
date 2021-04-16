package cz.habrondrej.garden.database;

import java.util.List;

public interface IRepository<T> {

    boolean create(T category);
    T getOneById(int id) throws IndexOutOfBoundsException;
    List<T> getAll();
    boolean update(T category);
    boolean deleteById(int id);
}
