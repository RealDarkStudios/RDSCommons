package net.realdarkstudios.commons;

import net.realdarkstudios.commons.util.AutoUpdater;
import net.realdarkstudios.commons.util.Version;

public class RDSCommonsUpdater extends AutoUpdater {

    /**
     * Creates an auto-updater for a given Plugin.
     * There is no need to create more than one auto updater, as this may have bad consequences.
     */
    public RDSCommonsUpdater() {
        super(RDSCommons.getInstance(),
                "https://maven.digitalunderworlds.com/%b%s/net/realdarkstudios/rdscommons/maven-metadata.xml",
                "https://maven.digitalunderworlds.com/%b%s/net/realdarkstudios/rdscommons/%v%/rdscommons-%v%.jar",
                Parser.MAVEN_METADATA);
    }

    @Override
    protected void statusBehind(Version newVer, String newVerInput) {

    }

    @Override
    protected void statusUpToDate() {

    }

    @Override
    protected void statusAhead() {

    }

    @Override
    protected void errorChecking() {

    }
}
