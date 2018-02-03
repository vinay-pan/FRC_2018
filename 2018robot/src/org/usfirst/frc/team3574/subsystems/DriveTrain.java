package org.usfirst.frc.team3574.subsystems;

import org.usfirst.frc.team3574.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3574.commands.driveTrain.*;

/**
 *
 */
public class DriveTrain extends Subsystem {
	TalonSRX motorLeft1 = new TalonSRX(RobotMap.DriveTrainLeftTalon1);
	TalonSRX motorLeft2 = new TalonSRX(RobotMap.DriveTrainLeftTalon2);
	TalonSRX motorRight1 = new TalonSRX(RobotMap.DriveTrainRightTalon1);
	TalonSRX motorRight2 = new TalonSRX(RobotMap.DriveTrainRightTalon2);


	TalonSRX motorTest1 = new TalonSRX(RobotMap.LeftTalon3);
	TalonSRX motorTest2 = new TalonSRX(RobotMap.LeftTalon4);


	TalonSRX imuTalon = new TalonSRX (RobotMap.DriveTrainPenguin1);
	PigeonIMU penguin = new PigeonIMU (imuTalon);

	double kPgain = 0.04; /* percent throttle per degree of error */
	double kDgain = 0.0004; /* percent throttle per angular velocity dps */
	double kMaxCorrectionRatio = 0.30; /* cap corrective turning throttle to 30 percent of forward throttle */
	
	public double _currentAngleToPass;

	public DriveTrain() {
		// TODO Auto-generated constructor stub
		motorLeft1.set(ControlMode.PercentOutput, 0.0);
		motorLeft2.set(ControlMode.PercentOutput,  0.0);
		motorRight1.set(ControlMode.PercentOutput,  0.0);
		motorRight2.set(ControlMode.PercentOutput,  0.0);
	}
	public int getEncoderLeft()
	{
		return motorLeft1.getSensorCollection().getPulseWidthPosition();
	}

