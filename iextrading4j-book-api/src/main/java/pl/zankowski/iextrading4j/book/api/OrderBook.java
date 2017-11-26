package pl.zankowski.iextrading4j.book.api;

import java.util.List;

public interface OrderBook {

    String getSymbol();

    List<PriceLevel> getBidLevels();

    List<PriceLevel> getAskLevels();

    PriceLevel getBestAskOffer();

    PriceLevel getBestBidOffer();

}
