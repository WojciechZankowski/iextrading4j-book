package pl.zankowski.iextrading4j.book;

import java.util.List;

/**
 * @author Wojciech Zankowski
 */
public interface OrderBook {

    String getSymbol();

    List<PriceLevel> getBidLevels();

    List<PriceLevel> getAskLevels();

    PriceLevel getBestAskOffer();

    PriceLevel getBestBidOffer();

}
