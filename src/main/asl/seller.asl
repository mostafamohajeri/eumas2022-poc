item("Book").
price_of("Book",5).
norm_agent("SellerAdvisor").


+buyer(Buyer) =>
    for(Item in item(Item)) {
        !offer(Buyer,Item);
    }.

+!offer(Buyer,Item) : price_of(Item,Price) && norm_agent(NormAgent) =>
    Permission = #coms.ask(NormAgent, permitted(offer(Self,Buyer,Item,Price)));
    !offer(Self,Buyer,Item,Price,Permission).

+!offer(Self,Buyer,Item,Price,true) : norm_agent(NormAgent) =>
    #coms.achieve(NormAgent, perform(offer(Self,Buyer,Item,Price)));
    #coms.inform(Buyer, offer(Item,Price)).

+accept(Item,Price) : price_of(Item,Price) && norm_agent(NormAgent) =>
    Response = #coms.ask(NormAgent, perform(accept(Source,Self,Item,Price)));
    #coms.inform(Source,acknowledge(accept(Item,Price),Response)).

+!deliver(Buyer,Item) => #println("need to deliver: " + delivery(Buyer,Item)).

+!expect_pay(Buyer,Price) => #println("expecting payment: " + payment(Buyer,Price)).