package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    @Test
    public void getFinishDirectionUnitVector_FinishUp(){
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1,1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_UP);
        Assertions.assertEquals(testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION),PositionVector.Direction.UP.vector);
    }

    @Test
    public void getFinishDirectionUnitVector_FinishDown(){
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1,1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_DOWN);
        Assertions.assertEquals(testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION),PositionVector.Direction.DOWN.vector);
    }

    @Test
    public void getFinishDirectionUnitVector_FinishLeft(){
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1,1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_LEFT);
        Assertions.assertEquals(testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION),PositionVector.Direction.LEFT.vector);
    }

    @Test
    public void getFinishDirectionUnitVector_FinishRight(){
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1,1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_RIGHT);
        Assertions.assertEquals(testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION),PositionVector.Direction.RIGHT.vector);
    }

    @Test
    public void getFinishDirectionUnitVector_PositionIsTrack(){
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1,1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.TRACK);
        Assertions.assertEquals(testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION),new PositionVector(0,0));
    }

    @Test
    public void getFinishDirectionUnitVector_PositionIsWall(){
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1,1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.WALL);
        Assertions.assertEquals(testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION),new PositionVector(0,0));
    }

    @Test
    public void getFinishDirectionUnitVector_NoSuchPosition(){
        PositionVector ARBITRARY_INVALID_FINISH_POSITION = new PositionVector(Integer.MIN_VALUE,Integer.MIN_VALUE);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        Assertions.assertEquals(testedGame.getFinishDirectionUnitVector(ARBITRARY_INVALID_FINISH_POSITION),new PositionVector(0,0));
    }

    @Test
    public void switchToNextActiveCar_FIVE_CARS(){
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
      //  when(mockedTrack.)
    }
}
