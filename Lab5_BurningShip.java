import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {
    public static final String NAME = "Burning Ship";
    public static final int MAX_ITERATIONS = 2000;
    public static final double INITIAL_X = -2;
    public static final double INITIAL_Y = -2.5;
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
        ComplexNumber p = new ComplexNumber(0, 0);

        int iter = 0;
        for (; iter < MAX_ITERATIONS; ++iter) {
            p = new ComplexNumber(z.getRealAbs(), z.getfakeAbs());
            z = p.mult(p).summ(c);
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