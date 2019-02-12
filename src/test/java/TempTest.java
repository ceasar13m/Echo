import org.junit.Test;

public class TempTest {

    @Test
    public void test0(){
        String[] split = "/signup/{\"login\":\"dfd\",\"password\":\"324\"}".split("/");
        for(String s : split) {
            System.out.println(s);
        }
    }

}
