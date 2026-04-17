package javapass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LocalDateTime_Test
    extends TestCase
{
    public LocalDateTime_Test( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( LocalDateTime_Test.class );
    }

    public void testApp()
    {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = localDate.format(formatterDate);

        LocalTime localTime = LocalTime.now();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = localTime.format(formatterTime);
        
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
        String formattedString = localDateTime.format(formatter);
        System.out.println(formattedString);
        assertEquals(formattedDate+" à "+formattedTime, formattedString);
    }
}