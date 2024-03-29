package net.realdarkstudios.commons.misc;

import net.realdarkstudios.commons.util.Version;
import org.jetbrains.annotations.NotNull;

public interface IRDSPlugin {
    @NotNull
    Version getVersion();

    @NotNull
    String getVersionString();
}
