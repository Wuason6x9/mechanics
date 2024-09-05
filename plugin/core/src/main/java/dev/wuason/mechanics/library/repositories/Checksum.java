package dev.wuason.mechanics.library.repositories;

import dev.wuason.mechanics.library.dependencies.Dependency;
import dev.wuason.mechanics.utils.HashingUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Checksum {

    private final List<ChecksumResult> checksumResults = new ArrayList<>();

    public void downloadChecksums(Repository repository, Dependency dependency) {
        for (ChecksumType type : ChecksumType.values()) {
            ChecksumResult result = new ChecksumResult(type);
            try (InputStream inputStream = repository.downloadDependency(dependency, Repository.DownloadType.JAR, type)) {
                if (inputStream != null) {
                    result.setOriginalChecksum(new String(inputStream.readAllBytes()));
                    result.setResolved(true);
                }
            } catch (Exception e) {
            }
            checksumResults.add(result);
        }
    }

    public void calculateChecksum(File file) {
        checksumResults.stream().filter(ChecksumResult::isResolved).forEach(result -> {
            HashingUtils.HashType hashType = HashingUtils.HashType.valueOf(result.getType().name());
            result.setCalculatedChecksum(HashingUtils.hash(file, hashType));
        });
    }

    public boolean isChecksumValid() {
        return checksumResults.isEmpty() || checksumResults.stream().anyMatch(ChecksumResult::isChecksumValid) || checksumResults.stream().noneMatch(ChecksumResult::isResolved);
    }

    public List<ChecksumResult> getChecksumCorrects() {
        return checksumResults.stream().filter(ChecksumResult::isChecksumValid).toList();
    }


    public enum ChecksumType {
        SHA512,
        SHA256,
        SHA1,
        MD5
    }
}
