package hu.bme.mit.train.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.beans.Transient;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainUser;

public class TrainSensorTest {

    private TrainSensorImpl sensor;

    private TrainUser mockUser;
    private TrainController mockController; 
    

    @Before
    public void before() {
        mockUser = mock(TrainUser.class);
        mockController = mock(TrainController.class);
        this.sensor = new TrainSensorImpl(mockController, mockUser);
    }

    @Test
    public void AlarmTest_ValidSpeed_NoAlarm() {
        when(mockController.getReferenceSpeed()).thenReturn(50);
        
        sensor.overrideSpeedLimit(100);

        verify(mockUser, times(1)).setAlarmState(false);
    }

    @Test
    public void AlarmTest_NegativeSpeed_Alarm() {
        when(mockController.getReferenceSpeed()).thenReturn(50);
        
        sensor.overrideSpeedLimit(-100);

        verify(mockUser, times(1)).setAlarmState(true);
    }

    @Test
    public void AlarmTest_TooHighSpeed_Alarm(){
        when(mockController.getReferenceSpeed()).thenReturn(50);
        
        sensor.overrideSpeedLimit(501);

        verify(mockUser, times(1)).setAlarmState(true);
    }

    @Test
    public void AlarmTest_TooMuchDifference_Alarm(){
        when(mockController.getReferenceSpeed()).thenReturn(50);
        
        sensor.overrideSpeedLimit(10);

        verify(mockUser, times(1)).setAlarmState(true);
    }
}
