package by.ostis.common.conforg.model.dao;

import by.ostis.common.sctpclient.model.ScString;
import org.joda.time.DateTime;

public interface SCStrings {

    ScString wrap(String value);
    ScString wrap(DateTime dateTime);
    ScString wrap(Integer value);

    String unwrapToString(String scString);
    DateTime unwrapToDateTime(String scString);
    Integer unwrapToInteger(String scString);
}
