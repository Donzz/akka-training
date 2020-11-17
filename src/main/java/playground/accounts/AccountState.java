package playground.accounts;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountState {

    private int value;

    public AccountState() {
    }

    @JsonCreator
    public AccountState(int value) {
        this.value = value;
    }


    public void deposit(int value) {
        this.value += value;
    }

    public void withdraw(int value) {
        this.value -= value;
    }

    public void validateGetBalance(String accountNumber) throws AccountException {

    }

    public void validateDeposit(String accountNumber, int value) throws AccountException {

    }

    public void validateWithdraw(String accountNumber, int value) throws AccountException {
        if (this.value < value) {
            throw new AccountException("Too low money");
        }
    }

    public int getValue() {
        return this.value;
    }
}
