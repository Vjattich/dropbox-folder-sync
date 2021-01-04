package components;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

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
        Assert.assertEquals(dateUtilsComponent.toLocalDateTimeWithoutNano(1L), LocalDateTime.of(1970, 1, 1, 6, 0, 0, 0));
    }

    @Test
    public void testToLocalDateTime() {
        Assert.assertEquals(dateUtilsComponent.toLocalDateTime(1L), LocalDateTime.of(1970, 1, 1, 6, 0, 0, 1000000));
    }
}

