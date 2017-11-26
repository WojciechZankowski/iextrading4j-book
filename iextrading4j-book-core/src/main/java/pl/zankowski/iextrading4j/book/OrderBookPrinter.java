package pl.zankowski.iextrading4j.book;

import pl.zankowski.iextrading4j.book.api.OrderBook;
import pl.zankowski.iextrading4j.book.api.PriceLevel;
import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderBookPrinter {

    private static final String BID_HEADER = "Bid";
    private static final String ASK_HEADER = "Ask";

    public static String printBook(final OrderBook orderBook) {
        final Iterator<PriceLevel> bidPriceLevels = orderBook.getBidLevels().iterator();
        final Iterator<PriceLevel> askPriceLevels = orderBook.getAskLevels().iterator();
        final List<String> bids = new ArrayList<>();
        final List<String> asks = new ArrayList<>();

        int maxSize = 10;

        while (bidPriceLevels.hasNext() || askPriceLevels.hasNext()) {
            if (bidPriceLevels.hasNext()) {
                final String line = formatBidLevel(bidPriceLevels.next());
                maxSize = Math.max(maxSize, line.length());
                bids.add(line);
            }
            if (askPriceLevels.hasNext()) {
                final String line = formatAskLevel(askPriceLevels.next());
                maxSize = Math.max(maxSize, line.length());
                asks.add(line);
            }
        }
        return prepareBook(maxSize, orderBook.getSymbol(), bids.iterator(), asks.iterator());
    }

    private static String prepareBook(final int maxSize, final String symbol, final Iterator<String> bidStringIterator,
            final Iterator<String> askStringIterator) {
        final StringBuilder finalBook = new StringBuilder()
                .append("Order Book: " + symbol + " \n")
                .append(BID_HEADER)
                .append(fillWith("", maxSize - BID_HEADER.length(), ' '))
                .append("| ").append(ASK_HEADER).append("\n")
                .append(fillWith("", maxSize, '-'))
                .append("+")
                .append(fillWith("", maxSize, '-'))
                .append("\n");

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

    private static String formatBidLevel(final PriceLevel priceLevel) {
        return priceLevel.getSize() + " " + printPrice(priceLevel.getPrice()) + " ";
    }

    private static String formatAskLevel(final PriceLevel priceLevel) {
        return printPrice(priceLevel.getPrice()) + " " + priceLevel.getSize();
    }

    private static String fillWith(final String stringToFill, final int size, final char fillChar) {
        final StringBuilder output = new StringBuilder(stringToFill);

        for (int i = output.length(); i < size; i++) {
            output.append(fillChar);
        }

        return output.toString();
    }

    private static String printPrice(final IEXPrice iexPrice) {
        return iexPrice.toString();
    }

}
