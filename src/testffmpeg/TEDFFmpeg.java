/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testffmpeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import pageDAO.PageDAO;

/**
 *
 * @author mumu
 */
public class TEDFFmpeg {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        
        //makeVideo();
        ParseTED.Run();
  
    }
        
    
    public static void makeVideo() {
        
        String inputVideo;
        inputVideo = System.getProperty("user.home") + "/tempVid/DavidCamarillo_2016X-480p.mp4";
        String commandString = "konsole -e ffmpeg -i " + inputVideo + " -vn -acodec pcm_s16le -ar 44100 -ac 2 output_file.wav";

        try {

            Process p = Runtime.getRuntime().exec(commandString);

        } catch (IOException ex) {
            Logger.getLogger(TEDFFmpeg.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("testffmpeg.TestFFmpeg.main()");

    }
  

}
