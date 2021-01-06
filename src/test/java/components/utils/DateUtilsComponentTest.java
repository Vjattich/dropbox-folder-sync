package components.utils;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class DateUtilsComponentTest {

    private final DateUtilsComponent dateUtilsComponent = new DateUtilsComponent();

    @Test
    public void testToDate() {
        assertTrue(dateUtilsComponent.toDate(1L) instanceof java.sql.Timestamp);
    }

    @Test
    public void testToDateWithoutNano() {
        assertTrue(dateUtilsComponent.toDateWithoutNano(1L) instanceof java.sql.Timestamp);
    }

    @Test
    public void testToLocalDateTimeWithoutNano() {
        Assert.assertEquals(
                dateUtilsComponent.toLocalDateTimeWithoutNano(1L),
                Instant.parse("1970-01-01T00:00:00.000Z").atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }

    @Test
    public void testToLocalDateTime() {
        Assert.assertEquals(
                dateUtilsComponent.toLocalDateTime(1L),
                Instant.parse("1970-01-01T00:00:00.001Z").atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }

    @Test
    public void testToLocalDateTimeWithoutNano2() {
        Assert.assertEquals(
                dateUtilsComponent.toLocalDateTimeWithoutNano(new Date(1)),
                Instant.parse("1970-01-01T00:00:00.000Z").atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}

