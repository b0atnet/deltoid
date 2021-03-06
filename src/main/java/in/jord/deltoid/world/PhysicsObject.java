package in.jord.deltoid.world;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.jord.deltoid.utils.MathUtilities;
import in.jord.deltoid.vector.Rotation;
import in.jord.deltoid.vector.Vec3;

public class PhysicsObject {
    public static final PhysicsObject ORIGIN = new PhysicsObject();
    /**
     * The <b>previous position</b> of the {@link PhysicsObject}.
     *
     * @serial
     */
    @JsonProperty("previous_position")
    private Vec3 previousPosition;

    /**
     * The <b>current position</b> of the {@link PhysicsObject}.
     *
     * @serial
     */
    @JsonProperty("position")
    private Vec3 position;

    /**
     * The <b>previous rotation</b> of the {@link PhysicsObject}.
     *
     * @serial
     */
    @JsonProperty("previous_rotation")
    private Rotation previousRotation;

    /**
     * The <b>current rotation</b> of the {@link PhysicsObject}.
     *
     * @serial
     */
    @JsonProperty("rotation")
    private Rotation rotation;

    /**
     * The <b>velocity</b> of the {@link PhysicsObject}.
     *
     * @serial
     */
    @JsonProperty("velocity")
    private Vec3 velocity;

    /**
     * The <b>constant acceleration</b> of the {@link PhysicsObject}.
     *
     * @serial
     */
    @JsonProperty("acceleration")
    private Vec3 acceleration;

    /**
     * The <b>mass</b> of the {@link PhysicsObject}.
     *
     * @serial
     */
    @JsonProperty("mass")
    private double mass;

