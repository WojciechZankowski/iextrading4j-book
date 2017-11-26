package pl.zankowski.iextrading4j.book.sample;

import pl.zankowski.iextrading4j.book.IEXOrderBook;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderBookStore {

    private final Map<String, IEXOrderBook> orderBookMap = new HashMap<>();

    public Optional<IEXOrderBook> getOrderBook(String symbol) {
        return Optional.ofNullable(orderBookMap.get(symbol));
    }

    public void updateOrderBook(IEXOrderBook iexOrderBook) {
        orderBookMap.put(iexOrderBook.getSymbol(), iexOrderBook);
    }

}
