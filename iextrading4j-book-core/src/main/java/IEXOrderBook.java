import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;

import java.util.*;

/**
 * @author Wojciech Zankowski
 */
public class IEXOrderBook implements OrderBook {

    private final Map<IEXPrice, PriceLevel> bidOffers;
    private final Map<IEXPrice, PriceLevel> askOffers;

    private Queue<>

    public IEXOrderBook() {
        bidOffers = new TreeMap<>();
        askOffers = new TreeMap<>();
    }

    @Override
    public List<PriceLevel> getBidLevels() {
        return new ArrayList<>(bidOffers.values());
    }

    @Override
    public List<PriceLevel> getAskLevels() {
        return new ArrayList<>(askOffers.values());
    }


}
