package pl.zankowski.iextrading4j.book;

import pl.zankowski.iextrading4j.book.api.OrderBook;
import pl.zankowski.iextrading4j.book.api.PriceLevel;
import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;
import pl.zankowski.iextrading4j.hist.deep.trading.field.IEXEventFlag;
import pl.zankowski.iextrading4j.hist.deep.trading.message.IEXPriceLevelUpdateMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class IEXOrderBook implements OrderBook {

    private final String symbol;
    private final TreeMap<IEXPrice, PriceLevel> bidOffers;
    private final TreeMap<IEXPrice, PriceLevel> askOffers;

    private final Queue<PriceLevel> bidQueue = new LinkedList<>();
    private final Queue<PriceLevel> askQueue = new LinkedList<>();

    public IEXOrderBook(final String symbol) {
        this.symbol = symbol;
        this.bidOffers = new TreeMap<>(Comparator.reverseOrder());
        this.askOffers = new TreeMap<>();
    }

    public void priceLevelUpdate(final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage) {
        switch (iexPriceLevelUpdateMessage.getIexMessageType()) {
            case PRICE_LEVEL_UPDATE_SELL:
                priceLevelUpdate(iexPriceLevelUpdateMessage, askQueue, askOffers);
                break;
            case PRICE_LEVEL_UPDATE_BUY:
                priceLevelUpdate(iexPriceLevelUpdateMessage, bidQueue, bidOffers);
                break;
            default:
                throw new IllegalArgumentException("Unknown Price Level Update type. Cannot process.");
        }
    }

    private void priceLevelUpdate(final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage,
            final Queue<PriceLevel> priceLevelQueue, final Map<IEXPrice, PriceLevel> offers) {

        if (iexPriceLevelUpdateMessage.getIexEventFlag() == IEXEventFlag.EVENT_PROCESSING_COMPLETE) {
            drainPriceLevelQueue(priceLevelQueue, offers);
            processPriceLevelToOffers(convertToPriceLevel(iexPriceLevelUpdateMessage), offers);
        } else if (iexPriceLevelUpdateMessage.getIexEventFlag() == IEXEventFlag.ORDER_BOOK_IS_PROCESSING_EVENT) {
            addEventToQueue(iexPriceLevelUpdateMessage, priceLevelQueue);
        } else {
            throw new IllegalArgumentException("Unknown Event Flag. Cannot process price level update");
        }
    }

    private void drainPriceLevelQueue(final Queue<PriceLevel> priceLevelQueue, final Map<IEXPrice, PriceLevel> offers) {
        while (!priceLevelQueue.isEmpty()) {
            PriceLevel priceLevel = priceLevelQueue.poll();
            processPriceLevelToOffers(priceLevel, offers);
        }
    }

    private void processPriceLevelToOffers(final PriceLevel priceLevel, final Map<IEXPrice, PriceLevel> offers) {
        if (priceLevel.getSize() == 0) {
            offers.remove(priceLevel.getPrice());
        } else {
            offers.put(priceLevel.getPrice(), priceLevel);
        }
    }

    private void addEventToQueue(final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage,
            final Queue<PriceLevel> priceLevelQueue) {
        priceLevelQueue.add(convertToPriceLevel(iexPriceLevelUpdateMessage));
    }

    private PriceLevel convertToPriceLevel(final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage) {
        return new PriceLevel(iexPriceLevelUpdateMessage.getSymbol(),
                iexPriceLevelUpdateMessage.getTimestamp(),
                iexPriceLevelUpdateMessage.getIexPrice(),
                iexPriceLevelUpdateMessage.getSize());
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public List<PriceLevel> getBidLevels() {
        return new ArrayList<>(bidOffers.values());
    }

    @Override
    public List<PriceLevel> getAskLevels() {
        return new ArrayList<>(askOffers.values());
    }

    @Override
    public PriceLevel getBestAskOffer() {
        return askOffers.firstEntry().getValue();
    }

    @Override
    public PriceLevel getBestBidOffer() {
        return bidOffers.firstEntry().getValue();
    }

    @Override
    public String toString() {
        return OrderBookPrinter.printBook(this);
    }
}
