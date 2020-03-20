package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PositionVectorTest {
    public static final PositionVector ZERO_VECTOR = new PositionVector(0, 0);
    public static final PositionVector ARBITRARY_LEGAL_VECTOR = new PositionVector(Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final PositionVector UNIT_VECTOR_X = new PositionVector(1, 0);
    public static final PositionVector UNIT_VECTOR_Y = new PositionVector(0, 1);
    public static final PositionVector UNIT_VECTOR_NEGATIVE_X = new PositionVector(-1, 0);
    public static final PositionVector UNIT_VECTOR_NEGATIVE_Y = new PositionVector(0, -1);

    @Test
    void vectorLength_ZeroVector_Zero() {
        Assertions.assertEquals(0, PositionVector.vectorLength(ZERO_VECTOR));
    }

    @Test
    void vectorLength_UnitVectorX_One() {
        PositionVector UNIT_VECTOR = new PositionVector(1, 0);
        Assertions.assertEquals(1, PositionVector.vectorLength(UNIT_VECTOR));
    }

    @Test
    void vectorLength_UnitVectorNegativeX_One() {
        PositionVector UNIT_VECTOR = new PositionVector(-1, 0);
        Assertions.assertEquals(1, PositionVector.vectorLength(UNIT_VECTOR));
    }

    @Test
    void vectorLength_UnitVectorY() {
        PositionVector UNIT_VECTOR = new PositionVector(0, 1);
        Assertions.assertEquals(1, PositionVector.vectorLength(UNIT_VECTOR));
    }

    @Test
    void vectorLength_UnitVectorNegativeY_One() {
        PositionVector UNIT_VECTOR = new PositionVector(0, -1);
        Assertions.assertEquals(1, PositionVector.vectorLength(UNIT_VECTOR));
    }

    @Test
    void vectorLength_TimesUnitVectorX_One() {
        int TIMES = 30;
        PositionVector TIMES_UNIT_VECTOR = new PositionVector(TIMES, 0);
        Assertions.assertEquals(TIMES, PositionVector.vectorLength(TIMES_UNIT_VECTOR));
    }

    @Test
    void vectorLength_TimesUnitVectorY_One() {
        int TIMES = 30;
        PositionVector TIMES_UNIT_VECTOR = new PositionVector(0, TIMES);
        Assertions.assertEquals(TIMES, PositionVector.vectorLength(TIMES_UNIT_VECTOR));
    }

    @Test
    void vectorLength_OneOneVector_SqrtTwo() {
        PositionVector UNIT_VECTOR = new PositionVector(1, 1);
        Assertions.assertEquals(Math.sqrt(2), PositionVector.vectorLength(UNIT_VECTOR));
    }

    @Test
    void calculateAngle_BothZero_IllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> PositionVector.calculateAngle(ZERO_VECTOR, ZERO_VECTOR));
    }

    @Test
    void calculateAngle_FirstZero_IllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> PositionVector.calculateAngle(ZERO_VECTOR, ARBITRARY_LEGAL_VECTOR));
    }

    @Test
    void calculateAngle_SecondZero_IllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> PositionVector.calculateAngle(ARBITRARY_LEGAL_VECTOR, ZERO_VECTOR));
    }

    @Test
    void calculateAngle_UnitVectorsSameDirectionX_Zero() {
        Assertions.assertEquals(0.0, PositionVector.calculateAngle(UNIT_VECTOR_X, UNIT_VECTOR_X));
    }

    @Test
    void calculateAngle_UnitVectorsSameDirectionNegativeX_Zero() {
        Assertions.assertEquals(0.0, PositionVector.calculateAngle(UNIT_VECTOR_NEGATIVE_X, UNIT_VECTOR_NEGATIVE_X));
    }

    @Test
    void calculateAngle_UnitVectorsSameDirectionY_Zero() {
        Assertions.assertEquals(0.0, PositionVector.calculateAngle(UNIT_VECTOR_Y, UNIT_VECTOR_Y));
    }

    @Test
    void calculateAngle_UnitVectorsSameDirectionNegativeY_Zero() {
        Assertions.assertEquals(0.0, PositionVector.calculateAngle(UNIT_VECTOR_NEGATIVE_Y, UNIT_VECTOR_NEGATIVE_Y));
    }
}