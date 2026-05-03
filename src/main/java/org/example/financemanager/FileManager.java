package org.example.financemanager;

import java.io.*;
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
            throw new Exception("Unexpected exception!");
        }
    }

    public static ArrayList<Profile> loadProfiles (String directoryPath) throws Exception {
        try {
            File file = new File(directoryPath);
            if (!file.exists()) {
                throw new Exception("Directory does not exist!");
            }
            String[] files = file.list();

            ArrayList<Profile> profiles = new ArrayList<>();
            for (String name : files) {
                if (name != null ) {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(directoryPath + "/" + name));
                    Profile profile = (Profile) in.readObject();
                    profiles.add(profile);
                }
            }
            return profiles;
        } catch (Exception e) {
            throw new Exception("Unexpected exception!" + e.getMessage());
        }
    }
}
