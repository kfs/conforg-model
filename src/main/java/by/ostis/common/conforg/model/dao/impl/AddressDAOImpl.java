package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.AddressDAO;
import by.ostis.common.conforg.model.dao.exception.DAOException;
import by.ostis.common.conforg.model.entity.Address;
import by.ostis.common.sctpclient.model.ScAddress;
import by.ostis.common.sctpclient.model.ScString;

import java.util.UUID;

public class AddressDAOImpl implements AddressDAO {

    private enum ScChildRelations implements ScIdentifiable {

        COUNTRY("conforg_address_country*"),
        CITY("conforg_address_city*"),
        STREET("conforg_address_street*"),
        HOUSE_NUMBER("conforg_address_house_number*");

        private String systemId;

        ScChildRelations(String systemId) {
            this.systemId = systemId;
        }

        public String getSystemId() {
            return systemId;
        }
    }

    public AddressDAOImpl() {
        super();
    }

    public UUID save(Address element) throws DAOException {
        if (element.getSystemId() != null) {
            throw new DAOException("attempting to save object with non-empty system id");
        }
        final UUID addressUuid = UUID.randomUUID();
        ScAddress addressNode = ScUtils.INSTANCE.createElWithGivenSystemId(addressUuid);

        saveFields(element, addressNode);

        element.setSystemId(addressUuid);
        return addressUuid;
    }

    private void saveFields(Address element, ScAddress addressNode) throws DAOException {
        ScString countryContent = ScUtils.INSTANCE.wrapString(element.getCountry());
        ScAddress countryNode = ScUtils.INSTANCE.createNodeWithContent(countryContent);
        ScUtils.INSTANCE.createRelation(addressNode, countryNode, ScChildRelations.COUNTRY);

        ScString cityContent = ScUtils.INSTANCE.wrapString(element.getCity());
        ScAddress cityNode = ScUtils.INSTANCE.createNodeWithContent(cityContent);
        ScUtils.INSTANCE.createRelation(addressNode, cityNode, ScChildRelations.CITY);

        ScString streetContent = ScUtils.INSTANCE.wrapString(element.getStreet());
        ScAddress streetNode = ScUtils.INSTANCE.createNodeWithContent(streetContent);
        ScUtils.INSTANCE.createRelation(addressNode, streetNode, ScChildRelations.STREET);

        ScString houseNumberContent = ScUtils.INSTANCE.wrapString(element.getHouseNumber());
        ScAddress houseNumberNode = ScUtils.INSTANCE.createNodeWithContent(houseNumberContent);
        ScUtils.INSTANCE.createRelation(addressNode, houseNumberNode, ScChildRelations.HOUSE_NUMBER);
    }
}
