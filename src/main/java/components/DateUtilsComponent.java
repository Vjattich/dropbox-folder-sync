package components;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtilsComponent {

    public Date toDate(long epochMilli) {
        return java.sql.Timestamp.valueOf(toLocalDateTime(epochMilli));
    }

    public Date toDateWithoutNano(long epochMilli) {
        return java.sql.Timestamp.valueOf(toLocalDateTimeWithoutNano(epochMilli));
    }

    public LocalDateTime toLocalDateTimeWithoutNano(Date date) {
        return toLocalDateTimeWithoutNano(date.getTime());
    }

    public LocalDateTime toLocalDateTimeWithoutNano(long epochMilli) {
        return toLocalDateTime(epochMilli).withNano(0);
    }

    public LocalDateTime toLocalDateTime(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
