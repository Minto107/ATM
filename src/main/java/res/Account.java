package res;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Account implements Serializable {

    int PIN, balance;
    String name, sur;

    public Account(int PIN, String name, String sur) {
        this.PIN = PIN;
        balance = 500;
        this.name = name;
        this.sur = sur;
    }

    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSur() {
        return sur;
    }

    public void setSur(String sur) {
        this.sur = sur;
    }

    /*@Override
    public String toString() {
        return "Account{" +
                "PIN=" + PIN +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                ", sur='" + sur + '\'' +
                '}';
    }*/
}
