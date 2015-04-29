package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.AcademicDegreeDAO;
import by.ostis.common.conforg.model.dao.exception.DAOException;
import by.ostis.common.conforg.model.entity.AcademicDegree;
import by.ostis.common.sctpclient.model.ScAddress;
import by.ostis.common.sctpclient.model.ScString;
import by.ostis.common.sctpclient.utils.constants.ScElementType;

import java.util.UUID;

public class AcademicDegreeDAOImpl implements AcademicDegreeDAO {

    private enum ScChildRelations implements ScIdentifiable {

        DEGREE("conforg_academic_degree_degree*"),
        TITLE("conforg_academic_degree_title*");

        private String systemId;

        ScChildRelations(String systemId) {
            this.systemId = systemId;
        }

        public String getSystemId() {
            return systemId;
        }
    }

    public AcademicDegreeDAOImpl() {
        super();
    }

    public UUID save(AcademicDegree element) throws DAOException {
        if (element.getSystemId() != null) {
            throw new DAOException("attempting to save object with non-empty system id");
        }
        final UUID addressUuid = UUID.randomUUID();
        ScAddress addressNode = ScUtils.INSTANCE.createElWithGivenSystemId(addressUuid);

        ScAddress addressSpaceNode = ScUtils.INSTANCE.findElement(ScSpaces.ACADEMIC_DEGREE.getSystemId());
        ScUtils.INSTANCE.createArc(ScElementType.SC_TYPE_ARC_POS, addressSpaceNode, addressNode);

        saveFields(element, addressNode);

        element.setSystemId(addressUuid);
        return addressUuid;
    }

    @Override
    public AcademicDegree read(UUID systemId) throws DAOException {
        return null;
    }

    private void saveFields(AcademicDegree element, ScAddress addressNode) throws DAOException {
        ScString degreeContent = ScUtils.INSTANCE.wrapString(element.getDegree());
        ScAddress degreeNode = ScUtils.INSTANCE.createNodeWithContent(degreeContent);
        ScUtils.INSTANCE.createRelation(addressNode, degreeNode, ScChildRelations.DEGREE);

        ScString titleContent = ScUtils.INSTANCE.wrapString(element.getTitle());
        ScAddress titleNode = ScUtils.INSTANCE.createNodeWithContent(titleContent);
        ScUtils.INSTANCE.createRelation(addressNode, titleNode, ScChildRelations.TITLE);
    }
}
