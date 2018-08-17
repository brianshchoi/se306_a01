package view.listeners;

import scheduleModel.ISchedule;

/* Makes sure that every class that implements this interface
 * has appropriate implementations for when branches, timer or schedule
  * needs to be updated. */
public interface AlgorithmListener {
    void bestScheduleUpdated(ISchedule schedule);
    void algorithmFinished();
    void numberOfBranchesChanged();
}
