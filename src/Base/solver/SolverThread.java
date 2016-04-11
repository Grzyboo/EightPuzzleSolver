package Base.solver;

import Base.MainWindow;

import java.util.LinkedList;

public class SolverThread implements Runnable {
    private Thread thread;
    private Solver solver;

    private MainWindow window;

    public SolverThread(MainWindow window, Solver solver) {
        this.solver = solver;
        this.window = window;
    }

    public void activate() {
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void run() {
        LinkedList<Movement> list = solver.getSolution();
        window.receiveSolution(list);
    }
}
