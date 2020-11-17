package playground.accounts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public interface Events {

    public String getAccountNumber();

    @JsonSerialize
    static class Deposited implements Events {

        private final String accountNumber;
        private final int value;

        @JsonCreator
        public Deposited(@JsonProperty("accountNumber") String accountNumber, @JsonProperty("value") int value) {
            this.accountNumber = accountNumber;
            this.value = value;
        }

        @Override
        public String getAccountNumber() {
            return accountNumber;
        }

        public int getValue() {
            return value;
        }
    }

    @JsonSerialize
    static class Withdrawn implements Events {

        private final String accountNumber;
        private final int value;

        @JsonCreator
        public Withdrawn(@JsonProperty("accountNumber") String accountNumber, @JsonProperty("value") int value) {
            this.accountNumber = accountNumber;
            this.value = value;
        }

        @Override
        public String getAccountNumber() {
            return accountNumber;
        }

        public int getValue() {
            return value;
        }
    }

    @JsonSerialize
    static class BalanceRequested implements Events {

        private final String accountNumber;

        @JsonCreator
        public BalanceRequested(@JsonProperty("accountNumber") String accountNumber) {
            this.accountNumber = accountNumber;
        }

        @Override
        public String getAccountNumber() {
            return accountNumber;
        }
    }

}
