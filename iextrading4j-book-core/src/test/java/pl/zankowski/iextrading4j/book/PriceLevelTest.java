package pl.zankowski.iextrading4j.book;

import org.junit.Test;
import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;
import pl.zankowski.iextrading4j.hist.api.message.IEXMessageHeader;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.zankowski.iextrading4j.book.builder.PriceLevelDataBuilder.defaultPriceLevel;

/**
 * @author Wojciech Zankowski
 */
public class PriceLevelTest {

    @Test
    public void shouldSuccessfullyCreatePriceLevelInstance() {
        final String symbol = "IBM";
        final long timestamp = 123456789L;
        final IEXPrice price = new IEXPrice(12345);
        final int size = 100;

        PriceLevel priceLevel = new PriceLevel(symbol, timestamp, price, size);

        assertThat(priceLevel.getPrice()).isEqualTo(price);
        assertThat(priceLevel.getSize()).isEqualTo(size);
        assertThat(priceLevel.getSymbol()).isEqualTo(symbol);
        assertThat(priceLevel.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    public void shouldTwoInstancesWithSameValuesBeEqual() {
        PriceLevel priceLevel_1 = defaultPriceLevel();
        PriceLevel priceLevel_2 = defaultPriceLevel();

        assertThat(priceLevel_1).isEqualTo(priceLevel_2);
        assertThat(priceLevel_1.hashCode()).isEqualTo(priceLevel_2.hashCode());
    }

}
