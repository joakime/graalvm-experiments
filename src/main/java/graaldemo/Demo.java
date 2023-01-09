package graaldemo;

import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.util.resource.Resource;

public class Demo
{
    public static void main( String[] args ) throws URISyntaxException
    {
        String versionResourceRef = "org/eclipse/jetty/version/build.properties";
        System.out.println("-- Looking for: " + versionResourceRef);
        URL urlVersion = Thread.currentThread().getContextClassLoader().getResource(versionResourceRef);
        if (urlVersion == null)
            System.out.println("WARNING: Unable to find resource: " + versionResourceRef);
        else
            System.out.println("FOUND: " + urlVersion.toURI());

        System.out.println("-- Looking for: PathResource.class");
        URL urlPathResource = Resource.class.getResource("PathResource.class");
        if (urlPathResource == null)
            System.out.println("WARNING: Unable to find PathResource.class");
        else
            System.out.println("FOUND: " + urlPathResource.toURI());
    }
}
