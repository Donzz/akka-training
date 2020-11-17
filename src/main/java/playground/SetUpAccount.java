package playground;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import playground.accounts.AccountEntity;
import playground.painter.BitmapEntity;
import support.AccountHandlerImpl;
import support.web.RequestHandlerFactory;

public class SetUpAccount {
    public static RequestHandlerFactory setup(ActorSystem system) {
        ActorRef accountsActor = system.actorOf(Props.create(AccountEntity.class, AccountEntity::new), "account");
        ActorRef transfersActor = system.deadLetters();
        return connectionActorContext -> new AccountHandlerImpl(accountsActor, transfersActor, connectionActorContext.self());
    }
}