    /**
     * Constructs a newly allocated {@link PhysicsObject} object.
     *
     * @param initialPosition the initial location for the {@link PhysicsObject}.
     * @param initialRotation the initial rotation for the {@link PhysicsObject}.
     * @param velocity        the initial velocity for the {@link PhysicsObject}.
     * @param acceleration    the initial acceleration for the {@link PhysicsObject}.
     */
    public PhysicsObject(Vec3 initialPosition, Rotation initialRotation, Vec3 velocity, Vec3 acceleration) {
        this.previousPosition = this.position = initialPosition;
        this.previousRotation = this.rotation = initialRotation;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    /**
     * Constructs a newly allocated {@link PhysicsObject} object,
     * with an initial <b>rotation</b> of <b>0.0</b>.
     *
     * @param initialPosition the initial location for the {@link PhysicsObject}.
     * @param velocity        the initial velocity for the {@link PhysicsObject}.
     * @param acceleration    the initial acceleration for the {@link PhysicsObject}.
     */
    public PhysicsObject(Vec3 initialPosition, Vec3 velocity, Vec3 acceleration) {
        this(initialPosition, Rotation.ORIGIN, velocity, acceleration);
    }

    /**
     * Constructs a newly allocated {@link PhysicsObject} object,
     * with an initial <b>acceleration</b> of <b>0.0</b>.
     *
     * @param initialPosition the initial location for the {@link PhysicsObject}.
     * @param initialRotation the initial rotation for the {@link PhysicsObject}.
     * @param velocity        the initial velocity for the {@link PhysicsObject}.
     */
    public PhysicsObject(Vec3 initialPosition, Rotation initialRotation, Vec3 velocity) {
        this(initialPosition, initialRotation, velocity, Vec3.ORIGIN);
    }

    /**
     * Constructs a newly allocated {@link PhysicsObject} object,
     * with an initial <b>velocity</b> and <b>acceleration</b> of <b>0.0</b>.
     *
     * @param initialPosition the initial location for the {@link PhysicsObject}.
     * @param initialRotation the initial rotation for the {@link PhysicsObject}.
     */
    public PhysicsObject(Vec3 initialPosition, Rotation initialRotation) {
        this(initialPosition, initialRotation, Vec3.ORIGIN, Vec3.ORIGIN);
    }

    /**
     * Constructs a newly allocated {@link PhysicsObject} object,
     * with an initial <b>rotation</b>, <b>velocity</b>,
     * and <b>acceleration</b> of <b>0.0</b>.
     *
     * @param initialPosition the initial location for the {@link PhysicsObject}.
     */
    public PhysicsObject(Vec3 initialPosition) {
        this(initialPosition, Rotation.ORIGIN, Vec3.ORIGIN, Vec3.ORIGIN);
    }

    /**
     * Constructs a newly allocated {@link PhysicsObject} object,
     * with an initial <b>position</b>, <b>rotation</b>, <b>velocity</b>,
     * and <b>acceleration</b> of <b>0.0</b>.
     */
    public PhysicsObject() {
        this(Vec3.ORIGIN, Rotation.ORIGIN, Vec3.ORIGIN, Vec3.ORIGIN);
    }

    /**
     * Returns a {@link Vec3} of the weighted average of the
     * <b>previous</b> and <b>current</b> positions, using a weight of <b>ratio</b>.
     *
     * @param ratio the weight for the weighted average.
     * @return the interpolated {@link Vec3}
     */
    public Vec3 getRenderPosition(double ratio) {
        return MathUtilities.interpolate(this.previousPosition, this.position, ratio);
    }

    /**
     * Returns a {@link Rotation} of the weighted average of the
     * <b>previous</b> and <b>current</b> rotations, using a weight of <b>ratio</b>.
     *
     * @param ratio the weight for the weighted average.
     * @return the interpolated {@link Rotation}
     */
    public Rotation getRenderRotation(double ratio) {
        return MathUtilities.interpolate(this.previousRotation, this.rotation, ratio);
    }

    /**
     * Simulates the {@link PhysicsObject} for a time of <b>deltaTime</b>.
     * This first caches the current <b>position</b> and <b>rotation</b> as
     * <b>previousPosition</b> and <b>previousRotation</b>, respectively.
     * <p>
     * The new <b>position</b> is then calculated with:
     * <p>
     * <b><i>dₜ = d₀ + vt</i></b>.
     * <p>
     * The new <b>velocity</b> is then calculated with:
     * <p>
     * <b><i>vₜ = v₀ + at</i></b>.
     *
     * @param deltaTime the elapsed time to simulate this {@link PhysicsObject} for.
     */
    public void update(double deltaTime) {
        this.previousPosition = this.position;
        this.previousRotation = this.rotation;
        this.position = this.position.add(this.velocity.scale(deltaTime));

        if (this.velocity.isValid() && this.acceleration.isValid()) {
            this.velocity = this.velocity.add(this.acceleration.scale(deltaTime));
        }
    }

    /**
     * Moves the {@link PhysicsObject} to the position <b>position</b>,
     * caching the previous position as <b>previousPosition</b>.
     *
     * @param position the new <b>position</b>.
     */
    public void setPosition(Vec3 position) {
        this.previousPosition = this.position;
        this.position = position;
    }

    /**
     * Rotates {@link PhysicsObject} to the rotation <b>rotation</b>,
     * caching the previous rotation as <b>previousRotation</b>.
     *
     * @param rotation the new {@link Rotation}.
     */
    public void setRotation(Rotation rotation) {
        this.previousRotation = this.rotation;
        this.rotation = rotation;
    }

    /**
     * Returns the current position of this {@link PhysicsObject}.
     *
     * @return the current position
     */
    public Vec3 getPosition() {
        return this.position;
    }

    /**
     * Returns the current rotation of this {@link PhysicsObject}.
     *
     * @return the current rotation
     */
    public Rotation getRotation() {
        return this.rotation;
    }

    /**
     * Returns the current velocity of this {@link PhysicsObject}.
     *
     * @return the current velocity
     */
    public Vec3 getVelocity() {
        return this.velocity;
    }

    /**
     * Sets the <b>velocity</b> of the {@link PhysicsObject} to <b>velocity</b>.
     *
     * @param velocity the new <b>velocity</b>.
     */
    public void setVelocity(Vec3 velocity) {
        this.velocity = velocity;
    }

    /**
     * Returns the current acceleration of this {@link PhysicsObject}.
     *
     * @return the current acceleration
     */
    public Vec3 getAcceleration() {
        return this.acceleration;
    }

    /**
     * Sets the <b>acceleration</b> of the {@link PhysicsObject} to <b>acceleration</b>.
     *
     * @param acceleration the new <b>acceleration</b>.
     */
    public void setAcceleration(Vec3 acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Returns the current mass of this {@link PhysicsObject}.
     *
     * @return the current mass
     */
    public double getMass() {
        return this.mass;
    }

    /**
     * Sets the <b>mass</b> of the {@link PhysicsObject} to <b>mass</b>.
     *
     * @param mass the new <b>mass</b>.
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Modifies the <b>velocity</b> of the {@link PhysicsObject}, by
     * calculating the <b>acceleration</b> with:
     * <p>
     * <b><i>F = ma ≣ a = F / m</i></b>.
     * <p>
     * This <b>acceleration</b> is then simulated for <b>deltaTime</b> by using:
     * <p>
     * <b><i>vₜ = v₀ + at</i></b>.
     *
     * @param force     the applied force vector.
     * @param deltaTime the time elapsed since this force was applied.
     */
    public void applyForce(Vec3 force, double deltaTime) {
        this.velocity = this.velocity.add(force.scale(deltaTime / this.mass));
    }
}