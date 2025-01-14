package br.otimizes.oplatool.architecture.smarty.util;

import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * This class help to save string in Logs and create and delete Log and Temp directory
 */
public class SaveStringToFile {

    public SaveStringToFile() {
    }

    private static final SaveStringToFile INSTANCE = new SaveStringToFile();

    public static SaveStringToFile getInstance() {
        return INSTANCE;
    }

    public void appendStrToFile(String fileName, String str) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
    }

    public void createLogDir() {
        String directory = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + "logs";
        File file = new File(directory);
        file.mkdir();
    }

    public void createTempDir() {
        String directory = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + FileConstants.TEMP_DIR;
        File file = new File(directory);
        file.mkdir();
    }

    public void deleteTempFolder() {
        String directory = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + FileConstants.TEMP_DIR;
        File folder = new File(directory);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        folder.delete();
    }

    public void moveProjectFinishPosteriori() {
        String fromDirectory = ApplicationFileConfigThreadScope.getDirectoryToExportModels();
        File fromFolder = new File(fromDirectory);
        String toDirectory = fromDirectory.replace(OPLAThreadScope.hash.get(), OPLAThreadScope.hashOnPosteriori.get());
        File toFolder = new File(toDirectory);
        try {
            Files.move(fromFolder.toPath(), toFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
