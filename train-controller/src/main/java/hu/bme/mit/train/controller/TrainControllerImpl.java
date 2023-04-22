package hu.bme.mit.train.controller;

import hu.bme.mit.train.interfaces.TrainController;

import java.util.Timer;
import java.util.TimerTask;


public class TrainControllerImpl implements TrainController {

	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 0;

	private Timer timer;

	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			followSpeed();
		}
	};

	@Override
	public void followSpeed() {

		referenceSpeed += step;

		enforceSpeedLimit();
	}

	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();
		
	}

	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
		else if (referenceSpeed < -speedLimit) {
			referenceSpeed = -speedLimit;
		}
	}

	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;
		
		if(this.timer == null) {
			this.timer = new Timer();
			this.timer.schedule(task,0, 500);
		}
		
	}

	@Override
	public int getStep() {
		return step;
	}

}
