package phonebook.addon;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class fileOperation
{
    //addRecord : Append records to the dataFile
    private static RandomAccessFile editFile,newFile;
    
    public static long addRecord(String filename,String content)
    {
        long byteOffset = 0;
        try
        {
            File file = new File(filename);
            if(!file.exists())
                file.createNewFile();
            try {
                editFile = new RandomAccessFile(file,"rw");
            }
            catch(FileNotFoundException e)
            {
                System.out.println("Error!!-File Not Found");
            }
            byteOffset = editFile.length();
            editFile.seek(byteOffset);
            editFile.write((content+"\n").getBytes());
            editFile.close();
            System.out.println("in add method");
        }
        catch(IOException e)
        {
            System.out.println("Error!!-File I/O Problem");
        }
        return byteOffset;
    }
    
    //deleteRecord : primaryKey(Unique:PhoneNo)(2nd Field)
    public static void DeleteRecords(String Name, String Phone, boolean primaryKey) {
        try 
        {
            ArrayList<String> fileContent = new ArrayList<>();
            fileContent.clear();
            
            File filename = new File("dataFile.txt");
            RandomAccessFile dataFile = new RandomAccessFile(filename, "rw");
            RandomAccessFile indexNameFile = new RandomAccessFile("sortByNames.txt", "rw");
            RandomAccessFile indexPhoneFile = new RandomAccessFile("sortByPhoneNo.txt", "rw");
            
            String line;
            while((line=dataFile.readLine())!=null)
            {
                if(primaryKey == true)
                {
                    if(!line.split(";")[0].equals(Phone))
                        fileContent.add(line);
                }
                else
                {
                    if(!line.split(";")[1].equals(Name))
                        fileContent.add(line);
                }
            }
            
            dataFile.setLength(0);
            indexNameFile.setLength(0);
            indexPhoneFile.setLength(0);
            
            for(int i=0; i<fileContent.size(); i++)
            {
                String name = fileContent.get(i).split(";")[1];
                String phone = fileContent.get(i).split(";")[0];
                
                if(!"".equals(name))
                {
                    long byteOffset = fileOperation.addRecord("dataFile.txt",fileContent.get(i));
                    System.out.println(byteOffset);
                    fileOperation.addRecord("sortByNames.txt",name+";"+byteOffset);
                    fileOperation.addRecord("sortByPhoneNo.txt",phone+";"+byteOffset);
                }
            }
            
            fileOperation.sortIndexFile("sortByNames.txt");
            fileOperation.sortIndexFile("sortByPhoneNo.txt");
            
            dataFile.close();
            indexNameFile.close();
            indexPhoneFile.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(fileOperation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(fileOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //This method modifies the contents of the record.
    public static void modifyRecord(String content) {
        ArrayList<String> fileContent = new ArrayList<>();
        fileContent.clear();
        try {
            RandomAccessFile dataFile = new RandomAccessFile("dataFile.txt","rw");
            RandomAccessFile indexNameFile = new RandomAccessFile("sortByNames.txt", "rw");
            RandomAccessFile indexPhoneFile = new RandomAccessFile("sortByPhoneNo.txt", "rw");
            
            String line;
            while((line = dataFile.readLine())!=null)
            {
                if(content.split(";")[0].equals(line.split(";")[0]))
                    fileContent.add(content);
                else
                    fileContent.add(line);
            }
            
            dataFile.setLength(0);
            indexNameFile.setLength(0);
            indexPhoneFile.setLength(0);
            
            for(int i=0; i<fileContent.size(); i++)
            {
                String name = fileContent.get(i).split(";")[1];
                String phone = fileContent.get(i).split(";")[0];
                
                long byteOffset = fileOperation.addRecord("dataFile.txt",fileContent.get(i));
                System.out.println(byteOffset);
                fileOperation.addRecord("sortByNames.txt",name+";"+byteOffset);
                fileOperation.addRecord("sortByPhoneNo.txt",phone+";"+byteOffset);
            }
            
            fileOperation.sortIndexFile("sortByNames.txt");
            fileOperation.sortIndexFile("sortByPhoneNo.txt");
            
            dataFile.close();
            indexNameFile.close();
            indexPhoneFile.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(fileOperation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(fileOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //This method sorts the index file based on the argument indexFile
    public static void sortIndexFile(String indexFile) {
        ArrayList<Index> indexContent = new ArrayList<>();
        indexContent.clear();
        try 
        {
            File filename = new File(indexFile);
            if(!filename.exists())
                filename.createNewFile();
            
            RandomAccessFile file = new RandomAccessFile(filename,"rw");
            String line;
            
            while((line = file.readLine())!=null)
            {
                String key = line.split(";")[0];
                String pos = line.split(";")[1];
                indexContent.add(new Index(key, pos));
            }
            file.close();
            
            Collections.sort(indexContent, new keyCompare());
            
            for(Index index : indexContent)
            {
                String indexline = index.key + ";" + index.pos;
                addRecord("duplicateIndex.txt",indexline);
            }
            indexContent.clear();
            
            filename.delete();
            filename = new File("duplicateIndex.txt");
            filename.renameTo(new File(indexFile));
            
        } catch (IOException ex) {
            Logger.getLogger(fileOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Class Index is used for sorting the index file
    private static class Index {
        String key, pos;
        public Index(String key, String pos) {
            this.key = key;
            this.pos = pos;
        }
    }

    //Comparator method to sort a file based on key.
    private static class keyCompare implements Comparator<Index> {
        @Override
        public int compare(Index o1, Index o2) {
            return o1.key.compareTo(o2.key);
        }
    }
    
}
