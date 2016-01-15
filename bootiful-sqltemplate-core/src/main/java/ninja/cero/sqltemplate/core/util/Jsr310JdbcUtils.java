package ninja.cero.sqltemplate.core.util;

import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Jsr310JdbcUtils {
    public static Object convertIfNecessary(Object value, ZoneId zoneId) {
        if (value instanceof LocalDateTime) {
            return Timestamp.valueOf((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            return Date.valueOf((LocalDate) value);
        } else if (value instanceof LocalTime) {
            return Time.valueOf((LocalTime) value);
        } else if (value instanceof OffsetDateTime) {
            OffsetDateTime offsetDateTime = ((OffsetDateTime) value).withOffsetSameInstant(zoneId.getRules().getOffset(Instant.now()));
            return Timestamp.valueOf(offsetDateTime.toLocalDateTime());
        } else if (value instanceof OffsetTime) {
            OffsetTime offsetTime = ((OffsetTime) value).withOffsetSameInstant(zoneId.getRules().getOffset(Instant.now()));
            return Time.valueOf(offsetTime.toLocalTime());
        } else if (value instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = ((ZonedDateTime) value).withZoneSameInstant(zoneId);
            return Timestamp.valueOf(zonedDateTime.toLocalDateTime());
        }

        return value;
    }

    public static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType, ZoneId zoneId) throws SQLException {
        if (LocalDateTime.class.equals(requiredType)) {
            return getAsLocalDateTime(rs, index);
        } else if (LocalDate.class.equals(requiredType)) {
            return getAsLocalDate(rs, index);
        } else if (LocalTime.class.equals(requiredType)) {
            return getAsLocalTime(rs, index);
        } else if (OffsetDateTime.class.equals(requiredType)) {
            return getAsOffsetDateTime(rs, index, zoneId);
        } else if (OffsetTime.class.equals(requiredType)) {
            return getAsOffsetTime(rs, index, zoneId);
        } else if (ZonedDateTime.class.equals(requiredType)) {
            return getAsZonedDateTime(rs, index, zoneId);
        }

        return JdbcUtils.getResultSetValue(rs, index, requiredType);
    }

    /**
     * Get the column value as LocalDateTime.
     * @param rs    ResultSet
     * @param index column index
     * @return column value
     * @throws SQLException in case of extraction failure
     */
    protected static LocalDateTime getAsLocalDateTime(ResultSet rs, int index) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(index);
        if (timestamp != null) {
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    /**
     * Get the column value as LocalDate.
     * @param rs    ResultSet
     * @param index column index
     * @return column value
     * @throws SQLException in case of extraction failure
     */
    protected static LocalDate getAsLocalDate(ResultSet rs, int index) throws SQLException {
        Date date = rs.getDate(index);
        if (date != null) {
            return date.toLocalDate();
        }
        return null;
    }

    /**
     * Get the column value as LocalTime.
     * @param rs    ResultSet
     * @param index column index
     * @return column value
     * @throws SQLException in case of extraction failure
     */
    protected static LocalTime getAsLocalTime(ResultSet rs, int index) throws SQLException {
        Time time = rs.getTime(index);
        if (time != null) {
            return time.toLocalTime();
        }
        return null;
    }

    /**
     * Get the column value as ZonedDateTime.
     * @param rs     ResultSet
     * @param index  column index
     * @param zoneId zoneId
     * @return column value
     * @throws SQLException in case of extraction failure
     */
    protected static ZonedDateTime getAsZonedDateTime(ResultSet rs, int index, ZoneId zoneId) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(index);
        if (timestamp != null) {
            return timestamp.toLocalDateTime().atZone(zoneId);
        }
        return null;
    }

    /**
     * Get the column value as OffsetDateTime.
     * @param rs     ResultSet
     * @param index  column index
     * @param zoneId zoneId
     * @return column value
     * @throws SQLException in case of extraction failure
     */
    protected static OffsetDateTime getAsOffsetDateTime(ResultSet rs, int index, ZoneId zoneId) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(index);
        if (timestamp != null) {
            return timestamp.toLocalDateTime().atZone(zoneId).toOffsetDateTime();
        }
        return null;
    }

    /**
     * Get the column value as OffsetTime.
     * @param rs     ResultSet
     * @param index  column index
     * @param zoneId zoneId
     * @return column value
     * @throws SQLException in case of extraction failure
     */
    protected static OffsetTime getAsOffsetTime(ResultSet rs, int index, ZoneId zoneId) throws SQLException {
        Time time = rs.getTime(index);
        if (time != null) {
            return time.toLocalTime().atOffset(zoneId.getRules().getOffset(Instant.now()));
        }
        return null;
    }
}

