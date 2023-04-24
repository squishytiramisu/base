package hu.bme.mit.train.sensor;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;

//import guava table
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Date;

public class TrainSensorImpl implements TrainSensor {

	private TrainController controller;
	private TrainUser user;
	private int speedLimit = 5;
	private Table<Date, Integer, Integer> logTable;

	public TrainSensorImpl(TrainController controller, TrainUser user) {
		this.controller = controller;
		this.user = user;
		this.logTable = HashBasedTable.create();
	}

	@Override
	public int getSpeedLimit() {
		return speedLimit;
	}

	@Override
	public void overrideSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		controller.setSpeedLimit(speedLimit);
		
		if(speedLimit < 0 || speedLimit > 500) {
			user.setAlarmState(true);
		}else if(speedLimit < controller.getReferenceSpeed() * 0.5) {
			user.setAlarmState(true);
		}else{
			user.setAlarmState(false);
		}
	}

	@Override
	public void logInfo() {
		logTable.put(new Date(), controller.getStep(), controller.getReferenceSpeed());
	}

	@Override
	public Table<Date, Integer, Integer> getLogTable() {
		return logTable;
	}

}
