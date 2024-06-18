package org.example.registros;

import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.util.List;

public class DiscoRegistro {
    public static String extrairDisco() {
        SystemInfo si = new SystemInfo();
        FileSystem fileSystem = si.getOperatingSystem().getFileSystem();
        List<OSFileStore> fileStores = fileSystem.getFileStores();
        double usoPorcentagem = 0.0;

        for (OSFileStore fs : fileStores) {
            long totalSpace = fs.getTotalSpace();
            long usableSpace = fs.getUsableSpace();
            long usedSpace = totalSpace - usableSpace;
            usoPorcentagem = (double) usedSpace / totalSpace * 100;
        }
        return String.format("%.2f", usoPorcentagem);
    }
}
