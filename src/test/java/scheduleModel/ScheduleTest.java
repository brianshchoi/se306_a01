package scheduleModel;

import org.junit.Before;
import org.junit.Test;
import taskModel.Task;

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
        assertNotEquals(a, b);
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

}