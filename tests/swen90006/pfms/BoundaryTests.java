package swen90006.pfms;

import java.util.List;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;

import org.junit.*;
import static org.junit.Assert.*;

//By extending PartitioningTests, we inherit the tests from that class
public class BoundaryTests
    extends PartitioningTests
{
    //Add another test
    @Test public void anotherTest()
    {
	//include a message for better feedback
	final int expected = 2;
	final int actual = 2;
	assertEquals("Some failure message", expected, actual);
    }
}
