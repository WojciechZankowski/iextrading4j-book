package pl.zankowski.iextrading4j.book;

import pl.zankowski.iextrading4j.book.api.OrderBook;
import pl.zankowski.iextrading4j.book.api.PriceLevel;
import pl.zankowski.iextrading4j.hist.deep.trading.message.IEXPriceLevelUpdateMessage;

import java.util.Comparator;
import java.util.List;

public class IEXOrderBook implements OrderBook {

    private final String symbol;

    private final BookSide bidSide;
    private final BookSide askSide;

    public IEXOrderBook(final String symbol) {
        this.symbol = symbol;
        this.bidSide = new BookSide(Comparator.reverseOrder());
        this.askSide = new BookSide(Comparator.naturalOrder());
    }

    public void priceLevelUpdate(final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage) {
        switch (iexPriceLevelUpdateMessage.getIexMessageType()) {
            case PRICE_LEVEL_UPDATE_SELL:
                askSide.priceLevelUpdate(iexPriceLevelUpdateMessage);
                break;
            case PRICE_LEVEL_UPDATE_BUY:
                bidSide.priceLevelUpdate(iexPriceLevelUpdateMessage);
                break;
            default:
                throw new IllegalArgumentException("Unknown Price Level Update type. Cannot process.");
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public List<PriceLevel> getBidLevels() {
        return bidSide.getLevels();
    }

    @Override
    public List<PriceLevel> getAskLevels() {
        return askSide.getLevels();
    }

    @Override
    public PriceLevel getBestAskOffer() {
        return askSide.getBestOffer();
    }

    @Override
    public PriceLevel getBestBidOffer() {
        return bidSide.getBestOffer();
    }

    @Override
    public String toString() {
        return OrderBookPrinter.printBook(this);
    }
}
