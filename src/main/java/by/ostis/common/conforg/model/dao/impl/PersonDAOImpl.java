package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.PersonDAO;
import by.ostis.common.conforg.model.dao.exception.DAOException;
import by.ostis.common.conforg.model.entity.Person;
import by.ostis.common.sctpclient.model.ScAddress;
import by.ostis.common.sctpclient.model.ScString;

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
    protected void saveFields(Person element, ScAddress parentNode) throws DAOException {
        ScString firstNameContent = ScStrings.wrap(element.getFirstName());
        ScAddress firstNameNode = ScUtils.INSTANCE.createNodeWithContent(firstNameContent);
        ScUtils.INSTANCE.createRelation(parentNode, firstNameNode, ScChildRelations.FIRST_NAME);
    }

    @Override
    protected Person readFields(ScAddress elementNode) throws DAOException {
        return null;
    }
}
