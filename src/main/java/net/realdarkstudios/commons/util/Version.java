package net.realdarkstudios.commons.util;

import net.realdarkstudios.commons.CommonsAPI;

public class Version implements Comparable<Version> {
    private final int majorapi, minorapi, build, patch, prereleaseVer;
    private final boolean snapshot, prerelease;
    private final String snapshotVer;

    public Version(int majorapi, int minorapi, int build, int patch, boolean snapshot, String snapshotVer, boolean prerelease, int prereleaseVer) {
        this.majorapi = majorapi;
        this.minorapi = minorapi;
        this.build = build;
        this.patch = patch;

        if (snapshot && prerelease) {
            CommonsAPI.warning("Constructed version is both a snapshot and prerelease! Picking prerelease due to precedence.");
            snapshot = false;
        }

        this.snapshot = snapshot;
        this.snapshotVer = snapshotVer;
        this.prerelease = prerelease;
        this.prereleaseVer = prereleaseVer;
    }

    public int getMajorApi() {
        return majorapi;
    }

    public int getMinorApi() {
        return minorapi;
    }

    public int getBuild() {
        return build;
    }

    public int getPatch() {
        return patch;
    }

    public String getSnapshotVer() {
        return snapshotVer;
    }

    public int getPrereleaseVer() {
        return prereleaseVer;
    }

    public boolean isRelease() {
        return !snapshot && !prerelease;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    public boolean isPrerelease() {
        return prerelease;
    }

    @Override
    public String toString() {
        if (prerelease) {
            return String.format("%d.%d.%d.%d-pre%d", majorapi, minorapi, build, patch, prereleaseVer);
        } else if (snapshot) {
            return String.format("%d.%d.%d.%d-snapshot-%s", majorapi, minorapi, build, patch, snapshotVer);
        } else {
            return String.format("%d.%d.%d.%d", majorapi, minorapi, build, patch);
        }
    }

    public static Version fromString(String version) {
        if (version.contains("-pre")) {
            // prerelease
            int maj, min, build, patch, prn;
            // splits into X.X.X.X and X (prerelease number/prn)
            // ex 1.0.0.0-pre1 -> 1.0.0.0 and 1

            String[] parts = version.split("-pre");
            String[] release = parts[0].split("\\.");

            maj = Integer.parseInt(release[0]);
            min = Integer.parseInt(release[1]);
            build = Integer.parseInt(release[2]);
            patch = Integer.parseInt(release[3]);
            prn = Integer.parseInt(parts[1]);

            return new Version(maj, min, build, patch, false, "", true, prn);
        } else if (version.toLowerCase().contains("snapshot-")) {
            // OLD CODE FROM MINECACHING
//            if (version.equals("snapshot-24w11b")) return new Version(0, 3, 1, 0, true, "24w11b", false, 0);

            //snapshot
            int maj, min, build, patch;
            String sv;

            String[] parts = version.split("-");
            String[] release;

            if (version.toLowerCase().startsWith("snapshot-")) {
                // splits into ["snapshot", "X.X.X.X", "sv (snapshot version)"]
                // ex snapshot-1.0.0.0-24w12a -> ["snapshot", "1.0.0.0", "24w12a"]
                release = parts[1].split("\\.");
            } else {
                // splits into ["X.X.X.X", "snapshot", "sv (snapshot version)"]
                // ex 1.0.0.0-snapshot-24w13a -> ["1.0.0.0", "snapshot", "24w13a"]
                release = parts[0].split("\\.");
            }

            maj = Integer.parseInt(release[0]);
            min = Integer.parseInt(release[1]);
            build = Integer.parseInt(release[2]);
            patch = Integer.parseInt(release[3]);
            sv = parts[2];

            return new Version(maj, min, build, patch, true, sv, false, 0);
        } else {
            // OLD CODE FROM MINECACHING
//            if (version.equals("1.0.0.0-24w10a")) return new Version(0, 3, 1, 0, true, "24w10a", false, 0);
//            if (version.equals("1.0.0.0-24w11a")) return new Version(0, 3, 1, 0, true, "24w11a", false, 0);

            //release
            int maj, min, build, patch;
            // splits into ["X.X.X.X", "extra (extra bits, will ignore)"]
            // ex 1.0.0.0-randomJunk -> ["1.0.0.0", "randomJunk"]
            // we only care about the first bit

            String[] parts = version.split("-");
            String[] release = (parts[0]).split("\\.");

            maj = Integer.parseInt(release[0]);
            min = Integer.parseInt(release[1]);
            build = Integer.parseInt(release[2]);
            patch = Integer.parseInt(release[3]);

            return new Version(maj, min, build, patch, false, "", false, 0);
        }
    }

    @Override
    public int compareTo(Version other) {
        // handles X.X.X.X comparing
        int maj = Integer.compare(this.getMajorApi(), other.getMajorApi());
        if (maj != 0) return maj;

        int min = Integer.compare(this.getMinorApi(), other.getMinorApi());
        if (min != 0) return min;

        int build = Integer.compare(this.getBuild(), other.getBuild());
        if (build != 0) return build;

        int patch = Integer.compare(this.getPatch(), other.getPatch());
        if (patch != 0) return patch;

        // release type check
        // release > prerelease > snapshot
        if (this.isRelease() && (other.isPrerelease() || other.isSnapshot()) || this.isPrerelease() && other.isSnapshot()) return 1;
        else if (other.isRelease() && (this.isPrerelease() || this.isSnapshot()) || other.isPrerelease() && this.isSnapshot()) return -1;

        // both are a release/prerelease/snapshot
        if (this.isRelease() && other.isRelease()) return 0;
        if (this.isPrerelease() && other.isPrerelease()) {
            return Integer.compare(this.getPrereleaseVer(), other.getPrereleaseVer());
        } else {
            //snapshot parsing woo
            //format: YYwWWX (YY is 2 digit year, WW is 2 digit week number, X is week snapshot ver)

            int year = compareSnapshotNumberPart(this.getSnapshotVer().substring(0, 2), other.getSnapshotVer().substring(0, 2));
            if (year != 0) return year;

            int week = compareSnapshotNumberPart(this.getSnapshotVer().substring(3, 5), other.getSnapshotVer().substring(3, 5));
            if (week != 0) return week;

            return compareSnapshotStringPart(this.getSnapshotVer().substring(5), other.getSnapshotVer().substring(5));
        }
    }

    private int compareSnapshotNumberPart(String partTs, String partOs) {
        int partT, partO;

        try {
            partT = Integer.parseInt(partTs);
        } catch (Exception e) {
            partT = 0;
        }

        try {
            partO = Integer.parseInt(partOs);
            if (partT == 0) return -1;
        } catch (Exception e) {
            if (partT == 0) return 0;
            else return 1;
        }

        return Integer.compare(partT, partO);
    }

    private int compareSnapshotStringPart(String partTs, String partOs) {
        // this will only be the very last part of the snapshot;

        if (partTs.length() > partOs.length()) return 1;
        if (partOs.length() > partTs.length()) return -1;
        if (partTs.isEmpty()) return 0;

        int lastCharCheck = 0;
        for (int i = 0; i < partTs.length(); i++) {
            int charV = Character.compare(partTs.toCharArray()[i], partOs.toCharArray()[i]);
            lastCharCheck = charV;
            if (charV != 0) break;
        }

        return lastCharCheck >= 1 ? 1 : lastCharCheck <= -1 ? -1 : 0;
    }
}
