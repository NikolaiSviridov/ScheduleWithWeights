package jb.intership;

import javafx.util.Pair;

import java.util.*;

public class ScheduleWithWeights {
    private ArrayList<Job> jobs;

    ScheduleWithWeights() {
        jobs = new ArrayList<>();
    }

    public void addJob(int id, int start_time, int finish_time, int weight) {
        jobs.add(new Job(id, start_time, finish_time, weight));
    }

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

    private ArrayList<Integer> findSequence(int[] dp) {
        ArrayList<Integer> resultIndexes = new ArrayList<>();
        int i = jobs.size() - 1;

        while (i > -1) {
            int index = bsFindJob(i);
            int wFound = (index == -1) ? 0 : dp[index];
            int wWithout = (i - 1 == -1) ? 0 : dp[i - 1];

            if (jobs.get(i).weight + wFound > wWithout) {
                resultIndexes.add(i);
                i = index;
            } else {
                i--;
            }
        }

        ArrayList<Integer> result = new ArrayList<>();
        ListIterator<Integer> it = resultIndexes.listIterator(resultIndexes.size());
        while (it.hasPrevious()) {
            result.add(jobs.get(it.previous()).id);
        }
        return result;
    }

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

    private static class JobComparator implements Comparator<Job> {
        @Override
        public int compare(Job lhs, Job rhs) {
            return Integer.compare(lhs.finishTime, rhs.finishTime);
        }
    }
}
