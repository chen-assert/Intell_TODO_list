package src;

public class Account {
    public String username;
    public String password;
    public int cookie;
    public String email;
    public Account(String password, int cookie) {
        this.password = password;
        this.cookie = cookie;
    }
    public Account(String password, int cookie,String email) {
        this(password,cookie);
        this.email=email;
    }
}
