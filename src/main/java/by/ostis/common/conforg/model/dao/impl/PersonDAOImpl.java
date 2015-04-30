package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.PersonDAO;
import by.ostis.common.conforg.model.dao.exception.DAOException;
import by.ostis.common.conforg.model.entity.Person;
import by.ostis.common.sctpclient.model.ScAddress;

import java.util.UUID;

public class PersonDAOImpl extends BaseDAOImpl<Person> implements PersonDAO {

    private enum ScChildRelations implements ScIdentifiable {

        FIRST_NAME("conforg_persons_first_name*"),
        PATRONYMIC("conforg_persons_patronymic*"),
        LAST_NAME("conforg_persons_last_name*");

        private String systemId;

        ScChildRelations(String systemId) {
            this.systemId = systemId;
        }

        public String getSystemId() {
            return systemId;
        }
    }

    public PersonDAOImpl() {
        super(ScSpaces.PERSONS);
    }

    @Override
    protected void saveFields(Person element, ScAddress parentNode) {
        // TODO save fields
    }

    public Person read(UUID systemId) throws DAOException {
        throw new DAOException("not implemented yet");
    }
}
