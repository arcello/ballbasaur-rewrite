package org.usfirst.frc.team449.robot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.drive.unidirectional.DriveTalonCluster;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedDigitalInput;
import org.usfirst.frc.team449.robot.jacksonWrappers.MappedRunnable;
import org.usfirst.frc.team449.robot.jacksonWrappers.YamlCommand;
import org.usfirst.frc.team449.robot.oi.OI;
import org.usfirst.frc.team449.robot.oi.buttons.CommandButton;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.other.MotionProfileData;
import org.usfirst.frc.team449.robot.subsystem.complex.intake.IntakeFixedAndActuated;
import org.usfirst.frc.team449.robot.subsystem.complex.shooter.LoggingFlywheel;
import org.usfirst.frc.team449.robot.subsystem.interfaces.solenoid.SolenoidSimple;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.camera.CameraNetwork;
import org.usfirst.frc.team449.robot.subsystem.singleImplementation.pneumatics.Pneumatics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Jackson-compatible object representing the entire robot.
 */
public class RobotMap {
	/**
	 * The buttons for controlling this robot.
	 */
	@NotNull
	private final List<CommandButton> buttons;

	/**
	 * The OI for controlling this robot's drive.
	 */
	@NotNull
	private final OI oi;

	/**
	 * The logger for recording events and telemetry data.
	 */
	@NotNull
	private final Logger logger;

	/**
	 * The drive.
	 */
	@NotNull
	private final DriveTalonCluster drive;

	/**
	 * The command for the drive to run during the teleoperated period.
	 */
	@NotNull
	private final Command defaultDriveCommand;

	/**
	 * A runnable that updates cached variables.
	 */
	@NotNull
	private final Runnable updater;



	/**
	 * The pneumatics on this robot. Can be null.
	 */
	@Nullable
	private final Pneumatics pneumatics;


	/**
	 * The I2C port of the RIOduino plugged into this robot. Can be null.
	 */
	@Nullable
	private final Integer RIOduinoPort;

	/**
	 * The switch for selecting which alliance we're on. Can be null if doMP is false or testMP is true, but otherwise
	 * must have a value.
	 */
	@Nullable
	private final MappedDigitalInput allianceSwitch;

	/**
	 * The dial for selecting which side of the field the robot is on. Can be null if doMP is false or testMP is true,
	 * but otherwise must have a value.
	 */
	@Nullable
	private final MappedDigitalInput locationDial;

	/**
	 * The command to be run when first enabled in teleoperated mode.
	 */
	@Nullable
	private final Command teleopStartupCommand;

	/**
	 * The command to be run when first enabled.
	 */
	@Nullable
	private final Command startupCommand;

	/**
	 * Whether to run the test or real motion profile during autonomous.
	 */
	private final boolean testMP;

	/**
	 * Whether to run a motion profile during autonomous.
	 */
	private final boolean doMP;

