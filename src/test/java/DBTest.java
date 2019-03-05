import com.company.DBWorker;
import com.company.model.User;
import org.junit.Test;

import java.lang.annotation.Target;
import java.sql.SQLException;

public class DBTest {
    @Test
    public void test1() throws SQLException {
        DBWorker dbWorker = new DBWorker();
        System.out.println(dbWorker.isUserExists("ainur", "112"));
    }

    @Test
    public void test2() throws SQLException {
        DBWorker dbWorker = new DBWorker();
        System.out.println(dbWorker.isLoginPasswordValid("ainur", "112"));
    }

    @Test
    public void test3() throws SQLException {

        User user = new User();
        user.login = "ilnaz";
        user.password = "123";

        DBWorker dbWorker = new DBWorker();
        System.out.println(dbWorker.addUser(user));
    }

    @Test
    public void test4() throws SQLException {
        User user = new User();
        user.login = "ilnaz";
        user.password = "123";
        DBWorker dbWorker = new DBWorker();
        System.out.println(dbWorker.addUser(user));
    }

    @Test
    public void test5() throws SQLException {
        DBWorker dbWorker = new DBWorker();
        System.out.println(dbWorker.isUserExists("ainur", "112"));
    }


    @Test
    public void test6() throws SQLException {
        DBWorker dbWorker = new DBWorker();
        System.out.println(dbWorker.isUserExists("ilnaz", "123"));
    }


}
