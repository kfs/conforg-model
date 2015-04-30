package by.ostis.common.conforg.model.dao.impl;

enum ScCommonRelations implements ScIdentifiable {

    ADDRESS("conforg_address*");

    private String systemId;

    ScCommonRelations(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemId() {
        return systemId;
    }
}
