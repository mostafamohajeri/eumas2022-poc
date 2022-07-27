
+?permitted(A) : enabled(A) =>
    #coms.respond(true).

+?permitted(A) =>
    #coms.respond(false).

+?perform(A) : enabled(A) =>
    #coms.respond(true);
    #executionContext.beliefBase.query(A).

+?perform(A) =>
    #coms.respond(false).


+!perform(A) : enabled(A) =>
    #executionContext.beliefBase.query(A).

+!perform(A) =>
    #println(failed(A)).

+duty_to_deliver(person(Seller),person(Buyer),item(Item)) =>
    #coms.achieve(Seller,deliver(Buyer,Item)).

+duty_to_pay(person(Buyer),person(Seller),amount(Price)) =>
    #coms.achieve(Seller,expect_pay(Buyer,Price)).




