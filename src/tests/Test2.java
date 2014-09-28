package tests;

import java.util.Random;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Test2 {
	
	@Test
	public void passTest()
	{
		assertTrue( 3== 3);
	}
	
	@Test
	public void failTest() throws InterruptedException
	{
		Random r = new Random();
		int sleep = -1;
		while ( (sleep = r.nextInt(20000) ) < 2000 );
		System.out.println("Sleeping : " + sleep);
		Thread.sleep(sleep);
		fail("simply faild!");
	}
	
	@Test ( dependsOnMethods="failTest")
	public void testskip()
	{
		assertTrue( 2==2);
	}

}
