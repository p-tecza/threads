/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.data;

/**
 *
 * @author Dominik Olszewski
 */
public class DataStreams {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String workingDirectoryPath = System.getProperty("user.dir");
        //IOProcessor.copyFile(workingDirectoryPath + "/Input", "Output");
        IOProcessor.removeWordsFromFile(workingDirectoryPath + "/Input", "Output", new String[]{"abc", "ftt"});
        //System.out.println(IOProcessor.fileToString(workingDirectoryPath + "/Input"));
    }
}
