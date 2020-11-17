package playground.transfers;

import akka.actor.ActorRef;
import akka.persistence.AbstractPersistentActor;


public class TransferEntity extends AbstractPersistentActor {

    private final String transferId;
    private final ActorRef accountA;
    private final ActorRef accountB;

    public TransferEntity(String transferId, ActorRef accountA, ActorRef accountB) {
        this.transferId = transferId;
        this.accountA = accountA;
        this.accountB = accountB;
    }

    public String persistenceId() {
        return context().self().path().name();
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(TransferProtocol.Transfer.class, this::onTransferCommand)
                .build();
    }

    private Receive awaitWithdrawReserved = receiveBuilder()
            .match(TransferProtocol.WithdrawReserved.class, this::onWithdrawReservedCommand )
            .build();

    private void onWithdrawReservedCommand(TransferProtocol.WithdrawReserved cmd) {
//        try {
//            validate(cmd);
//            TransferEvents.WithdrawReserved unsaved = new TransferEvents.WithdrawReserved(cmd.getTransferId());
//            persist(unsaved, saved -> {
//                reactOnTransferRequestedEvent(saved);
//
//            });
//        } catch (Exception e) {
//
//        }
    }


    private void onTransferCommand(TransferProtocol.Transfer cmd) {
        try {
//            validate(cmd);
            TransferEvents.TransferRequested unsaved = new TransferEvents.TransferRequested(cmd.getTransferId(), cmd.getFromAccountNumber(), cmd.getToAccountNumber(), cmd.getValue());
            persist(unsaved, saved -> {
                reactOnTransferRequestedEvent(saved);

            });
        } catch (Exception e) {

        }
    }

    private void reactOnTransferRequestedEvent(TransferEvents.TransferRequested event) {
        getContext().become(awaitWithdrawReserved);

    }

    public Receive createReceiveRecover() {
        return receiveBuilder()
                .build();
    }

    public void whenEmpty(Object command) {
        sender().tell(new TransferResponse.ERROR(null, "Not initialized"), self());
    }
}
