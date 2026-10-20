package net.lapointep.paulsmod.util;

public class BankAccount {
    private final String owner;
    private int balance;

    public BankAccount(String owner, int balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }

    public int withdraw(int amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return amount;
        } else {
            int withdrawn = this.balance;
            this.balance = 0;
            return withdrawn;
        }
    }

    public int getBalance() {
        return this.balance;
    }

    public String getOwner() {
        return this.owner;
    }
}
