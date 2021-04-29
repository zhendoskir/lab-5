import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {
    public static final String NAME = "Tricorn";
    public static final int MAX_ITERATIONS = 2000;
    public static final double INITIAL_X = -2;
    public static final double INITIAL_Y = -2;
    public static final double INITIAL_WIDTH = 4;
    public static final double INITIAL_HEIGHT = 4;
    public static final double LIMIT = 2;

    public void getInitialRange (Rectangle2D.Double rectangle) {
        rectangle.x = INITIAL_X;
        rectangle.y = INITIAL_Y;
        rectangle.width = INITIAL_WIDTH;
        rectangle.height = INITIAL_HEIGHT;
    }

    public final int numIterations(final double x, final double y) {
        ComplexNumber z = new ComplexNumber(0, 0);
        ComplexNumber c = new ComplexNumber(x, y);

        int iter = 0;
        for (; iter < MAX_ITERATIONS; ++iter) {
            z = z.conjugate().mult(z.conjugate()).summ(c);
            if (z.absPow() > LIMIT * LIMIT) {
                break;
            }
        }

        if (iter == MAX_ITERATIONS) {
            return -1;
        } else {
            return iter;
        }
    }

    public String toString(){
        return NAME;
    }
}