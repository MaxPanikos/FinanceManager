package org.example.financemanager;

import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;

public class FileManager {
    public static final String profilesPath = "profiles";
    public static final String profilePicturesPath= "profilePictures/";

    public static void save (Profile profile, String directoryPath) throws Exception {
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
            throw new Exception("Unexpected exception! ");
        }
    }

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
}
