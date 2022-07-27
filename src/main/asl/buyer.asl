
needed_item("Book").
fair_price("Book", 5).
have_money(10).

+offer(Item ,Price) =>
    #coms.achieve("BuyerAdvisor", perform(offer(Source, Self, Item, Price))).

+purchase(Seller,Item,Price) => !consider_buying(Seller,Item,Price).

+!consider_buying(Seller, I, P) :
  needed_item(I) && fair_price(I, FP) && P =< FP && have_money(M) && M >= P =>
    #coms.inform(Seller, accept(I, P));
    +pending(accept(Seller,I, P)).

+!consider_buying(Seller, I, P) => #println(failed(consider_buying(Seller, I, P))).

+acknowledge(accept(Item,Price),true) : pending(accept(Source,Item, Price)) =>
    -pending(accept(Source, Item, Price));
    #coms.achieve("BuyerAdvisor", perform(accept(Self, Source, Item, Price))).

+duty_to_deliver(Seller,Buyer,I) : Source == "BuyerAdvisor" && Buyer == Self =>
    +expected_delivery(Seller,I).

+delivery(Sender, Item) : expected_delivery(Sender, Item) =>
    -expected_delivery(Sender, Item);
    #coms.achieve("BuyerAdvisor", perform(deliver(Sender, Self, Item))).

+duty_to_pay(Buyer, Seller, P) : Source == "BuyerAdvisor" && Buyer == Self  =>
    !pay(Seller, P).

+!pay(Seller, P) : have_money(M) && M >= P =>
    #println(pay(Seller, P));
    #coms.achieve("BuyerAdvisor", perform(pay(Self, Seller, P))).
