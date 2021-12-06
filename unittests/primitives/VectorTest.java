package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Vector class
 */
class VectorTest {
    /**
     * Test method for {@link Vector#crossProduct(Vector)}
     */
    @Test
    void crossProduct() {

        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);

        // ============ Equivalence Partitions Tests ==============
        Vector v3 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v3);

        // Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
        assertEquals(v1.length() * v3.length(), vr.length(), 0.00001, "crossProduct() wrong result length");

        // Test cross-product result orthogonality to its operands
        assertTrue(Util.isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to 1st operand");
        assertTrue(Util.isZero(vr.dotProduct(v3)), "crossProduct() result is not orthogonal to 2nd operand");

        // =============== Boundary Values Tests ==================
        // test zero vector from cross-productof co-lined vectors
        try {
            v1.crossProduct(v2);
            fail("crossProduct() for parallel vectors does not throw an exception");
        } catch (Exception e) {
        }
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}
     */
    @Test
    void dotProduct() {
        Vector vec1 = new Vector(1, 2, 3);
        Vector vec2 = new Vector(2, 3, 4);
        double result = vec1.dotProduct(vec2);
        assertEquals(result, 20);

    }

    /**
     * Test method for {@link Vector#subtract(Vector)}
     */
    @Test
    void subtract() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1.0, 1.0, 1.0);
        Vector v2 = new Vector(-1.0, -1.0, -1.0);

        v1 = v1.subtract(v2);
        assertEquals(new Vector(2.0, 2.0, 2.0), v1);

        v2 = v2.subtract(v1);
        assertEquals(new Vector(-3.0, -3.0, -3.0), v2);
        // =============== Boundary Values Tests ==================
        // test result zero vector from subtract two vectors
        Vector v3 = new Vector(1.0, 1.0, 1.0);
        Vector v4 = new Vector(1.0, 1.0, 1.0);
        try {
            v3.subtract(v4);
            fail("Didn't throw zero vector exception!");
        } catch (IllegalArgumentException ex) {
            assertEquals("Vector Zero is not valid for head", ex.getMessage());
        }
    }

    /**
     * Test method for {@link Vector#add(Vector)}
     */
    @Test
    void add() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1.0, 1.0, 1.0);
        Vector v2 = new Vector(-1.0, -1.0, -1.5);

        v1 = v1.add(v2);
        assertEquals(new Vector(0.0, 0.0, -0.5), v1);

        v2 = v2.add(v1);
        assertEquals(new Vector(-1.0, -1.0, -2.0), v2);
        // =============== Boundary Values Tests ==================
        // test result zero vector from add two vectors
        Vector v3 = new Vector(1.0, 1.0, 1.0);
        Vector v4 = new Vector(-1.0, -1.0, -1.0);
        try {
            v3.add(v4);
            fail("Didn't throw zero vector exception!");
        } catch (IllegalArgumentException ex) {
            assertEquals("Vector Zero is not valid for head", ex.getMessage());
        }
    }

    /**
     * Test method for {@link Vector#scale(double)}
     */
    @Test
    void scale() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1.0, 1.0, 1.0);

        v1 = v1.scale(1);
        assertEquals(new Vector(1.0, 1.0, 1.0), v1);

        v1 = v1.scale(2);
        assertEquals(new Vector(2.0, 2.0, 2.0), v1);

        v1 = v1.scale(-2);
        assertEquals(new Vector(-4.0, -4.0, -4.0), v1);
// =============== Boundary Values Tests ==================
        // test result zero vector from scaling a vector

        try {
            v1.scale(0);
            fail("Didn't throw zero vector exception!");
        } catch (IllegalArgumentException ex) {
            assertEquals("Vector Zero is not valid for head", ex.getMessage());
        }
    }

    /**
     * Test method for {@link Vector#length()}
     */
    @Test
    void length() {
        Vector vec = new Vector(1, 2, 3);
        double result = vec.length();
        double expResult = Math.sqrt(14);
        assertEquals(result, expResult);

    }

    /**
     * Test method for {@link Vector#normalize()}
     */
    @Test
    void normalize() {
        Vector v = new Vector(3.5, -5, 10);
        v.normalize();
        assertEquals(1, v.length(), 1e-10);

        try {
            Vector v1 = new Vector(0, 0, 0);
            v1.normalize();
            fail("Didn't throw divide by zero exception!");
        } catch (IllegalArgumentException ex) {
            assertEquals("Vector Zero is not valid for head", ex.getMessage());
        }
    }
}
