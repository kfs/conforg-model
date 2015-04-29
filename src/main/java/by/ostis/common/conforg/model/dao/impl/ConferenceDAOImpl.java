package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.ConferenceDAO;
import by.ostis.common.conforg.model.dao.exception.DAOException;
import by.ostis.common.conforg.model.entity.Conference;
import by.ostis.common.sctpclient.model.ScAddress;
import by.ostis.common.sctpclient.utils.constants.ScElementType;

import java.util.UUID;

/* (non-Javadoc)
 * ConferenceDAO 
 */
public class ConferenceDAOImpl implements ConferenceDAO {

    private enum ScChildRelations {
        ;

        private String systemId;

        ScChildRelations(String systemId) {
            this.systemId = systemId;
        }

        public String getSystemId() {
            return systemId;
        }
    }

    public ConferenceDAOImpl() {
        super();
    }

    public UUID save(Conference element) throws DAOException {
        if (element.getSystemId() != null) {
            throw new DAOException("attempting to save object with non-empty system id");
        }
        ScAddress conferencesSpaceAdr = ScUtils.INSTANCE.findElement(ScSpaces.CONFERENCES.getSystemId());
        final UUID conferenceSystemId = UUID.randomUUID();
        ScAddress conferenceRootAdr = ScUtils.INSTANCE.createElWithGivenSystemId(conferenceSystemId);
        ScUtils.INSTANCE.createArc(ScElementType.SC_TYPE_ARC_COMMON, conferencesSpaceAdr, conferenceRootAdr);
        // TODO save content
        return conferenceSystemId;
    }

    public Conference read(UUID systemId) throws DAOException {
        throw new DAOException("not implemented yet");
    }
}
