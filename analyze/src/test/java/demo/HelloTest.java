package demo;

import static org.junit.Assert.assertEquals;

import com.gm.Hello;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class HelloTest {
    @Test
    public void testSum() {
        Hello hello = new Hello();
        int sum = hello.sum(1, 2);
        assertEquals(3,sum);
    }
}
