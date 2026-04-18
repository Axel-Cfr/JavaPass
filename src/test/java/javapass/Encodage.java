package javapass;

import static java.lang.System.out;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

public class Encodage {

    public static void main(String[] args) {
        out.println("Java Runtime version " + System.getProperty("java.runtime.version"));
        out.println("-------------------------------------------------");
        out.println("Charset.defaultCharset()                = " + Charset.defaultCharset());
        out.println("System.getProperty(\"file.encoding\")     = " + System.getProperty("file.encoding"));
        out.println("System.getProperty(\"native.encoding\")   = " + System.getProperty("native.encoding"));
        out.println("System.getProperty(\"jnu.encoding\")      = " + System.getProperty("sun.jnu.encoding"));
        out.println("System.getProperty(\"stdout.encoding\")   = " + System.getProperty("stdout.encoding"));
        out.println("System.getProperty(\"stdin.encoding\")    = " + System.getProperty("stdin.encoding"));
        out.println("System.getProperty(\"stderr.encoding\")   = " + System.getProperty("stderr.encoding"));
        out.println("System.console().charset()              = " + System.console().charset());

        assertTrue( true );
    }
}