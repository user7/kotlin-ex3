import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.ArrayList;

public class TaskR1_SumContest2 {
    int n;
    int rot = 0;
    long sum = 0;
    ArrayList<Long> data = new ArrayList<Long>();
    PrintStream output = null;

    void run(InputStream input, PrintStream output) {
        this.output = output;
        Scanner s = new Scanner(input);
        n = s.nextInt();
        for (int j = 0; j < n; ++j)
            addNum(s.nextLong());
        int q = s.nextInt();
        for (int j = 0; j < q; ++j) {
            int t = s.nextInt();
            if (t == 1) {
                int i = s.nextInt();
                long x = s.nextLong();
                replace(i, x);
            } else {
                rotate(s.nextInt());
            }
        }
    }

    void addNum(long v) {
        data.add(v);
        sum += v;
    }

    void replace(int i, long x) {
        int j = (rot + i - 1) % n;
        sum = sum - data.get(j) + x;
        data.set(j, x);
        out();
    }

    void rotate(int k) {
        rot = (rot + n - k) % n;
        out();
    }

    void out() {
        output.println(sum);
    }

    public static void main(String[] argv) {
        new TaskR1_SumContest2().run(System.in, System.out);
    }
}
