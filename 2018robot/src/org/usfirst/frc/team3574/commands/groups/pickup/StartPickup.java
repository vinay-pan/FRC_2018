package org.usfirst.frc.team3574.commands.groups.pickup;

import org.usfirst.frc.team3574.commands.arm.DownUntilClicked;
import org.usfirst.frc.team3574.commands.arm.SetArmPosition;
import org.usfirst.frc.team3574.commands.arm.SetWristPosition;
import org.usfirst.frc.team3574.commands.arm.UpUntilUnclicked;
import org.usfirst.frc.team3574.commands.claw.SetClawPosition;
import org.usfirst.frc.team3574.commands.driveTrain.DriveByInches;
import org.usfirst.frc.team3574.commands.driveTrain.DriveWithJoy;
import org.usfirst.frc.team3574.commands.sensors.UntilBothSensorsAreTripped;
import org.usfirst.frc.team3574.commands.slide.SetSlidePosition;
import org.usfirst.frc.team3574.enums.ClawPosition;
import org.usfirst.frc.team3574.enums.WristPosition;
import org.usfirst.frc.team3574.robot.Robot;
import org.usfirst.frc.team3574.subsystems.Arm;
import org.usfirst.frc.team3574.subsystems.Slide;
import org.usfirst.frc.team3574.utilities.ArmSpeedSettingsWithoutCube;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class StartPickup extends CommandGroup {

	public StartPickup() {
		 /*
		 * TO PICK UP a cube.
		 * the forearm section will be located 19 degrees down from a straight out position... or 109 degrees from a straight up position.  (not sure where you are making 0 reference.
		 * The wrist will bend down 71.05 degrees from a straight in line (straight out along the forearm) position.  This won't be a critical to remembers as the pneumatic will put it there when out.
		 * The center of the shoulder (which makes the arm move in a circle) will be at 21.75 inches off the ground.  
		 */
		addSequential(new DriveByInches(Robot.driveTrain.backupDistancePickupStart, 0.2));
		addSequential(new SetClawPosition(ClawPosition.RELEASE));
		addSequential(new SetWristPosition(WristPosition.ANGLED));
//		addSequential(new SetSlidePosition(Slide.SLIDE_BOTTOM));
		addSequential(new DownUntilClicked(new ArmSpeedSettingsWithoutCube(), 1, true));
		addSequential(new UpUntilUnclicked(new ArmSpeedSettingsWithoutCube(), 1));
		addSequential(new DownUntilClicked(new ArmSpeedSettingsWithoutCube(), 1, false));
	}
}