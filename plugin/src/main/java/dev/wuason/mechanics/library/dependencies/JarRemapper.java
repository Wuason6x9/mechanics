package dev.wuason.mechanics.library.dependencies;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;

import java.io.*;
import java.util.Map;
import java.util.jar.*;
import java.util.regex.Pattern;

public class JarRemapper {

    private static final Pattern SIGNATURE_FILE_PATTERN = Pattern.compile("META-INF/(?:[^/]+\\.(?:DSA|RSA|SF)|SIG-[^/]+)");
    private static final Pattern SIGNATURE_PROPERTY_PATTERN = Pattern.compile(".*-Digest");

    public JarRemapper() {
    }

    public File remap(File oldFile, Remap... remaps) {
        try {
            File newFile = File.createTempFile(oldFile.getName().replace(".jar", ""), ".jar");
            remap(oldFile, newFile, remaps);
            return newFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void remap(File oldFile, File newFile, Remap... remaps) {
        if (remaps.length == 0) {
            return;
        }
        MechanicRemapper reMapper = new MechanicRemapper(remaps);
        try (JarFile oldJar = new JarFile(oldFile);
             JarOutputStream newJar = new JarOutputStream(new FileOutputStream(newFile))) {

            oldJar.stream().forEach(entryJar -> {
                try (InputStream entryInputStream = oldJar.getInputStream(entryJar)) {

                    if (entryJar.isDirectory()) {
                        return;
                    }

                    String newName = reMapper.map(entryJar.getName());
                    if (newName == null) {
                        newName = entryJar.getName();
                    }

                    JarEntry newEntry = new JarEntry(newName);
                    newEntry.setTime(entryJar.getTime());
                    newJar.putNextEntry(newEntry);

                    if (entryJar.getName().endsWith(".class")) {
                        ClassReader cr = new ClassReader(entryInputStream);
                        ClassWriter cw = new ClassWriter(0);

                        ClassRemapper classRemapper = new ClassRemapper(cw, reMapper);
                        cr.accept(classRemapper, ClassReader.EXPAND_FRAMES);

                        byte[] classBytes = cw.toByteArray();
                        newJar.write(classBytes);
                    } else {
                        if(entryJar.getName().equals("META-INF/MANIFEST.MF")) {
                            Manifest in = new Manifest(entryInputStream);
                            Manifest out = new Manifest();
                            out.getMainAttributes().putAll(in.getMainAttributes());
                            for (Map.Entry<String, Attributes> entry : in.getEntries().entrySet()) {
                                Attributes outAttributes = new Attributes();
                                for (Map.Entry<Object, Object> property : entry.getValue().entrySet()) {
                                    String key = property.getKey().toString();
                                    if (!SIGNATURE_PROPERTY_PATTERN.matcher(key).matches()) {
                                        outAttributes.put(property.getKey(), property.getValue());
                                    }
                                }
                                out.getEntries().put(entry.getKey(), outAttributes);
                            }
                            out.write(newJar);
                        }
                        else {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = entryInputStream.read(buffer)) > 0) {
                                newJar.write(buffer, 0, len);
                            }
                        }
                    }

                    newJar.closeEntry();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


