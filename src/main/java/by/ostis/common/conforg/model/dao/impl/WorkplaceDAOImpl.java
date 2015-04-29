package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.WorkplaceDAO;
import by.ostis.common.conforg.model.dao.exception.DAOException;
import by.ostis.common.conforg.model.entity.Workplace;
import by.ostis.common.sctpclient.model.ScAddress;
import by.ostis.common.sctpclient.model.ScString;
import by.ostis.common.sctpclient.utils.constants.ScElementType;

import java.util.UUID;

public class WorkplaceDAOImpl implements WorkplaceDAO {

    private enum ScChildRelations implements ScIdentifiable {

        POSITION("conforg_workplace_position*"),
        WORKPLACE("conforg_workplace_workplace*");

        private String systemId;

        ScChildRelations(String systemId) {
            this.systemId = systemId;
        }

        public String getSystemId() {
            return systemId;
        }
    }

    public WorkplaceDAOImpl() {
        super();
    }

    public UUID save(Workplace element) throws DAOException {
        if (element.getSystemId() != null) {
            throw new DAOException("attempting to save object with non-empty system id");
        }
        final UUID addressUuid = UUID.randomUUID();
        ScAddress addressNode = ScUtils.INSTANCE.createElWithGivenSystemId(addressUuid);

        ScAddress addressSpaceNode = ScUtils.INSTANCE.findElement(ScSpaces.WORKPLACE.getSystemId());
        ScUtils.INSTANCE.createArc(ScElementType.SC_TYPE_ARC_POS, addressSpaceNode, addressNode);

        saveFields(element, addressNode);

        element.setSystemId(addressUuid);
        return addressUuid;
    }

    @Override
    public Workplace read(UUID systemId) throws DAOException {
        return null;
    }

    private void saveFields(Workplace element, ScAddress addressNode) throws DAOException {
        ScString positionContent = ScUtils.INSTANCE.wrapString(element.getPosition());
        ScAddress positionNode = ScUtils.INSTANCE.createNodeWithContent(positionContent);
        ScUtils.INSTANCE.createRelation(addressNode, positionNode, ScChildRelations.POSITION);

        ScString workplaceContent = ScUtils.INSTANCE.wrapString(element.getWorkplace());
        ScAddress workplaceNode = ScUtils.INSTANCE.createNodeWithContent(workplaceContent);
        ScUtils.INSTANCE.createRelation(addressNode, workplaceNode, ScChildRelations.WORKPLACE);
    }
}
