package playground.accounts;

import akka.persistence.AbstractPersistentActor;

public class AccountEntity extends AbstractPersistentActor {

    private String accountNumber;
    private AccountState state;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        try {
            this.state = new AccountState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String persistenceId() {
        return context().self().path().name();
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(AccountProtocol.Deposit.class, this::onDepositCommand)
                .match(AccountProtocol.Withdraw.class, this::onWithdrawCommand)
                .match(AccountProtocol.GetBalance.class, this::onGetBalanceCommand)
                .build();
    }

    private void onGetBalanceCommand(AccountProtocol.GetBalance cmd) {
        try {
            state.validateGetBalance(cmd.getAccountNumber());
            Events.BalanceRequested unsaved = new Events.BalanceRequested(cmd.getAccountNumber());
            persist(unsaved, saved -> {
                reactOnBalanceRequestedEvent(saved);
                getSender().tell(new AccountResponse.OK(saved.getAccountNumber(), state.getValue()), getSelf());
            });
        } catch (AccountException e) {
            getSender().tell(new AccountResponse.ERROR(cmd.getAccountNumber(), e.getMessage()), getSelf());
        }
    }

    private void onDepositCommand(AccountProtocol.Deposit cmd) {
        try {
            state.validateDeposit(cmd.getAccountNumber(), cmd.getValue());
            Events.Deposited unsaved = new Events.Deposited(cmd.getAccountNumber(), cmd.getValue());
            persist(unsaved, saved -> {
                reactOnDepositedEvent(saved);
                getSender().tell(new AccountResponse.OK(saved.getAccountNumber(), state.getValue()), getSelf());
            });
        } catch (AccountException e) {
            getSender().tell(new AccountResponse.ERROR(cmd.getAccountNumber(), e.getMessage()), getSelf());

        }
    }

    private void onWithdrawCommand(AccountProtocol.Withdraw cmd) {
        try {
            state.validateWithdraw(cmd.getAccountNumber(), cmd.getValue());
            Events.Withdrawn unsaved = new Events.Withdrawn(cmd.getAccountNumber(), cmd.getValue());
            persist(unsaved, saved -> {
                reactOnWithdrawnEvent(saved);
                getSender().tell(new AccountResponse.OK(saved.getAccountNumber(), state.getValue()), getSelf());
            });
        } catch (AccountException e) {
            getSender().tell(new AccountResponse.ERROR(cmd.getAccountNumber(), e.getMessage()), getSelf());
        }
    }

    public Receive createReceiveRecover() {
        return receiveBuilder()
                .match(Events.Deposited.class, this::reactOnDepositedEvent)
                .match(Events.Withdrawn.class, this::reactOnWithdrawnEvent)
                .match(Events.BalanceRequested.class, this::reactOnBalanceRequestedEvent)
                .build();
    }

    private void reactOnBalanceRequestedEvent(Events.BalanceRequested event) {

    }

    private void reactOnDepositedEvent(Events.Deposited event) {
        state.deposit(event.getValue());
    }

    private void reactOnWithdrawnEvent(Events.Withdrawn event) {
        state.withdraw(event.getValue());
    }

}
