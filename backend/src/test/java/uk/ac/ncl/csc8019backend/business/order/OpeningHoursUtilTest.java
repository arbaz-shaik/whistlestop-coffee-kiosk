package uk.ac.ncl.csc8019backend.business.order;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import uk.ac.ncl.csc8019backend.business.common.InvalidPickupTimeException;

class OpeningHoursUtilTest {
// past day 
    @Test
    void validate_throwsException_whenTimeIsInThePast(){
        LocalDateTime past = LocalDateTime.now().minusHours(1);
        assertThrows(InvalidPickupTimeException.class,() -> OpeningHoursUtil.validate(past));
    }
// sunday
    @Test
    void validate_throwsException_whenDayIsSunday(){
    LocalDateTime sunday =LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).withHour(10).withMinute(0).withSecond(0).withNano(0);
    assertThrows(InvalidPickupTimeException.class, ()-> OpeningHoursUtil.validate(sunday));

}

//weekday valid    

     @Test
    void validate_passes_forWeekdayWithinHours() {
        LocalDateTime monday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(10).withMinute(0).withSecond(0).withNano(0);
        assertDoesNotThrow(() -> OpeningHoursUtil.validate(monday));
    }

    @Test
    void validate_throwsException_forWeekdayBeforeOpening() {
        LocalDateTime monday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(6).withMinute(0).withSecond(0).withNano(0);
        assertThrows(InvalidPickupTimeException.class, () -> OpeningHoursUtil.validate(monday));
    }

    @Test
    void validate_throwsException_forWeekdayAfterClosing() {
        LocalDateTime monday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(20).withMinute(0).withSecond(0).withNano(0);
        assertThrows(InvalidPickupTimeException.class, () -> OpeningHoursUtil.validate(monday));
    }
// Saturday valid

    @Test
    void validate_passes_forSaturdayWithinHours(){
        LocalDateTime saturday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).withHour(10).withMinute(0).withSecond(0).withNano(0);
        assertDoesNotThrow(()-> OpeningHoursUtil.validate(saturday));
    }
     @Test
    void validate_throwsException_forSaturdaybeforOpening(){
        LocalDateTime saturday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).withHour(6).withMinute(0);
        assertThrows(InvalidPickupTimeException.class, ()-> OpeningHoursUtil.validate(saturday));
    }
    @Test
    void validate_throwsException_forSaturdayAfterClosing(){
        LocalDateTime saturday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).withHour(19).withMinute(0).withSecond(0).withNano(0);
        assertThrows(InvalidPickupTimeException.class, ()-> OpeningHoursUtil.validate(saturday));
    }


}
