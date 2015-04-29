package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.exception.DAOException;
import by.ostis.common.sctpclient.client.SctpClient;
import by.ostis.common.sctpclient.client.SctpClientImpl;
import by.ostis.common.sctpclient.exception.SctpClientException;
import by.ostis.common.sctpclient.model.ScAddress;
import by.ostis.common.sctpclient.model.ScIterator;
import by.ostis.common.sctpclient.model.ScString;
import by.ostis.common.sctpclient.model.response.SctpResponse;
import by.ostis.common.sctpclient.model.response.SctpResponseHeader;
import by.ostis.common.sctpclient.model.response.SctpResultType;
import by.ostis.common.sctpclient.utils.constants.ScElementType;

import java.util.List;
import java.util.UUID;

/* (non-Javadoc)
 * ScUtils 
 */
enum ScUtils {

    INSTANCE;

    private final SctpClient sctpClient;

    ScUtils() {
        sctpClient = new SctpClientImpl();
    }

    public ScString wrapString(String content) {
        return new ScString(content);
    }

    public ScString wrapUuid(UUID uuid) {
        final String systemId = uuid.toString();
        return new ScString(systemId);
    }

    public ScAddress findElement(String systemId) throws DAOException {
        SctpResponse<ScAddress> response;
        try {
            ScString scId = wrapString(systemId);
            response = sctpClient.searchElement(scId);
        } catch (SctpClientException e) {
            throw new DAOException("element not found: " + systemId, e);
        }
        checkHeader(response.getHeader());
        return response.getAnswer();
    }

    public void checkHeader(SctpResponseHeader header) throws DAOException {
        SctpResultType resultType = header.getResultType();
        switch (resultType) {
            case SCTP_RESULT_ERROR_NO_ELEMENT:
                throw new DAOException("element not found");
            case SCTP_RESULT_FAIL:
                throw new DAOException("error occurred - assertion failure");
            case SCTP_RESULT_OK:
                break;
            default:
                throw new DAOException("unsupported result type: " + resultType);
        }
    }

    public ScAddress createElWithGivenSystemId(UUID systemId) throws DAOException {
        ScAddress newNode = createEmptyNode();
        ScString scId = wrapUuid(systemId);
        try {
            SctpResponse<Boolean> setSysIdResponse = sctpClient.setSystemIdentifier(newNode, scId);
            checkHeader(setSysIdResponse.getHeader());
            if (setSysIdResponse.getAnswer() != Boolean.TRUE) {
                throw new DAOException("cannot set system id for node, id: " + systemId);
            }
        } catch (SctpClientException e) {
            // TODO we have to collect garbage here in order to prevent "memory leaks" (i.e. el was created, but system id wasn't, so we can't use the el)
            throw new DAOException("cannot set system id for previously created element", e);
        }
        return newNode;
    }

    private ScAddress createEmptyNode() throws DAOException {
        SctpResponse<ScAddress> response;
        try {
            response = sctpClient.createElement(ScElementType.SC_TYPE_NODE);
        } catch (SctpClientException e) {
            throw new DAOException("failed to create a node", e);
        }
        checkHeader(response.getHeader());
        return response.getAnswer();
    }

    public void createRelation(ScAddress parent, ScAddress child, ScIdentifiable relationId) throws DAOException {
        ScAddress commonArc = createArc(ScElementType.SC_TYPE_ARC_COMMON, parent, child);
        ScAddress relationNode = findElement(relationId.getSystemId());
        createArc(ScElementType.SC_TYPE_ARC_POS, relationNode, commonArc);
    }

    public ScAddress createArc(ScElementType elementType, ScAddress firstEl, ScAddress secondEl) throws DAOException {
        try {
            SctpResponse<ScAddress> response;
            response = sctpClient.createScArc(elementType, firstEl, secondEl);
            checkHeader(response.getHeader());
            return response.getAnswer();
        } catch (SctpClientException e) {
            throw new DAOException("cannot create arc, type: " + elementType, e);
        }
    }

    public ScAddress createNodeWithContent(ScString content) throws DAOException {
        ScAddress node = createEmptyNode();
        try {
            SctpResponse<Boolean> response = sctpClient.setScRefContent(node, content);
            checkHeader(response.getHeader());
            Boolean successfully = response.getAnswer();
            if (!successfully) {
                throw new DAOException("cannot set node content");
            }
            return node;
        } catch (SctpClientException e) {
            throw new DAOException("cannot set node content", e);
        }
    }

//    public ScAddress createElWithContent(ScAddress parent, String content) throws DAOException {
//        try {
//            sctpClient.searchByIterator(ScIteratorType.SCTP_ITERATOR_3F_A_A, Arrays.asList(parent, ScElementType.SC_TYPE_ARC, ScElementType.SC_TYPE_NODE));
//        } catch (SctpClientException e) {
//            throw new DAOException("cannot get children from parent :" + parent, e);
//        }
//    }

//    public List<ScIterator> findTripletByFixedNode() {
//
//    }
}
