package by.ostis.common.conforg.model.dao;

import by.ostis.common.conforg.model.dao.exception.DAOException;

import java.util.UUID;

public interface BaseDAO<Type> {

    /**
     * Performs save operation.
     * @param element element to be stored.
     * @return system identifier of stored element.
     * @throws DAOException if system identifier of @param element is non-empty.
     */
    UUID save(Type element) throws DAOException;
}