	/**
	 * Default constructor.
	 *
	 * @param buttons              The buttons for controlling this robot. Can be null for an empty list.
	 * @param oi                   The OI for controlling this robot's drive.
	 * @param logger               The logger for recording events and telemetry data.
	 * @param drive                The drive.
	 * @param defaultDriveCommand  The command for the drive to run during the teleoperated period.
	 * @param updater              A runnable that updates cached variables.
	 * @param pneumatics           The pneumatics on this robot. Can be null.
	 * @param RIOduinoPort         The I2C port of the RIOduino plugged into this robot. Can be null.
	 * @param allianceSwitch       The switch for selecting which alliance we're on. Can be null if doMP is false or
	 *                             testMP is true, but otherwise must have a value.
	 * @param locationDial         The dial for selecting which side of the field the robot is on. Can be null if doMP
	 *                             is false or testMP is true, but otherwise must have a value.
	 * @param leftTestProfile      The profile for the left side of the drive to run in test mode. Can be null if either
	 *                             testMP or doMP are false, but otherwise must have a value.
	 * @param rightTestProfile     The profile for the right side of the drive to run in test mode. Can be null if
	 *                             either testMP or doMP are false, but otherwise must have a value.
	 * @param leftProfiles         The starting position to peg profiles for the left side. Should have options for
	 *                             "red_right", "red_center", "red_left", "blue_right", "blue_center", and "blue_left".
	 *                             Can be null if doMP is false or testMP is true, but otherwise must have a value.
	 * @param rightProfiles        The starting position to peg profiles for the right side. Should have options for
	 *                             "red_right", "red_center", "red_left", "blue_right", "blue_center", and "blue_left".
	 *                             Can be null if doMP is false or testMP is true, but otherwise must have a value.
	 * @param teleopStartupCommand The command to be run when first enabled in teleoperated mode.
	 * @param startupCommand       The command to be run when first enabled.
	 * @param testMP               Whether to run the test or real motion profile during autonomous. Defaults to false.
	 * @param doMP                 Whether to run a motion profile during autonomous. Defaults to true.
	 */
	@JsonCreator
	public RobotMap(@Nullable List<CommandButton> buttons,
	                    @NotNull @JsonProperty(required = true) OI oi,
	                    @NotNull @JsonProperty(required = true) Logger logger,
	                    @NotNull @JsonProperty(required = true) DriveTalonCluster drive,
	                    @NotNull @JsonProperty(required = true) YamlCommand defaultDriveCommand,
	                    @NotNull @JsonProperty(required = true) MappedRunnable updater,
	                    @Nullable IntakeFixedAndActuated intake,
	                    @Nullable Pneumatics pneumatics,
	                    @Nullable Integer RIOduinoPort,
	                    @Nullable MappedDigitalInput allianceSwitch,
	                    @Nullable MappedDigitalInput locationDial,
	                    @Nullable MotionProfileData leftTestProfile, @Nullable MotionProfileData rightTestProfile,
	                    @Nullable Map<String, MotionProfileData> leftProfiles, @Nullable Map<String, MotionProfileData> rightProfiles,
	                    @Nullable YamlCommand teleopStartupCommand,
	                    @Nullable YamlCommand startupCommand,
	                    boolean testMP,
	                    @Nullable Boolean doMP) {
		this.buttons = buttons != null ? buttons : new ArrayList<>();
		this.oi = oi;
		this.drive = drive;
		this.pneumatics = pneumatics;
		this.logger = logger;
		this.updater = updater;
		this.RIOduinoPort = RIOduinoPort;
		this.allianceSwitch = allianceSwitch;
		this.locationDial = locationDial;
		this.defaultDriveCommand = defaultDriveCommand.getCommand();
		this.teleopStartupCommand = teleopStartupCommand != null ? teleopStartupCommand.getCommand() : null;
		this.startupCommand = startupCommand != null ? startupCommand.getCommand() : null;
		this.testMP = testMP;
		this.doMP = doMP != null ? doMP : true;
	}

	/**
	 * @return The buttons for controlling this robot.
	 */
	@NotNull
	public List<CommandButton> getButtons() {
		return buttons;
	}

	/**
	 * @return The OI for controlling this robot's drive.
	 */
	@NotNull
	public OI getOI() {
		return oi;
	}

	/**
	 * @return The logger for recording events and telemetry data.
	 */
	@NotNull
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @return The drive.
	 */
	@NotNull
	public DriveTalonCluster getDrive() {
		return drive;
	}

	/**
	 * @return The command for the drive to run during the teleoperated period.
	 */
	@NotNull
	public Command getDefaultDriveCommand() {
		return defaultDriveCommand;
	}

	/**
	 * @return The pneumatics on this robot. Can be null.
	 */
	@Nullable
	public Pneumatics getPneumatics() {
		return pneumatics;
	}

	/**
	 * @return The I2C port of the RIOduino plugged into this robot. Can be null.
	 */
	@Nullable
	public Integer getRIOduinoPort() {
		return RIOduinoPort;
	}

	/**
	 * @return The switch for selecting which alliance we're on. Can be null if getDoMP returns false or getTestMP
	 * returns true, but otherwise has a value.
	 */
	@Nullable
	public MappedDigitalInput getAllianceSwitch() {
		return allianceSwitch;
	}

	/**
	 * @return The dial for selecting which side of the field the robot is on. Can be null if getDoMP returns false or
	 * getTestMP returns true, but otherwise has a value.
	 */
	@Nullable
	public MappedDigitalInput getLocationDial() {
		return locationDial;
	}

	/**
	 * @return Whether to run the test or real motion profile during autonomous.
	 */
	public boolean getTestMP() {
		return testMP;
	}

	/**
	 * @return The command to be run when first enabled in teleoperated mode.
	 */
	@Nullable
	public Command getTeleopStartupCommand() {
		return teleopStartupCommand;
	}

	/**
	 * @return Whether to run a motion profile during autonomous.
	 */
	public boolean getDoMP() {
		return doMP;
	}

	/**
	 * @return The command to be run when first enabled.
	 */
	@Nullable
	public Command getStartupCommand() {
		return startupCommand;
	}

	/**
	 * @return A runnable that updates cached variables.
	 */
	@NotNull
	public Runnable getUpdater() {
		return updater;
	}
}
