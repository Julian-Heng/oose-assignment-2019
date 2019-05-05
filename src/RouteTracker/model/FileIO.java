package RouteTracker.model;

import java.io.*;
import java.util.*;

public class FileIO
{
    public List<String> readFile(String filename) throws IOException
    {
        List<String> contents = new ArrayList<>();
        String line;
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(filename));

            while ((line = reader.readLine()) != null)
            {
                contents.add(line);
            }
        }
        catch (IOException e)
        {
            throw new IOException("Error reading file: " + filename);
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException ex)
            {
            }
        }

        return contents;
    }
}
