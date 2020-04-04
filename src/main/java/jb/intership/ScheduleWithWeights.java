package jb.intership;

import javafx.util.Pair;

import java.util.*;
/**
 * The ScheduleWithWeights class solves weighted intervals scheduling problem.
 * Complexity O(n*log(n))
*/
public class ScheduleWithWeights {
    /**
     * Jobs/Intervals container
     */
    private ArrayList<Job> jobs = new ArrayList<>();

    /**
     * Add new Job to schedule.
     * @param id identifier of job/interval
     * @param startTime start of job/interval
     * @param finishTime finish of job/interval
     * @param weight weight of job/interval
     */
    public void addJob(int id, int startTime, int finishTime, int weight) {
        jobs.add(new Job(id, startTime, finishTime, weight));
    }

    /**
     * Solve problem and return pair of maximum income and sequence of jobs identifiers.
     * Complexity(n*log(n))
     * Firstly it sorts jobs/intervals by finishTime in ASC order O(n*log(n)). Then it uses
     * dynamic programming with binary search to find maximum income which is stored in the
     * local array dp[jobs.size() - 1] O(n*log(n)).
     * @return pair of maximum income (Integer) and sequence of jobs identifiers (ArrayList<Integer>)
     */
    public Pair<Integer, ArrayList<Integer>> maxIncomeAndSequence() {
        if (jobs.isEmpty()) {
            return new Pair<>(0, null);
        }

        jobs.sort(new JobComparator());

        int[] dp = new int[jobs.size()];
        dp[0] = jobs.get(0).weight;
        for (int i = 1; i < jobs.size(); i++) {
            int weight = jobs.get(i).weight;
            int index = bsFindJob(i);

            if (index != -1) {
                weight += dp[index];
            }

            dp[i] = Math.max(weight, dp[i - 1]);
        }

        return new Pair<>(dp[jobs.size() - 1], findSequence(dp));
    }

    /**
     * Binary search for finding the latest non overlapping job/interval
     * for current job/interval.
     * Complexity O(log(n))
     * @param index index of current job/interval
     * @return index of the latest non overlapping job/interval for current job/interval,
     * if nothing found returns -1
     */
    private int bsFindJob(int index) {
        int l = 0;
        int r = index - 1;

        while (l <= r) {
            int mid = (l + r) / 2;
            if (jobs.get(mid).finishTime <= jobs.get(index).startTime) {
                if (jobs.get(mid + 1).finishTime <= jobs.get(index).startTime) {
                    l = mid + 1;
                } else {
                    return mid;
                }
            } else {
                r = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Find sequence of jobs identifiers for maximum income by using
     * the filled array from dynamic programming step.
     * Complexity O(n*log(n))
     * Start from the end of array. Find the latest non overlapping job/interval by using
     * binary search. Then check if job/interval was included in solution and change the current index
     * depending on this. Repeat until the index is out of range.
     * @param dp the filled array from dynamic programming step
     * @return sequence of jobs identifiers for maximum income
     */
    private ArrayList<Integer> findSequence(int[] dp) {
        ArrayList<Integer> resultIndexes = new ArrayList<>();
        int currIndex = jobs.size() - 1;

        while (currIndex > -1) {
            int indexLatestJob = bsFindJob(currIndex);
            int wFound = (indexLatestJob == -1) ? 0 : dp[indexLatestJob];
            int wWithout = (currIndex - 1 == -1) ? 0 : dp[currIndex - 1];

            if (jobs.get(currIndex).weight + wFound > wWithout) {
                resultIndexes.add(currIndex);
                currIndex = indexLatestJob;
            } else {
                currIndex--;
            }
        }

        ArrayList<Integer> result = new ArrayList<>();
        ListIterator<Integer> it = resultIndexes.listIterator(resultIndexes.size());
        while (it.hasPrevious()) {
            result.add(jobs.get(it.previous()).id);
        }
        return result;
    }

    /**
     * Nested class for storing all parameters of jobs/intervals
     */
    private static final class Job {
        final public int id;
        public final int startTime;
        public final int finishTime;
        public final int weight;

        private Job(int id, int startTime, int finishTime, int weight) {
            this.id = id;
            this.startTime = startTime;
            this.finishTime = finishTime;
            this.weight = weight;
        }
    }

    /**
     * Class Comparator for nested class Job.
     * Compares finishTime values in ASC order (<)
     */
    private static class JobComparator implements Comparator<Job> {
        @Override
        public int compare(Job lhs, Job rhs) {
            return Integer.compare(lhs.finishTime, rhs.finishTime);
        }
    }
}
