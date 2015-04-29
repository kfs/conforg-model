package by.ostis.common.conforg.model.dao.impl;

enum ScSpaces implements ScIdentifiable {
    CONFERENCES("conforg_conferences_space"),
    SECTIONS("conforg_sections_space"),
    REPORTS("conforg_reports_space"),
    PARTICIPANTS("conforg_participants_space"),
    PERSONS("conforg_persons_space");

    private String spaceSystemId;

    ScSpaces(String spaceSystemId) {
        this.spaceSystemId = spaceSystemId;
    }

    public String getSystemId() {
        return spaceSystemId;
    }
}
