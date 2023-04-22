package hu.bme.mit.train.system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import hu.bme.mit.train.system.TrainSystem;

import static java.lang.Thread.sleep;


public class TrainSystemTest {

	TrainController controller;
	TrainSensor sensor;
	TrainUser user;
	
	@Before
	public void before() {
		TrainSystem system = new TrainSystem();
		controller = system.getController();
		sensor = system.getSensor();
		user = system.getUser();

		sensor.overrideSpeedLimit(50);
	}
	
	@Test
	public void OverridingJoystickPosition_IncreasesReferenceSpeed() {
		sensor.overrideSpeedLimit(10);

		Assert.assertEquals(0, controller.getReferenceSpeed());
		
		user.overrideJoystickPosition(5);

		controller.followSpeed();
		Assert.assertEquals(5, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());
	}

	@Test
	public void OverridingJoystickPositionToNegative_SetsReferenceSpeedToNegative() {
		user.overrideJoystickPosition(4);
		controller.followSpeed();
		user.overrideJoystickPosition(-5);
		controller.followSpeed();
		controller.followSpeed();
		Assert.assertEquals(-6, controller.getReferenceSpeed());
	}

	@Test
	public void OverridingJoystickPositionToNegative_SetsReferenceSpeedToMin() {
		
		sensor.overrideSpeedLimit(10);
		Assert.assertEquals(0, controller.getReferenceSpeed());
		
		user.overrideJoystickPosition(-5);
		controller.followSpeed();

		Assert.assertEquals(-5, controller.getReferenceSpeed());
		controller.followSpeed();

		Assert.assertEquals(-10, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(-10, controller.getReferenceSpeed());
	}

	@Test
	public void TestTechnoGraph() {
		sensor.overrideSpeedLimit(10);
		user.overrideJoystickPosition(5);

		sensor.logInfo();

		controller.followSpeed();
		controller.followSpeed();

		sensor.logInfo();

		Assert.assertEquals(2, sensor.getLogTable().size());
	}

	@Test
	public void TestAutomaticReferenceSpeedChange() throws InterruptedException{
		sensor.overrideSpeedLimit(1000);

		Assert.assertEquals(0, controller.getReferenceSpeed());

		user.overrideJoystickPosition(50);
		sleep(1100);

		int refSpeed = controller.getReferenceSpeed();
		Assert.assertNotEquals(0, refSpeed);

		sleep(1100);

		Assert.assertTrue(refSpeed < controller.getReferenceSpeed());
	}
	
}
