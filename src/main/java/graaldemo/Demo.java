package graaldemo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import org.eclipse.jetty.util.resource.Resource;

public class Demo
{
    public static void main(String[] args) throws URISyntaxException
    {
        String versionResourceRef = "org/eclipse/jetty/version/build.properties";
        System.out.println("-- Looking for: " + versionResourceRef);
        URL urlVersion = Thread.currentThread().getContextClassLoader().getResource(versionResourceRef);
        dumpURLinfo(urlVersion);

        System.out.println();
        System.out.println("-- Looking for: PathResource.class");
        URL urlPathResource = Resource.class.getResource("PathResource.class");
        dumpURLinfo(urlPathResource);

        System.out.println();
        System.out.println("-- Looking for: hello.txt relative to " + Demo.class.getName());
        URL urlDemoResource = Demo.class.getResource("hello.txt");
        dumpURLinfo(urlDemoResource);
    }

    private static void dumpURLinfo(URL url) throws URISyntaxException
    {
        if (url == null)
        {
            System.out.println("WARNING: Unable to find resource");
            return;
        }
        URI uri = url.toURI();
        System.out.println("FOUND:              " + uri);
        Path p;
        try
        {
            p = Path.of(uri);
        }
        catch (FileSystemNotFoundException e)
        {
            // This is "somewhat" expected...
            // thrown for jar:file: and resource: URIs upon first interaction with container
            e.printStackTrace();
            try
            {
                FileSystems.newFileSystem(uri, Collections.emptyMap());
                p = Path.of(uri);
            }
            catch (RuntimeException | IOException e1)
            {
                e.printStackTrace();
                p = null;
            }
        }
        System.out.println("Path:               " + p + " (exists:" + Files.exists(p) + ")");
        if (p != null)
        {
            URI pathUri = p.toUri();
            System.out.println("Path.toUri:         " + pathUri);
            Path p2 = Path.of(pathUri);
            System.out.println("Path of Path.toUri: " + p2 + " (exists:" + Files.exists(p2) + ")");
        }
    }
}
