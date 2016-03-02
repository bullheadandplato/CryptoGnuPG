package com.Controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h4ck3r on 2/18/16.
 * The Gnu Privacy Guard is in this file
 * All the operations of GPG will be executed in this file
 */
public class GnuPG {
    //
    //GnuPG variables
    //
    private String passphrase;
    private String keyID;
    private String gpgCommamd = "/usr/bin/gpg --batch --armor --yes";
    private int gpg_exitCode = -1;
    private String gpg_result;
    private String gpg_err;
    //
    //File Variables
    //
    File tmpFile;
    private String encryptedMessage = "";
    private String decryptedMessage = "";


    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    /**
     * @param keyid   the id of the user for which this user is encrypting message
     * @param message message to be encrypted
     */
    public void Encrypt(String keyid, String message) {
        boolean success = createTempFile(message);
        if (success) {
            runGnuPG("--output /tmp/enc -r " + keyid + " --encrypt " + tmpFile.getAbsolutePath());
            //get encrypted message
            try {
                encryptedMessage = "";
                FileReader reader = new FileReader("/tmp/enc");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    encryptedMessage += line + "\n";
                }

            } catch (IOException e) {
                //do nothing
            }
            tmpFile.delete();


        }

    }

    /**
     * @param message
     */
    public void Decrypt(String message, String pass) {
        boolean file = createTempFile(message);
        if (file) {
            boolean success = runGnuPG("--output /tmp/dec --passphrase " + pass + " --decrypt " + tmpFile.getAbsolutePath());
            if (success) {
                //get decrypted message
                decryptedMessage = "";
                try {
                    FileReader reader = new FileReader("/tmp/dec");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        decryptedMessage += line;
                    }

                } catch (IOException e) {
                    //do nothing
                }
                File abc = new File("/tmp/dec");
                abc.delete();
                tmpFile.delete();
            }
        }

    }

    public String getDecryptedMessage() {
        return this.decryptedMessage;
    }

    /**
     * Runs GnuPG external program
     *
     * @param commandArgs command line arguments
     * @return true if success.
     */
    private boolean runGnuPG(String commandArgs) {
        Process p;
        String fullCommand = gpgCommamd + " " + commandArgs;


        try {
            p = new ProcessBuilder().inheritIO().command("bash", "-c", fullCommand).start();
        } catch (IOException io) {
            System.out.println("io Error" + io.getMessage());
            return false;
        }

        try {
            p.waitFor();
        } catch (InterruptedException i) {
            System.out.println("Exception at wait for! " + i.getMessage());
            return false;
        }

        try {
            gpg_exitCode = p.exitValue();
        } catch (IllegalThreadStateException se) {
            return false;
        }
        return true;
    }

    /**
     * A utility method for creating a unique temporary file when needed by one of
     * the main methods.<BR>
     * The file handle is store in tmpFile object var.
     *
     * @param inStr data to write into the file.
     * @return true if success
     */
    private boolean createTempFile(String inStr) {
        this.tmpFile = null;
        FileWriter fw;

        try {
            this.tmpFile = File.createTempFile("YGnuPG", null);
        } catch (Exception e) {
            System.out.println("Cannot create temp file " + e.getMessage());
            return false;
        }

        try {
            fw = new FileWriter(this.tmpFile);
            fw.write(inStr);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            // delete our file:
            tmpFile.delete();

            System.out.println("Cannot write temp file " + e.getMessage());
            return false;
        }

        return true;
    }

    public String getEncryptedMessage() {
        return this.encryptedMessage;
    }

    public List<String> friends() {
        List<String> temp = new ArrayList<>();
        try {
            Process pb = new ProcessBuilder().command("bash", "-c",
                    "/usr/bin/gpg --batch --yes --list-keys |grep uid >/tmp/friends").start();
            pb.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            FileReader fileReader = new FileReader("/tmp/friends");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                temp.add(line);
            }

        } catch (IOException e) {
            //do nothing
        }
        File file = new File("/tmp/friends");
        file.delete();
        return temp;

    }
}
