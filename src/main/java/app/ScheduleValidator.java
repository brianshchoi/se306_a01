package app;

import scheduleModel.ISchedule;

public class ScheduleValidator {

    ISchedule schedule;

    public ScheduleValidator(ISchedule schedule) {
        this.schedule = schedule;
    }

    public void validate() {

        if (true) { // Check some condition
            return; // Dodge the throw
        }

        throw new InvalidScheduleException("{Reason why invalid}");
    }
}
