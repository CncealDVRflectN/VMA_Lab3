package by.vma.lab3;

public class Sweep {
    private static Matrix a;
    private static Vector f;
    private static Vector x;
    private static Vector r;
    private static Vector alpha;
    private static Vector beta;
    private static final int n = 5;

    public static void vma() {
        try {
            a = new Matrix(n, n);
            f = new Vector(n);
            a.fillDefault();
            if (isCorrect()) {
                System.out.println("Matrix A is correct.");
                f.fillDefault();
                fillAlpha();
                fillBeta();
                reversal();
                System.out.println("X: ");
                x.print(false);
                r = a.mul(x).subtract(f);
                System.out.println("R: ");
                r.print(true);
                System.out.println("||r|| = " + r.normI());
            } else {
                System.out.println("Matrix A is incorrect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fillAlpha() throws Exception {
        alpha = new Vector(n - 1);
        alpha.vector[0] = -a.matrix[0][1] / a.matrix[0][0];
        for (int i = 1; i < n - 1; i++) {
            alpha.vector[i] = -a.matrix[i][i + 1] / (a.matrix[i][i] - (-a.matrix[i][i - 1]) * alpha.vector[i - 1]);
        }
    }

    private static void fillBeta() throws Exception {
        beta = new Vector(n);
        beta.vector[0] = f.vector[0] / a.matrix[0][0];
        for (int i = 1; i < n; i++) {
            beta.vector[i] = f.vector[i] + (-a.matrix[i][i - 1]) * beta.vector[i - 1];
            beta.vector[i] /= (a.matrix[i][i] - (-a.matrix[i][i - 1]) * alpha.vector[i - 1]);
        }
    }

    private static void reversal() throws Exception {
        x = new Vector(n);
        x.vector[n - 1] = beta.vector[n - 1];
        for (int i = n - 2; i > -1; i--) {
            x.vector[i] = alpha.vector[i] * x.vector[i + 1] + beta.vector[i];
        }
    }

    private static boolean isCorrect() {
        if (Math.abs(a.matrix[0][0]) < Math.abs(a.matrix[0][1])) {
            return false;
        }
        if (Math.abs(a.matrix[n - 1][n - 1]) < Math.abs(a.matrix[n - 1][n - 2])) {
            return false;
        }
        for (int i = 1; i < n - 1; i++) {
            if (Math.abs(a.matrix[i][i]) < (Math.abs(a.matrix[i][i - 1]) + Math.abs(a.matrix[i][i + 1]))) {
                return false;
            }
        }
        return true;
    }
}
