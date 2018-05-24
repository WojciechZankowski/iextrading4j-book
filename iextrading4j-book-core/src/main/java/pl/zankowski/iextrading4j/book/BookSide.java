package pl.zankowski.iextrading4j.book;

import pl.zankowski.iextrading4j.book.api.PriceLevel;
import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;
import pl.zankowski.iextrading4j.hist.deep.trading.field.IEXEventFlag;
import pl.zankowski.iextrading4j.hist.deep.trading.message.IEXPriceLevelUpdateMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeMap;

class BookSide {

    private final TreeMap<IEXPrice, PriceLevel> offers;
    private final Queue<PriceLevel> queue = new LinkedList<>();

    BookSide(final Comparator<IEXPrice> comparator) {
        this.offers = new TreeMap<>(comparator);
    }

    void priceLevelUpdate(final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage) {
        if (iexPriceLevelUpdateMessage.getIexEventFlag() == IEXEventFlag.EVENT_PROCESSING_COMPLETE) {
            drainPriceLevelQueue();
            processPriceLevelToOffers(toPriceLevel(iexPriceLevelUpdateMessage));
        } else if (iexPriceLevelUpdateMessage.getIexEventFlag() == IEXEventFlag.ORDER_BOOK_IS_PROCESSING_EVENT) {
            addEventToQueue(iexPriceLevelUpdateMessage);
        } else {
            throw new IllegalArgumentException("Unknown Event Flag. Cannot process price level update");
        }
    }

    List<PriceLevel> getLevels() {
        return new ArrayList<>(offers.values());
    }

    PriceLevel getBestOffer() {
        return offers.firstEntry().getValue();
    }


    private void drainPriceLevelQueue() {
        while (!queue.isEmpty()) {
            final PriceLevel priceLevel = queue.poll();
            processPriceLevelToOffers(priceLevel);
        }
    }

    private void processPriceLevelToOffers(final PriceLevel priceLevel) {
        if (priceLevel.getSize() == 0) {
            offers.remove(priceLevel.getPrice());
        } else {
            offers.put(priceLevel.getPrice(), priceLevel);
        }
    }

    private void addEventToQueue(final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage) {
        queue.add(toPriceLevel(iexPriceLevelUpdateMessage));
    }

    private PriceLevel toPriceLevel(final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage) {
        return new PriceLevel(
                iexPriceLevelUpdateMessage.getSymbol(),
                iexPriceLevelUpdateMessage.getTimestamp(),
                iexPriceLevelUpdateMessage.getIexPrice(),
                iexPriceLevelUpdateMessage.getSize());
    }

}
