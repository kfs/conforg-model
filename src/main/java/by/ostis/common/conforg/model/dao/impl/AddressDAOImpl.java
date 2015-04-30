package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.AddressDAO;
import by.ostis.common.conforg.model.dao.exception.DAOException;
import by.ostis.common.conforg.model.dao.exception.TypeMismatchException;
import by.ostis.common.conforg.model.entity.Address;
import by.ostis.common.sctpclient.model.ScAddress;
import by.ostis.common.sctpclient.model.ScString;
import by.ostis.common.sctpclient.utils.constants.ScElementType;

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

        ScAddress addressSpaceNode = ScUtils.INSTANCE.findElement(ScSpaces.ADDRESS.getSystemId());
        ScUtils.INSTANCE.createArc(ScElementType.SC_TYPE_ARC_POS, addressSpaceNode, addressNode);

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

    public Address read(UUID systemId) throws DAOException {
        if (systemId == null) {
            throw new NullPointerException("cannot find element, system id is null");
        }
        ScAddress addressElement = ScUtils.INSTANCE.findElement(systemId);
        boolean convenientType = ScUtils.INSTANCE.isElementOfSpace(addressElement, ScSpaces.ADDRESS);
        if (!convenientType) {
            throw new TypeMismatchException("element with given system id doesn't belong to address space, id: "
                    + systemId);
        }
        Address address = readFields(addressElement);
        address.setSystemId(systemId);
        return address;
    }

    private Address readFields(ScAddress addressElement) throws DAOException {
        ScAddress countryAdr = ScUtils.INSTANCE.findUniqueElementByParentAndRelation(addressElement,
                ScChildRelations.COUNTRY);
        String countryContent = ScUtils.INSTANCE.findElementContent(countryAdr);

        ScAddress cityAdr = ScUtils.INSTANCE.findUniqueElementByParentAndRelation(addressElement,
                ScChildRelations.CITY);
        String cityContent = ScUtils.INSTANCE.findElementContent(cityAdr);

        ScAddress streetAdr = ScUtils.INSTANCE.findUniqueElementByParentAndRelation(addressElement,
                ScChildRelations.STREET);
        String streetContent = ScUtils.INSTANCE.findElementContent(streetAdr);

        ScAddress houseNumberAdr = ScUtils.INSTANCE.findUniqueElementByParentAndRelation(addressElement,
                ScChildRelations.HOUSE_NUMBER);
        String houseNumberContent = ScUtils.INSTANCE.findElementContent(houseNumberAdr);

        return new Address(countryContent, cityContent, streetContent, houseNumberContent);
    }
}
