package scheduleModel;

import org.junit.Before;
import org.junit.Test;
import taskModel.Task;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ScheduleTest {

    private Schedule a, b;

    @Before
    public void setup() {
        a = new Schedule(5);
        a.schedule(new Task("a", 3), a.getProcessors().get(0), 0);
        b = new Schedule(1);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        b = a;
        assertEquals(a, b);
        b = (Schedule) a.clone();
        assertFalse(a.hashCode() == b.hashCode());
    }

    @Test
    public void testCloneModification() throws CloneNotSupportedException {
        b = a;
        b = (Schedule) a.clone();

        Task c = new Task("c", 6);
        a.schedule(c, a.getProcessors().get(0), 5);
        assertTrue(a.contains(c));
        assertFalse(b.contains(c));
    }

    @Test
    public void testIdleTime() {
        a.schedule(new Task("b", 5), a.getProcessors().get(0), 7);
        a.schedule(new Task("c", 2), a.getProcessors().get(1), 0);
        a.schedule(new Task("d", 1), a.getProcessors().get(1), 4);
        assertEquals(6, a.getIdleTime());
    }

    @Test
    public void testSameSchedule() {
        Schedule schedule1, schedule2;
        schedule1 = new Schedule(2);
        schedule2 = new Schedule(2);
        Task a, b;
        a = new Task("a", 1);
        b = new Task("b", 2);
        schedule1.schedule(a, schedule1.getProcessors().get(0), 0);
        schedule1.schedule(b, schedule1.getProcessors().get(1), 2);

        schedule2.schedule(a, schedule2.getProcessors().get(1), 0);
        schedule2.schedule(b, schedule2.getProcessors().get(0), 2);

        Set<ISchedule> schedules = new HashSet<>();
        schedules.add(schedule1);
        schedules.add(schedule2);
        assertEquals(1, schedules.size());

        Set<Task> tasks = new HashSet<>();
        tasks.add(a);
        tasks.add(new Task("a", 2));
        assertEquals(1, tasks.size());

        Set<IProcessor> processors = new HashSet<>();
        processors.add(schedule1.getProcessorOf(a));
        processors.add(schedule2.getProcessorOf(a));
        assertEquals(1, processors.size());

        Schedule schedule3 = new Schedule(2);
        schedule3.schedule(a, schedule2.getProcessors().get(1), 2);
        schedule3.schedule(b, schedule2.getProcessors().get(0), 0);
        schedules.add(schedule3);
        assertEquals(2, schedules.size());

        //assertTrue(schedule1.equals(schedule2));
    }
}