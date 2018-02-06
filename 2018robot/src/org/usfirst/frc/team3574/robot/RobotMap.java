/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3574.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public final class RobotMap {
	public static int ShifterSolenoid = 9991;
	public static int ClawSolenoid = 9996;
	
	public static int ArmMotor = 9992;
	public static int LiftMotor = 9993;
	public static int LeftWingReleaseMotor = 9994;
	public static int RightWingReleaseMotor = 9995;
	public static int DriveTrainRightTalon1 = 13;
	public static int DriveTrainRightTalon2 = 12;
	public static int DriveTrainLeftTalon1 = 1;
	public static int DriveTrainLeftTalon2 = 2;
	public static int DriveTrainPenguin1 = 3;
	
	public static int LeftTalon3 = 3;
	public static int LeftTalon4 = 4;
}