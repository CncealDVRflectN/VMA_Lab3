package by.vma.lab3;

public class Main {
    private static class Matrix {
        public double[][] matrix;
        private int lines;
        private int columns;

        public Matrix(int lines, int columns) throws Exception {
            if (lines < 1 || columns < 1) {
                throw new Exception("Неверный размер.");
            }
            this.lines = lines;
            this.columns = columns;
            this.matrix = new double[lines][columns];
        }

        public void print() {
            for (double[] i : matrix) {
                for (double j : i) {
                    System.out.printf("%.5f", j);
                    System.out.print("  ");
                }
                System.out.println();
            }
        }

        public void fillDefault() {
            double[][] a = {{0.6444, 0.0000, 0.0000, 0.0000, 0.0000},
                    {-0.0395, 0.4208, 0.0000, 0.0000, 0.0000},
                    {0.0000, -0.1184, 0.7627, 0.0145, 0.0000},
                    {0.0000, 0.0000, -0.0960, 0.7627, 0.0000},
                    {0.0000, 0.0000, 0.0000, -0.0158, 0.5523}};
            this.lines = 5;
            this.columns = 5;
            this.matrix = a;
        }

        public Vector mul(Vector vector) throws Exception {
            if (columns != vector.getLength()) {
                throw new Exception("Неверная матрица или вектор.");
            }
            Vector result = new Vector(vector.getLength());
            for (int i = 0; i < lines; i++) {
                result.vector[i] = 0;
                for (int j = 0; j < columns; j++) {
                    result.vector[i] += matrix[i][j] * vector.vector[j];
                }
            }
            return result;
        }
    }

    private static class Vector {
        public double[] vector;
        private int length;

        public Vector(int length) throws Exception {
            if (length < 1) {
                throw new Exception("Неверный размер.");
            }
            this.length = length;
            vector = new double[length];
        }

        public int getLength() {
            return length;
        }

        public void print(boolean exponent) {
            for (double item : vector) {
                if (exponent) {
                    System.out.printf("%e\n", item);
                } else {
                    System.out.printf("%.5f\n", item);
                }
            }
        }

        public void fillDefault() {
            double[] b = {1.2677, 1.6819, -2.3657, -6.5369, 2.8351};
            this.length = 5;
            this.vector = b;
        }

        public Vector subtract(Vector sub) throws Exception {
            if (length != sub.getLength()) {
                throw new Exception("Неверный размер.");
            }
            Vector result = new Vector(length);
            for (int i = 0; i < length; i++) {
                result.vector[i] = this.vector[i] - sub.vector[i];
            }
            return result;
        }

        public double normI() {
            double max = Math.abs(vector[0]);
            for (int i = 1; i < length; i++) {
                if (Math.abs(vector[i]) > max) {
                    max = Math.abs(vector[i]);
                }
            }
            return max;
        }
    }

    private static Matrix a;
    private static Vector f;
    private static Vector x;
    private static Vector alpha;
    private static Vector beta;
    private static final int n = 5;

    public static void main(String[] args) {
        Vector r;
        try {
            a = new Matrix(n, n);
            f = new Vector(n);
            a.fillDefault();
            a.print();
            if (isCorrect()) {
                System.out.println("Матрица A корректна.");
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
                System.out.println("Матрица A некорректна.");
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
