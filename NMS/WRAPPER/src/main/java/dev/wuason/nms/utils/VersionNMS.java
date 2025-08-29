package dev.wuason.nms.utils;

import dev.wuason.mechanics.utils.VersionDetector;

public class VersionNMS {

    private static ServerVersionNMS serverVersionNMS = null;

    public enum ServerVersionNMS {
        v1_18_R2(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_18_2
        }),
        v1_19_R1(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_19,
                VersionDetector.ServerVersion.v1_19_1,
                VersionDetector.ServerVersion.v1_19_2
        }),
        v1_19_R2(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_19_3
        }),
        v1_19_R3(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_19_4
        }),
        v1_20_R1(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_20,
                VersionDetector.ServerVersion.v1_20_1
        }),
        v1_20_R2(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_20_2
        }),
        v1_20_R3(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_20_3,
                VersionDetector.ServerVersion.v1_20_4
        }),
        v1_20_R4(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_20_5,
                VersionDetector.ServerVersion.v1_20_6
        }),
        v1_21_R1(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_21,
                VersionDetector.ServerVersion.v1_21_1,
                VersionDetector.ServerVersion.v1_21_2
        }),
        v1_21_R2(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_21_3,
        }),
        v1_21_R3(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_21_4,
        }),
        v1_21_R4(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_21_5,
        }),
        v1_21_R5(new VersionDetector.ServerVersion[] {
                VersionDetector.ServerVersion.v1_21_6,
                VersionDetector.ServerVersion.v1_21_7,
                VersionDetector.ServerVersion.v1_21_8
        }),
        UNSUPPORTED(new VersionDetector.ServerVersion[] {VersionDetector.ServerVersion.UNSUPPORTED});

        private final VersionDetector.ServerVersion[] versions;

        ServerVersionNMS(VersionDetector.ServerVersion[] versions) {
            this.versions = versions;
        }

        public VersionDetector.ServerVersion[] getVersions() {
            return versions;
        }

        public static VersionNMS.ServerVersionNMS getServerVersion(VersionDetector.ServerVersion version) {
            for (VersionNMS.ServerVersionNMS serverVersion : VersionNMS.ServerVersionNMS.values()) {
                for (VersionDetector.ServerVersion serverVersion1 : serverVersion.getVersions()) {
                    if (serverVersion1 == version) {
                        return serverVersion;
                    }
                }
            }
            return UNSUPPORTED;
        }

    }

    public static ServerVersionNMS getServerVersion() {
        if (serverVersionNMS == null) {
            serverVersionNMS = ServerVersionNMS.getServerVersion(VersionDetector.getServerVersion());
        }
        return serverVersionNMS;
    }
}