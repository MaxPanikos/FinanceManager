package org.example.financemanager;

import javax.crypto.spec.OAEPParameterSpec;
import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.UUID;

public class FileManager {
    public static final String profilesPath = "profiles";
    public static final String profilePicturesPath= "profilePictures/";

    /**
     * saves profile
     * @param profile profile you want to save
     * @param directoryPath directory where you want to save the profile
     */
    public static void save (Profile profile, String directoryPath) {
        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(directory, profile.getUsername());
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(profile);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println(e.getCause().toString() + " " + e.getMessage());
        }
    }

    /**
     * load profiles from directory
     * @param directoryPath path where the directory is located
     * @return ArrayList of Profiles
     * @throws Exception unexpected exception
     */
    public static ArrayList<Profile> loadProfiles (String directoryPath) throws Exception {
        try {
            File file = new File(directoryPath);
            if (!file.exists()) {
                Path path = Paths.get(directoryPath);
                Files.createDirectory(path);
                return new ArrayList<>();
            }
            String[] files = file.list();

            ArrayList<Profile> profiles = new ArrayList<>();
            for (String name : files) {
                if (name != null ) {
                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(directoryPath + "/" + name));
                        Profile profile = (Profile) in.readObject();
                        profiles.add(profile);
                    } catch (Exception e) {
                        System.err.println("Unexpected file in " + directoryPath + " directory (" + name + ")");
                    }
                }
            }
            return profiles;
        } catch (Exception e) {
            throw new Exception("Unexpected exception! " + e.getMessage());
        }
    }

    /**
     * checks if this username already exists
     * @param username username you want to check
     * @param directoryPath path to where the profiles are located
     * @return true if it already exists
     */
    public static boolean exists (String username, String directoryPath) {
        File file = new File(directoryPath, username);
        return file.exists() && file.isFile();
    }

    /**
     * removes profile
     * @param username username of profile you want to remove
     * @param directoryPath path to where the profiles are located
     * @return
     */
    public static boolean removeProfile (String username, String directoryPath) {
        try {
            Path path = Paths.get(directoryPath, username);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * saves profile picture
     * @param profilePicture picture you want to save
     * @param directoryPath path to directory where you want to save the picture
     * @return String of the new picture file name
     * @throws Exception unexpected exception
     */
    public static String saveProfilePicture (File profilePicture, String directoryPath) throws Exception {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String originalFileName = profilePicture.getName();
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + ext;
        Path targetPath = path.resolve(newFileName);
        Files.copy(profilePicture.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return newFileName;
    }
}
