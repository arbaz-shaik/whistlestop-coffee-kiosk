package uk.ac.ncl.csc8019backend.business.order;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import uk.ac.ncl.csc8019backend.business.common.InvalidPickupTimeException;


public class OpeningHoursUtil {
    // opening hours constants
    // localtime of (hour, minute) --> 24 hour format

    private static final LocalTime WEEKDAY_OPEN = LocalTime.of(6,30); // 6:30 
    private static final LocalTime WEEKDAY_CLOSE = LocalTime.of(19,0); // 19:00
    private static final LocalTime SAT_OPEN = LocalTime.of(7,0); // 7:00
    private static final LocalTime SAT_CLOSE = LocalTime.of(18,0); // 18:00

// private constructor to 
// it is utility class and all logic is in the static method.

private OpeningHoursUtil() {}

    /**
     * Validates a pickup time against kiosk opening hours.
     * Throws InvalidPickupTimeException with a clear message if invalid.
     * Called by OrderService before creating an order.
     *
     * Rules:
     *   - Cannot be in the past
     *   - No orders on Sundays
     *   - Mon-Fri: 06:30 - 19:00
     *   - Saturday: 07:00 - 18:00
     * Author: parthbhilare
     */

        public static void validate(LocalDateTime pickupTime) {

            //rule 1: cannot be in the past.
        if(pickupTime.isBefore(LocalDateTime.now())){
            throw new InvalidPickupTimeException("Pickup time cannot be in the past.");

        }
        DayOfWeek day = pickupTime.getDayOfWeek();
        LocalTime time = pickupTime.toLocalTime();

        // rule 2: no orders on sundays
        if (day ==DayOfWeek.SUNDAY){
            throw new InvalidPickupTimeException("Kiosk is closed on sundays.");

        }
        // rule 3: Saturday hours 
        if(day== DayOfWeek.SATURDAY){
            if (time.isBefore(SAT_OPEN) || time.isAfter(SAT_CLOSE)){
                throw new InvalidPickupTimeException("Saturday pickup must be between 07:00 and 18:00, got:" + time);

            }
            return; // valid saturday time, exit method
        }
        // rule 4: weekday hours (Monday to Friday)
        if(time.isBefore(WEEKDAY_OPEN) || time.isAfter(WEEKDAY_CLOSE)){
            throw new InvalidPickupTimeException("Weekday pickup must be between 06:30 and 19:00, got:" + time);
        }

    }
    
}

