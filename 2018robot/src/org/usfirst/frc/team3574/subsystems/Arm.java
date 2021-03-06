package org.usfirst.frc.team3574.subsystems;

import org.usfirst.frc.team3574.commands.arm.ManualArm;
import org.usfirst.frc.team3574.enums.BrakePosition;
import org.usfirst.frc.team3574.enums.WristPosition;
import org.usfirst.frc.team3574.robot.Robot;
import org.usfirst.frc.team3574.robot.RobotMap;
import org.usfirst.frc.team3574.utilities.L;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class Arm extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	Solenoid Wrist = new Solenoid(RobotMap.WristSolenoid);
	Solenoid Brake = new Solenoid(RobotMap.BrakeSolenoid);
	TalonSRX ArmMotor = new TalonSRX(RobotMap.ArmMotor);
	DigitalInput zeroSwitch = new DigitalInput(RobotMap.ArmLimitSwitch);

	public static double ARM_MOTOR_ZERO_POINT;

	public static final int TICKS_PER_REVOLUTION = 4096;

	public static final double TICKS_PER_DEGREE = TICKS_PER_REVOLUTION  / 360;

	public static final int STARTING_POSITION = 88;
	public static final int CARRY_ANGLE = 150;
	public static final int SCALE_DELIVERY = 90; //degrees
	public static final int AUTO_SCALE_DELIVERY = 125; //degrees
	public static final int AUTO_SECOND_SCALE_DELIVERY = 125; //degrees // was 40, changing to 111 at GG
	public static final int SWITCH_DELIVERY = 21; //degrees
	public static final int AUTO_SWITCH_DELIVERY = 111; //degrees
	public static final int CUBE_PICKUP = 0; //degrees

	public final int timeoutMs = 50;
	public boolean armDoneMoving = false;

	//	public static final int kSlotIdx = 0;
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 100;
	public static boolean kSensorPhase = false;
	public static boolean kMotorInvert = false;

	public Arm() {

		
		ArmMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, kPIDLoopIdx, kTimeoutMs);
		
		ArmMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_8_PulseWidth, 15, 0);
		//
		ArmMotor.setSensorPhase(kSensorPhase);
		//
		//		ArmMotor.setInverted(kMotorInvert);
		//
		//		/* set the peak and nominal outputs, 12V means full */
		ArmMotor.configNominalOutputForward(0, kTimeoutMs);
		ArmMotor.configNominalOutputReverse(0, kTimeoutMs);
		ArmMotor.configPeakOutputForward(1, kTimeoutMs);
		ArmMotor.configPeakOutputReverse(-1, kTimeoutMs);
//		ArmMotor.configForwardSoftLimitEnable(true, 10);
//		ArmMotor.configReverseSoftLimitEnable(true, 10);
		//		ArmMotor.configAllowableClosedloopError(0, kPIDLoopIdx, kTimeoutMs);
		//
		//
		//		ArmMotor.config_kF(kPIDLoopIdx, 0.0, kTimeoutMs);
		//		ArmMotor.config_kP(kPIDLoopIdx, 0.7, kTimeoutMs);
		//		ArmMotor.config_kI(kPIDLoopIdx, 0.0, kTimeoutMs);
		//		ArmMotor.config_kD(kPIDLoopIdx, 0., kTimeoutMs);
		//
		//		/*
		//		 * lets grab the 360 degree position of the MagEncoder's absolute
		//		 * position, and intitally set the relative sensor to match.
		//		 */
		//		int absolutePosition = ArmMotor.getSensorCollection().getPulseWidthPosition();
		//		/* mask out overflows, keep bottom 12 bits */
		//		absolutePosition &= 0xFFF;
		//		if (kSensorPhase)
		//			absolutePosition *= -1;
		//		if (kMotorInvert)
		//			absolutePosition *= -1;
		//		/* set the quadrature (relative) sensor to match absolute */
		//		ArmMotor.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);
	}

	public double getEncPos() {
		return -(ArmMotor.getSensorCollection().getPulseWidthPosition() %  4096);
	}
	public double getAngleOfArm() {
		return ((ARM_MOTOR_ZERO_POINT - getEncPos()) / TICKS_PER_DEGREE);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ManualArm());
	}

	public void setBrakePosition(BrakePosition pos) {
		if(pos == BrakePosition.OPEN) {
			Brake.set(true);
		}
		else if(pos == BrakePosition.CLOSED){
			Brake.set(false);
		}
		else {
			System.out.println("Invalid SetBrakePosition Entry");
		}
	}

	public void setWristPosition(WristPosition pos) {
		if(pos == WristPosition.STRAIGHT) {
			Wrist.set(false);
		}
		else if(pos == WristPosition.ANGLED){
			Wrist.set(true);
		}
		else {
			System.out.println("Invalid SetWristParallel Entry");
		}
	}

	public void setSpeed(double motorSpeed){
		ArmMotor.set(ControlMode.PercentOutput, motorSpeed);
	}

	public void calibrateArmEncoder() {
		ARM_MOTOR_ZERO_POINT = getEncPos();
	}

	public void calibrateArmEncoderFromStarting() {
		ARM_MOTOR_ZERO_POINT = getEncPos() + ((STARTING_POSITION - 1) * TICKS_PER_DEGREE);
	}

	public boolean getLimitSwitch() {
		return !ArmMotor.getSensorCollection().isRevLimitSwitchClosed();
	}


	public void setCurrent(double current) {
		ArmMotor.set(ControlMode.Current, current);
	}

	public void setUpPIDPosition(TalonSRX _talon) {
		ArmMotor.configAllowableClosedloopError(0, kPIDLoopIdx, kTimeoutMs);


		ArmMotor.config_kF(kPIDLoopIdx, 0.0, kTimeoutMs);
		ArmMotor.config_kP(kPIDLoopIdx, 0.7, kTimeoutMs);
		ArmMotor.config_kI(kPIDLoopIdx, 0.0, kTimeoutMs);
		ArmMotor.config_kD(kPIDLoopIdx, 0., kTimeoutMs);

		/*
		 * lets grab the 360 degree position of the MagEncoder's absolute
		 * position, and intitally set the relative sensor to match.
		 */
		int absolutePosition = ArmMotor.getSensorCollection().getPulseWidthPosition();
		/* mask out overflows, keep bottom 12 bits */
		absolutePosition &= 0xFFF;
		if (kSensorPhase)
			absolutePosition *= -1;
		if (kMotorInvert)
			absolutePosition *= -1;
		/* set the quadrature (relative) sensor to match absolute */
		ArmMotor.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);

	}

	public void log() {
		SmartDashboard.putNumber("Arm Encoder", getEncPos());
		SmartDashboard.putNumber("Arm Zero Point (Not Really 0)", ARM_MOTOR_ZERO_POINT);
		L.ogSD("Arm Limit", ArmMotor.getSensorCollection().isRevLimitSwitchClosed());
		L.ogSD("Motor 5 voltage", ArmMotor.getMotorOutputVoltage());
		SmartDashboard.putNumber("Arm Angle", getAngleOfArm());
	}
}