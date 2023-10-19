package GeoServer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Zip {

   private static final int BUFFER = 2048;
   List<String> fileList;
    private static  String OUTPUT_ZIP_FILE = "my.zip";
    private static  String SOURCE_FOLDER = "Zip";
	
    Zip(){
    	fileList = new ArrayList<String>();
    }
	
    Zip(String Dir, String Zip ){
    	OUTPUT_ZIP_FILE=Zip;
    	SOURCE_FOLDER=Dir;
    	fileList = new ArrayList<String>();
    }
    
    
    public void Zipit () throws IOException{
    	
    	final int BUFFER = 2048;
    	 BufferedInputStream origin = null;
         FileOutputStream dest = new FileOutputStream(OUTPUT_ZIP_FILE);
         
         
         CheckedOutputStream checksum = new  CheckedOutputStream(dest, new Adler32());
         ZipOutputStream out = new  ZipOutputStream(new BufferedOutputStream(checksum));
         out.setMethod(ZipOutputStream.DEFLATED);
         byte data[] = new byte[BUFFER];
         // get a list of files from current directory
        
         File f = new File(SOURCE_FOLDER);
         String files[] = f.list();

         for (int i=0; i<files.length; i++) {
            System.out.println("Adding: "+files[i]);
            FileInputStream fi = new FileInputStream(SOURCE_FOLDER+"//"+files[i]);
           
            origin = new BufferedInputStream(fi, BUFFER);
            
            
            ZipEntry entry = new ZipEntry(files[i]);
            out.putNextEntry(entry);
            int count;
           
            
            while((count = origin.read(data, 0, BUFFER)) != -1) {
               out.write(data, 0, count);
            }
            origin.close();
            
            Path path = FileSystems.getDefault().getPath(SOURCE_FOLDER, files[i]);
            Files.deleteIfExists(path);
         }
         out.close();
        // System.out.println("checksum:"+checksum.getChecksum().getValue());
    }
    
    public void deleteZip() throws IOException{
    	 Path path = FileSystems.getDefault().getPath("", OUTPUT_ZIP_FILE);
         Files.deleteIfExists(path);
    }
    
    
    /*   public static void main( String[] args ) throws IOException
    {
    	Zip appZip = new Zip();
    	//appZip.generateFileList(new File(SOURCE_FOLDER));
    	//appZip.zipIt(OUTPUT_ZIP_FILE);
    	appZip.Zipit();
    }*/
}
