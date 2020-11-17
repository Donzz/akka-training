package playground.transfers;

public interface TransferEvents {

    static class TransferRequested implements TransferEvents {
        private final String transferId;
        private final String fromAccountNumber;
        private final String toAccountNumber;
        private final int value;

        public TransferRequested(String transferId, String fromAccountNumber, String toAccountNumber, int value) {
            this.transferId = transferId;
            this.fromAccountNumber = fromAccountNumber;
            this.toAccountNumber = toAccountNumber;
            this.value = value;
        }

        public String getTransferId() {
            return transferId;
        }

        public String getFromAccountNumber() {
            return fromAccountNumber;
        }

        public String getToAccountNumber() {
            return toAccountNumber;
        }

        public int getValue() {
            return value;
        }

    }

}
