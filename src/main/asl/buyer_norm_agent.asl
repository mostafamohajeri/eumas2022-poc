
+?permitted(A) : enabled(A) =>
    #coms.respond(res(A,true)).

+?permitted(A) =>
    #coms.respond(res(A,false)).

+?perform(A) : enabled(A) =>
    #coms.respond(res(A,true));
    #executionContext.beliefBase.query(A).

+?perform(A) =>
    #coms.respond(res(A,false)).


+!perform(A) : enabled(A) =>
    #executionContext.beliefBase.query(A).

+!perform(A) =>
    #println(failed(A)).

+duty_to_deliver(person(Seller),person(Buyer),item(Item)) =>
    #coms.achieve(Buyer,expect_deliver(Seller,Item)).

+duty_to_pay(person(Buyer),person(Seller),amount(Price)) =>
    #coms.achieve(Buyer,pay(Seller,Price)).

+accept(person(Buyer),person(Seller),item(Item),amount(Price)) =>
    #println("Purchase created: " + Item + ", informing " + Seller);
    #coms.inform(Buyer,purchase(Seller,Item,Price)).





