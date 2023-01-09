package graaldemo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.util.resource.Resource;

public class Demo
{
    public static void main(String[] args) throws URISyntaxException
    {
        String versionResourceRef = "org/eclipse/jetty/version/build.properties";
        System.out.println("-- Looking for: " + versionResourceRef);
        URL urlVersion = Thread.currentThread().getContextClassLoader().getResource(versionResourceRef);
        if (urlVersion == null)
            System.out.println("WARNING: Unable to find resource: " + versionResourceRef);
        else
        {
            System.out.println("FOUND: " + urlVersion.toURI());
            dumpPathDetails(urlVersion.toURI());
        }

        System.out.println("-- Looking for: PathResource.class");
        URL urlPathResource = Resource.class.getResource("PathResource.class");
        if (urlPathResource == null)
            System.out.println("WARNING: Unable to find PathResource.class");
        else
        {
            System.out.println("FOUND: " + urlPathResource.toURI());
            dumpPathDetails(urlPathResource.toURI());
        }
    }

    public static void dumpPathDetails(URI uri)
    {
        try
        {
            Path path = Path.of(uri);
            dumpPathDetails("Direct open of URI: " + uri, path);
        } catch (FileSystemNotFoundException e)
        {
            // we probably need to mount it first
            System.out.println(e.getClass().getName() + ": Unable to open URI directly, switching to mount version.");
            dumpMountedPathDetails(uri);
        }
    }

    public static void dumpPathDetails(String context, Path path)
    {
        System.out.println("-- " + context);
        System.out.println("Path: " + path);
        System.out.println("Path.toURI: " + path.toUri());
        System.out.println("Path.getFileSystem.className: (" + path.getFileSystem().getClass().getName() + ")");
        System.out.println("Files.isDirectory(path): " + Files.isDirectory(path));
        System.out.println("Files.isRegularFile(path): " + Files.isRegularFile(path));
        System.out.println("Files.isReadable(path): " + Files.isReadable(path));
        System.out.println("Files.exists(path): " + Files.exists(path));
        try
        {
            System.out.println("Files.size(path): " + Files.size(path));
        }
        catch (IOException e)
        {
            System.out.println("Files.size(path): " + e.getClass().getName() + ": " + e.getMessage());
        }
        try
        {
            System.out.println("Files.getLastModifiedTime(path): " + Files.getLastModifiedTime(path));
        }
        catch (IOException e)
        {
            System.out.println("Files.getLastModifiedTime(path): " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private static void dumpMountedPathDetails(URI uri)
    {
        System.out.println("Mounting: " + uri);
        Map<String,String> env = new HashMap<>();
        try (FileSystem fs = FileSystems.newFileSystem(uri, env))
        {
            dumpPathDetails("root of mount", fs.getPath("/"));
            dumpPathDetails("Specific URI: " + uri, Path.of(uri));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
