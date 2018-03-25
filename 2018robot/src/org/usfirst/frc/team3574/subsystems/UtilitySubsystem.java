package org.usfirst.frc.team3574.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class UtilitySubsystem extends Subsystem {
	
	public static int armPositionPlacementForDropoff;

    public void initDefaultCommand() {
    }
    
    public void log() {
    	SmartDashboard.putNumber("arm Position Placement For Switch Dropoff", armPositionPlacementForDropoff);
    }
}