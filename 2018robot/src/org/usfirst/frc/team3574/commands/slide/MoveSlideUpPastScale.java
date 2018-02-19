package org.usfirst.frc.team3574.commands.slide;

import org.usfirst.frc.team3574.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveSlideUpPastScale extends Command {

	private boolean _hasSeenScale = false;

	public MoveSlideUpPastScale() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.slide);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		_hasSeenScale = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {	
		Robot.slide.setSlideSpeed(0.2);
		if (Robot.slide.isScaleDetectorTripped()) {
			_hasSeenScale = true;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (_hasSeenScale && !Robot.slide.isScaleDetectorTripped()) {
			return true;
		}
		else {
			return false;
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.slide.setSlideSpeed(0.0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}