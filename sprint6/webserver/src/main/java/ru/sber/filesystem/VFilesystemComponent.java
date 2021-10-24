package ru.sber.filesystem;

/**
 * Represents an object in the simulated filesystem, either a file or
 * folder.
 */
public abstract class VFilesystemComponent {
    /**
     * Name of this object (note this is not the path, but only this
     * object's name).
     */
    private final String name;

    /**
     * Construct a new component with the given name.
     *
     * @param setName Name of this component.
     */
    public VFilesystemComponent(final String setName) {
        name = setName;
    }

    /**
     * Getter for this component's name.
     *
     * @return Name of this component.
     */
    public String getName() {
        return name;
    }
}
