package org.usfirst.frc.team3574.commands.claw;

import org.usfirst.frc.team3574.commands.util.L;
import org.usfirst.frc.team3574.enums.ClawPosition;
import org.usfirst.frc.team3574.robot.Robot;
import org.usfirst.frc.team3574.subsystems.UtilitySubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetClawPosition extends Command {

	private ClawPosition pos;

	public SetClawPosition(ClawPosition position) {
		requires(Robot.claw);
		pos = position;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		L.ogInit(this);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {    
		UtilitySubsystem.armPositionPlacementForDropoff = 0;
		Robot.claw.setClawPosition(pos);
		System.out.println("SetClawPosition executes. going to: " + pos);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		System.out.println("SetClawPosition IsFinished");
		return true;
	}

	// Called once after isFinished returns true
	protected void end() {
		System.out.println("SetClawPosition ends. Time Since Initialized: " + timeSinceInitialized());
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		System.out.println("SetClawPosition Interrupted");
	}
}