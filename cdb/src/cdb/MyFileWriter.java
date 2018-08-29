package cdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;



public class MyFileWriter {
	private String db_path = "/dev/";
	private String db_directory_name = "cosadb";
	private String db_name = "cdb";
	private String directoryName = db_path.concat(db_directory_name);
	long sizePerFile = 1000000; //max allowed size per file, given in bytes
	int retPolicy = 0; //if set to 0 means infinity
	int maxfiles = 1024;
	
	public void setDbName(String dbname) {
		this.db_name = dbname;
	}
    public int writeFile(String fileName, InputStream inputStream) throws IOException {
    	
    	File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }
                
        FileWriter f = new FileWriter(directoryName + "/" + db_name+"_"+fileName,true);
        BufferedWriter bos = new BufferedWriter(f);
        
        int bytesWritten = 0;
        int b;
        while ((b = inputStream.read()) != -1) {
            bos.write(b);
            bytesWritten++;
        }
        bos.close();
        
        return bytesWritten;
    }
    
    /**
     * Checks if file to be written in meet size per file. 
     * If not, create a new file by adding 1us to current filename
     */
    @SuppressWarnings("unused")
	private long findEntryPoint(long fileName) {
    	File[] listOfFiles = (new File(directoryName)).listFiles();
    	long[] filenamesList = new long[maxfiles];
    	int k = 0;
    	for (int i = 0; i < listOfFiles.length; i++) {
    		  if (listOfFiles[i].isFile()) {
    		    filenamesList[k] = Long.parseLong(listOfFiles[i].getName());
    		    k++;
    		  } 
    	}
    	
    	if (listOfFiles.length>0) {
    		long[] filenamesList_resized = new long[k];
        	
        	for(int i=0;i<filenamesList_resized.length;i++) {
        		filenamesList_resized[i] = filenamesList[i];
        	}
        	
        	return filenamesList_resized[k-1];
    	}
    	else {
    		return 0L;
    	}
    }
    
    @SuppressWarnings("unused")
	private File checkFileSize(long fileName) { 
    	File file = new File(directoryName + "/" + fileName);
    	long fileSize = file.length();
    	if ( fileSize >= sizePerFile ) {
    		fileName++;
    		file = new File(directoryName + "/" + fileName);
    	}
		return file;
    }
    
    /**
     * For testing:
     *    args[0] = Name of binary file to make a copy of.
     */
    public static void main(String[] args) throws Exception {
        // 
        // Read file
       
        // 
        // Write copy of file
        //
        
//        new MyFileWriter().writeFile(fileName + "_COPY." + parts[1], is);
    }
}
