package tests;

import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.testng.annotations.Test;

public class Test1 {
	
	@Test
	public void passAlways()
	{
		assertTrue( 3== 3);
	}
	
	@Test
	public void randomfailTest() throws InterruptedException
	{
		Random r = new Random();
		int sleep = -1;
		while ( (sleep = r.nextInt(10000) ) < 2000 );
		System.out.println("Sleeping : " + sleep);
		Thread.sleep(sleep);
		assertTrue( sleep % 2 == 0,  "failed because of odd number " + sleep);
	}
	
	@Test ( dependsOnMethods="randomfailTest")
	public void testskip()
	{
		assertTrue( 2==2);
	}
	
	@Test ( groups="excluded")
	public void excluded() 
	{
		System.out.println("whatever!");
	}

}
