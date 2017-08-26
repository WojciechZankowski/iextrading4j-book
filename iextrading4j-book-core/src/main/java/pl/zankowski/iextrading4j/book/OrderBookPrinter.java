package pl.zankowski.iextrading4j.book;

import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Wojciech Zankowski
 */
public class OrderBookPrinter {

    private static final String BID_HEADER = "Bid";
    private static final String ASK_HEADER = "Ask";

    public static String printBook(OrderBook orderBook) {
        Iterator<PriceLevel> bidPriceLevels = orderBook.getBidLevels().iterator();
        Iterator<PriceLevel> askPriceLevels = orderBook.getAskLevels().iterator();
        List<String> bids = new ArrayList<>();
        List<String> asks = new ArrayList<>();
        int maxSize = 10;
        while (bidPriceLevels.hasNext() || askPriceLevels.hasNext()) {
            if (bidPriceLevels.hasNext()) {
                String line = formatBidLevel(bidPriceLevels.next());
                maxSize = Math.max(maxSize, line.length());
                bids.add(line);
            }
            if (askPriceLevels.hasNext()) {
                String line = formatAskLevel(askPriceLevels.next());
                maxSize = Math.max(maxSize, line.length());
                asks.add(line);
            }
        }
        return prepareBook(maxSize, orderBook.getSymbol(), bids.iterator(), asks.iterator());
    }

    private static String prepareBook(int maxSize, String symbol, Iterator<String> bidStringIterator, Iterator<String> askStringIterator) {
        StringBuilder finalBook = new StringBuilder();
        finalBook.append("Order Book: " + symbol + " \n");
        finalBook.append(BID_HEADER);
        finalBook.append(fillWith("", maxSize - BID_HEADER.length(), ' '));
        finalBook.append("| ").append(ASK_HEADER).append("\n");
        finalBook.append(fillWith("", maxSize, '-'));
        finalBook.append("+");
        finalBook.append(fillWith("", maxSize, '-'));
        finalBook.append("\n");
        while (bidStringIterator.hasNext() || askStringIterator.hasNext()) {
            if (bidStringIterator.hasNext()) {
                finalBook.append(fillWith(bidStringIterator.next(), maxSize, ' '));
            } else {
                finalBook.append(fillWith("", maxSize, ' '));
            }
            finalBook.append("|");
            if (askStringIterator.hasNext()) {
                finalBook.append(" ").append(askStringIterator.next());
            }
            finalBook.append("\n");
        }
        return finalBook.toString();
    }

    private static String formatBidLevel(PriceLevel priceLevel) {
        return priceLevel.getSize() + " " + printPrice(priceLevel.getPrice()) + " ";
    }

    private static String formatAskLevel(PriceLevel priceLevel) {
        return printPrice(priceLevel.getPrice()) + " " + priceLevel.getSize();
    }

    private static String fillWith(String stringToFill, int size, char fillChar) {
        StringBuilder output = new StringBuilder(stringToFill);
        for (int i = output.length(); i < size; i++) {
            output.append(fillChar);
        }
        return output.toString();
    }

    private static String printPrice(IEXPrice iexPrice) {
        return iexPrice.toString();
    }

}
