package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PositionVectorTest {

    @Test
    void vectorLength_ZeroVector_Zero() {
        PositionVector ZERO_VECTOR = new PositionVector(0, 0);
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
}