	public int getEncoderRight()
	{
		//		Value reversed for clarity
		return -motorRight1.getSensorCollection().getPulseWidthPosition();
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new DriveWithJoy());
	}

	//Drives the robot using s inputs for the left and right side motors.
	//Inputs are percentages of maxeperateimum motor output.
	public void driveByTank (double leftSpeed, double rightSpeed)	
	{
		motorLeft1.set(ControlMode.PercentOutput, leftSpeed);
		motorLeft2.set(ControlMode.PercentOutput, leftSpeed);

		motorRight1.set(ControlMode.PercentOutput, rightSpeed);
		motorRight2.set(ControlMode.PercentOutput, rightSpeed);
	}    

	//Controls speed and direction of the robot.
	// -1 = full reverse; 1 = full forward
	public void driveByArcadeWithModifiers (double percentThrottle, double percentRotationOutput)
	{

		percentThrottle = valueAfterDeadzoned(percentThrottle);
		percentRotationOutput = valueAfterDeadzoned(percentRotationOutput);

		percentThrottle = scalingSpeed(percentThrottle);
		percentRotationOutput = scalingSpeed(percentRotationOutput);

		SmartDashboard.putNumber("ACTUAL Percent Throttle", percentThrottle);
		SmartDashboard.putNumber("ACTUAL Percent Rotation", percentRotationOutput);

		this.driveByArcade(percentThrottle, percentRotationOutput);
	}

	public void driveByArcade (double percentThrottle, double percentRotationOutput) {

		motorLeft1.set(ControlMode.PercentOutput, percentThrottle - percentRotationOutput);
		motorLeft2.set(ControlMode.PercentOutput, percentThrottle - percentRotationOutput);

		motorRight1.set(ControlMode.PercentOutput, (percentThrottle + percentRotationOutput) * -1.0);
		motorRight2.set(ControlMode.PercentOutput, (percentThrottle + percentRotationOutput) * -1.0);		
	}

	public void testSomeFriedChicken() {
		double power = 0.2;
		motorTest1.set(ControlMode.PercentOutput, power);
		motorTest2.set(ControlMode.PercentOutput, power);
	}

	public double scalingSpeed (double joystickValue) {
		//		TODO: Find better scaling system
		//		Here's a simple algorithm to add sensitivity adjustment to your joystick:
		//
		//		x' = a * x^3 + (1-a) * x
		//
		//		x is a joystick output ranging from -1 to +1
		//
		//		x' is the sensitivity-adjusted output (also will be -1 to +1)
		//
		//		"a" is a variable ranging from 0 to +1
		//
		//		When a=0, you get x' = x
		//
		//		When a=1, you get x' = x^3 which gives very fine control of small outputs
		//
		//		When a is between 0 and 1, you get something in between.

		//		joystickValue is "x"

		//		below is "a"
		double scalingCutoff = .75;

		//		below is "x^3"
		double joystickValueToTheThird = Math.pow(joystickValue, 3);

		//		x'   = a             * x^3                     +  (1-a)             * x
		return scalingCutoff * joystickValueToTheThird + ((1-scalingCutoff) * joystickValue);
	}

	public void doNothing () 
	{
		driveByTank(0.0, 0.0);

	}

	private double valueAfterDeadzoned (double currentValue) {
		//		This is the deadzone. Change to change how sensitive the robot is.
		double deadzone = 0.2;
		if (Math.abs(currentValue) < deadzone)
		{
			return 0;
		}
		else
		{
			return currentValue;
		}
	}

	public double driveStraight(double forwardThrottle, double targetAngle) {
		//    	/* some temps for Pigeon API */
		PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();
		PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
		double [] xyz_dps = new double [3];
		
		//		/* grab some input data from Pigeon */
		penguin.getGeneralStatus(genStatus);
		penguin.getRawGyro(xyz_dps);
		penguin.getFusedHeading(fusionStatus);
		double currentAngle = fusionStatus.heading;
		double currentAngularRate = xyz_dps[2];
		double turnThrottle = (targetAngle - currentAngle) * kPgain - (currentAngularRate) * kDgain;
		
		/* the max correction is the forward throttle times a scaler,
		 * This can be done a number of ways but basically only apply small turning correction when we are moving slow
		 * and larger correction the faster we move.  Otherwise you may need stiffer pgain at higher velocities. */
		double maxThrot = MaxCorrection(forwardThrottle, kMaxCorrectionRatio);
		//System.out.println("Before Cap " + turnThrottle);
		turnThrottle = Cap(turnThrottle, maxThrot);
		//System.out.println("After Cap " + turnThrottle);
		return turnThrottle;
	}

	private double Cap(double value, double peak) {
		if (value < -peak)
			return -peak;
		if (value > +peak)
			return +peak;
		return value;
	}

	private double MaxCorrection(double forwardThrot, double scalor) {
		/* make it positive */
		if(forwardThrot < 0) {forwardThrot = -forwardThrot;}
		/* max correction is the current forward throttle scaled down */
		forwardThrot *= scalor;
		/* ensure caller is allowed at least 10% throttle,
		 * regardless of forward throttle */
		if(forwardThrot < 0.10)
			return 0.10;
		return forwardThrot;
	}

	public void log() {
		PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();
		PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
		double [] xyz_dps = new double [3];
		/* grab some input data from Pigeon and gamepad*/
		penguin.getGeneralStatus(genStatus);
		penguin.getRawGyro(xyz_dps);
		penguin.getFusedHeading(fusionStatus);
		double currentAngle = fusionStatus.heading;
		_currentAngleToPass = currentAngle;
		boolean angleIsGood = (penguin.getState() == PigeonIMU.PigeonState.Ready) ? true : false;
		double currentAngularRate = xyz_dps[2];
		SmartDashboard.putNumber("angle", currentAngle);
		SmartDashboard.putNumber("Encoder Right", this.getEncoderRight());
		SmartDashboard.putNumber("Encoder Left", this.getEncoderLeft());
	}
}