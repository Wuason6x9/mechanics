package dev.wuason.mechanics.library.dependencies;

/**
 * The Remap class is utilized to represent a pair of mapping values
 * which could be useful in dependency management and remapping operations.
 */
public class Remap {
    /**
     * Represents the old mapping value in the Remap class.
     * This value is processed by replacing colons with slashes.
     */
    private final String old;
    /**
     * Represents the new mapping value in the Remap class.
     * This value is used to store the remapped destination for a given
     * source value, commonly utilized in dependency management and remapping
     * operations.
     */
    private final String new_;

    /**
     * Constructs a Remap object that represents a mapping between two strings.
     * The given strings will have their colon characters (':') replaced with
     * forward slashes ('/').
     *
     * @param old the original string to be remapped
     * @param new_ the new string to be used for mapping
     */
    private Remap(String old, String new_) {
        this.old = old.replace(':', '/');
        this.new_ = new_.replace(':', '/');
    }

    /**
     * Retrieves the old mapping value used for remapping operations.
     *
     * @return the old mapping value
     */
    public String getOld() {
        return old;
    }

    /**
     * Retrieves the new mapping value of this {@code Remap}.
     *
     * @return the new mapping value after replacing colons with slashes.
     */
    public String getNew() {
        return new_;
    }

    /**
     * Creates a new instance of the Remap class with the provided old and new values.
     *
     * @param old the original value to be remapped.
     * @param new_ the new value to map to.
     * @return a new Remap instance with the specified old and new values.
     */
    public static Remap of(String old, String new_) {
        return new Remap(old, new_);
    }
}
