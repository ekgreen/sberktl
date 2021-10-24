package ru.sber.filesystem;

/**
 * A class to represent a virtual, in-memory filesystem. VFilesystem exposes
 * two user-visible APIs: one for adding a file to a given virtual filesystem
 * (addFile) and one for reading the contents of a file that already exists
 * (readFile).
 */
public class VFilesystem {
    /**
     * The root folder for the virtual filesystem.
     */
    private final VFolder root = new VFolder("static");

    /**
     * Default constructor, creating an empty filesystem.
     */
    public VFilesystem() {
    }

    /**
     * Add a file at the provided path with the provided contents.
     *
     * @param path VPath to the new file to create
     * @param contents Contents of the new file
     */
    public void addFile(VPath path, String contents) {
        assert path.getNComponents() > 0;

        assert path.getComponent(0).equals(root.getName());

        int componentIndex = 1;
        VFolder curr = root;
        while (componentIndex < path.getNComponents()) {
            String component = path.getComponent(componentIndex++);
            VFilesystemComponent next = curr.getChild(component);

            if (componentIndex < path.getNComponents()) {
                // Haven't reached bottom of path yet so must be folder
                if (next == null) {
                    VFolder newFolder = new VFolder(component);
                    curr.addChild(newFolder);
                    curr = newFolder;
                } else {
                    assert next instanceof VFolder;
                    curr = (VFolder)next;
                }
            } else {
                // Reached base filename
                assert next == null;
                VFile newFile = new VFile(component, contents);
                curr.addChild(newFile);
            }
        }
    }

    /**
     * Read the file specified by path.
     *
     * @param path The absolute path to the file to read.
     * @return The contents of the specified file, or null if the file does not
     *         seem to exist.
     */
    public String readFile(VPath path) {
        if (path.getNComponents() == 0) {
            return null;
        }

        if (!path.getComponent(0).equals(root.getName())) {
            return null;
        }

        int componentIndex = 1;
        VFilesystemComponent curr = root;
        while (componentIndex < path.getNComponents()) {
            final String nextComponent = path.getComponent(componentIndex++);

            if (curr == null || !(curr instanceof VFolder)) {
                return null;
            }

            VFilesystemComponent next = ((VFolder)curr).getChild(
                    nextComponent);

            curr = next;
        }

        if (curr == null || !(curr instanceof VFile)) {
            return null;
        } else {
            return ((VFile)curr).read();
        }
    }
}
