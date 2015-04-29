package by.ostis.common.conforg.model.dao.impl;

import by.ostis.common.conforg.model.dao.SCStrings;
import by.ostis.common.sctpclient.model.ScString;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

class SCStringsImpl implements SCStrings{

    @Override
    public ScString wrap(String value) {
        return StringUtils.isNotEmpty(value) ? new ScString(value) : new ScString(StringUtils.EMPTY);
    }

    @Override
    public ScString wrap(DateTime dateTime) {
        ScString result;
        if(dateTime != null){
            DateTimeFormatter formatter = ISODateTimeFormat.basicDateTime();
            String stringDate = formatter.print(dateTime);
            result = new ScString(stringDate);
        } else {
            result = new ScString(StringUtils.EMPTY);
        }
        return result;
    }

    @Override
    public ScString wrap(Integer value) {
        return value == null ? new ScString(StringUtils.EMPTY) : new ScString(value.toString());
    }

    @Override
    public String unwrapToString(String scString) {
        return StringUtils.EMPTY.equals(scString) ? StringUtils.EMPTY : scString;
    }

    @Override
    public DateTime unwrapToDateTime(String scString) {
        DateTime result;
        if(!scString.equals(StringUtils.EMPTY)){
            DateTimeFormatter formatter = ISODateTimeFormat.basicDateTime();
            result = formatter.parseDateTime(scString);
        } else {
            result = null;
        }
        return result;
    }

    @Override
    public Integer unwrapToInteger(String scString) {
        return scString.equals(StringUtils.EMPTY) ? null : new Integer(scString);
    }
}
