import java.util.List;

/**
 * @author Wojciech Zankowski
 */
public interface OrderBook {

    List<PriceLevel> getBidLevels();

    List<PriceLevel> getAskLevels();

}
