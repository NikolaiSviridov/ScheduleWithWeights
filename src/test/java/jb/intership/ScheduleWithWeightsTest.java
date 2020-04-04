package jb.intership;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ScheduleWithWeightsTest {
    private ScheduleWithWeights schedule;

    @Before
    public void setUp() throws Exception {
        schedule = new ScheduleWithWeights();
    }

    @Test
    public void calculateMaxIncome1() {
        schedule.addJob(1, 0, 3, 5);
        schedule.addJob(2, 9, 10, 5);
        schedule.addJob(3, 2, 4, 10);
        schedule.addJob(4, 3, 5, 7);
        schedule.addJob(5, 5, 8, 4);

        Pair<Integer, ArrayList<Integer>> result = schedule.maxIncomeAndSequence();
        assertEquals(Integer.valueOf(21), result.getKey());
        assertEquals(Arrays.asList(1, 4, 5, 2), result.getValue());
    }


    @Test
    public void calculateMaxIncome2() {
        schedule.addJob(1, 0, 3, 5);
        schedule.addJob(2, 9, 10, 5);
        schedule.addJob(3, 0, 10, 100);
        schedule.addJob(4, 3, 5, 7);
        schedule.addJob(5, 5, 8, 4);

        Pair<Integer, ArrayList<Integer>> result = schedule.maxIncomeAndSequence();
        assertEquals(Integer.valueOf(100), result.getKey());
        assertEquals(Arrays.asList(3), result.getValue());
    }


    @Test
    public void calculateMaxIncome3() {
        schedule.addJob(1, 0, 7, 5);
        schedule.addJob(2, 3, 5, 4);
        schedule.addJob(3, 4, 7, 7);
        schedule.addJob(4, 6, 10, 7);
        schedule.addJob(5, 5, 9, 4);

        Pair<Integer, ArrayList<Integer>> result = schedule.maxIncomeAndSequence();
        assertEquals(Integer.valueOf(11), result.getKey());
        assertEquals(Arrays.asList(2, 4), result.getValue());
    }
}