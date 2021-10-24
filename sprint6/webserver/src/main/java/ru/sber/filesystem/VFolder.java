package ru.sber.filesystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a folder node in the virtual filesystem.
 */
public final class VFolder extends VFilesystemComponent {
    /**
     * Child components of this folder (may be folders and/or files).
     */
    private final Map<String, VFilesystemComponent> children;

    /**
     * Construct an empty folder with the given name.
     *
     * @param setName Name of this folder
     */
    public VFolder(final String setName) {
        super(setName);
        children = new HashMap<String, VFilesystemComponent>();
    }

    /**
     * Add a child of this folder.
     *
     * @param child Child of this folder, either a file or another folder.
     */
    public void addChild(VFilesystemComponent child) {
        assert !children.containsKey(child.getName());
        children.put(child.getName(), child);
    }

    /**
     * Check if this folder has a child by the given name.
     * 
     * @param name Child to check for
     * @return true if there is a child of this folder with the provided
     *              name, false otherwise
     */
    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    /**
     * Get the child of this folder that has the provided name.
     *
     * @param name Name of the child to fetch.
     * @return Child component
     */
    public VFilesystemComponent getChild(String name) {
        if (!hasChild(name)) {
            return null;
        }

        return children.get(name);
    }
}

