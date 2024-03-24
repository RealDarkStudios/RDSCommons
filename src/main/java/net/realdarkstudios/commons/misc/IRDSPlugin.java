package net.realdarkstudios.commons.misc;

import net.realdarkstudios.commons.util.Version;

public interface IRDSPlugin {
    Version getVersion();

    String getVersionString();
}
