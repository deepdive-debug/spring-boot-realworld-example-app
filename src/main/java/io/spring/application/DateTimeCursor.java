package io.spring.application;

import java.time.Instant;
import java.time.ZoneOffset;

public class DateTimeCursor extends PageCursor<Instant> {

  public DateTimeCursor(Instant data) {
    super(data);
  }

  @Override
  public String toString() {
    return String.valueOf(getData().toEpochMilli());
  }

  public static Instant parse(String cursor) {
    if (cursor == null) {
      return null;
    }
    return Instant.ofEpochMilli(Long.parseLong(cursor)).atOffset(ZoneOffset.UTC).toInstant();
  }
}
