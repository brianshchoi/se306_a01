package view.listeners;

import scheduleModel.ISchedule;

/**
 * All implementations of this interface are seen as
 * objects interested in changes in an AlgorithmObservable.
 * This is the observer in the observer pattern.
 * For example, when an AlgorithmObservable (an algorithm) updates
 * its best schedule, it will notify all its registered AlgorithmListeners
 * by invoking bestScheduleUpdated().
 */
public interface AlgorithmListener {
    void bestScheduleUpdated(ISchedule schedule);
    void algorithmFinished();
    void numberOfBranchesChanged();
}
