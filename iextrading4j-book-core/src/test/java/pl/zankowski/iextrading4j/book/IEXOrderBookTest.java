package pl.zankowski.iextrading4j.book;

import org.junit.Test;
import pl.zankowski.iextrading4j.hist.api.IEXMessageType;
import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;
import pl.zankowski.iextrading4j.hist.deep.trading.field.IEXEventFlag;
import pl.zankowski.iextrading4j.hist.deep.trading.message.IEXPriceLevelUpdateMessage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.zankowski.iextrading4j.book.builder.IEXPriceLevelUpdateMessageDataBuilder.anIEXPriceLevelUpdateMessage;
import static pl.zankowski.iextrading4j.book.builder.IEXPriceLevelUpdateMessageDataBuilder.defaultIEXPriceLevelUpdateMessage;

/**
 * @author Wojciech Zankowski
 */
public class IEXOrderBookTest {

    private final String TEST_SYMBOL = "AAPL";

    @Test
    public void shouldSuccessfullyAddBidQuote() {
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage = defaultIEXPriceLevelUpdateMessage();
        final IEXOrderBook orderBook = new IEXOrderBook(TEST_SYMBOL);

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage);

        List<PriceLevel> bidPriceLevels = orderBook.getBidLevels();
        assertThat(bidPriceLevels).hasSize(1);

        PriceLevel priceLevel = bidPriceLevels.get(0);
        assertThat(priceLevel.getTimestamp()).isEqualTo(iexPriceLevelUpdateMessage.getTimestamp());
        assertThat(priceLevel.getSize()).isEqualTo(iexPriceLevelUpdateMessage.getSize());
        assertThat(priceLevel.getPrice()).isEqualTo(iexPriceLevelUpdateMessage.getIexPrice());
        assertThat(orderBook.getAskLevels()).isEmpty();
    }

    @Test
    public void shouldSuccessfullyAddAskQuote() {
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage = anIEXPriceLevelUpdateMessage()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL).build();
        final IEXOrderBook orderBook = new IEXOrderBook(TEST_SYMBOL);

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage);

        List<PriceLevel> askPriceLevels = orderBook.getAskLevels();
        assertThat(askPriceLevels).hasSize(1);

        PriceLevel priceLevel = askPriceLevels.get(0);
        assertThat(priceLevel.getTimestamp()).isEqualTo(iexPriceLevelUpdateMessage.getTimestamp());
        assertThat(priceLevel.getSize()).isEqualTo(iexPriceLevelUpdateMessage.getSize());
        assertThat(priceLevel.getPrice()).isEqualTo(iexPriceLevelUpdateMessage.getIexPrice());
        assertThat(orderBook.getBidLevels()).isEmpty();
    }

    @Test
    public void shouldAddBidPriceLevelToQueueAndProcessItLater() {
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_1 = anIEXPriceLevelUpdateMessage()
                .withIEXPrice(new IEXPrice(17534L)).withIEXEventFlag(IEXEventFlag.ORDER_BOOK_IS_PROCESSING_EVENT).build();
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_2 = defaultIEXPriceLevelUpdateMessage();
        final IEXOrderBook orderBook = new IEXOrderBook(TEST_SYMBOL);

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_1);
        assertThat(orderBook.getBidLevels()).isEmpty();

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_2);
        assertThat(orderBook.getBidLevels()).hasSize(2);

        List<PriceLevel> bidPriceLevels = orderBook.getBidLevels();
        assertThat(bidPriceLevels.get(0).getPrice()).isEqualTo(iexPriceLevelUpdateMessage_1.getIexPrice());
        assertThat(bidPriceLevels.get(1).getPrice()).isEqualTo(iexPriceLevelUpdateMessage_2.getIexPrice());
    }

    @Test
    public void shouldAddAskPriceLevelToQueueAndProcessItLater() {
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_1 = anIEXPriceLevelUpdateMessage()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL).withIEXPrice(new IEXPrice(17534L))
                .withIEXEventFlag(IEXEventFlag.ORDER_BOOK_IS_PROCESSING_EVENT).build();
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_2 = anIEXPriceLevelUpdateMessage()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL).build();;
        final IEXOrderBook orderBook = new IEXOrderBook(TEST_SYMBOL);

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_1);
        assertThat(orderBook.getAskLevels()).isEmpty();

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_2);
        assertThat(orderBook.getAskLevels()).hasSize(2);

        List<PriceLevel> askPriceLevels = orderBook.getAskLevels();
        assertThat(askPriceLevels.get(0).getPrice()).isEqualTo(iexPriceLevelUpdateMessage_2.getIexPrice());
        assertThat(askPriceLevels.get(1).getPrice()).isEqualTo(iexPriceLevelUpdateMessage_1.getIexPrice());
    }

    @Test
    public void shouldUpdateBidPriceLevel() {
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_1 = anIEXPriceLevelUpdateMessage().withSize(200).build();
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_2 = defaultIEXPriceLevelUpdateMessage();
        final IEXOrderBook orderBook = new IEXOrderBook(TEST_SYMBOL);

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_1);
        assertThat(orderBook.getBidLevels().get(0).getSize()).isEqualTo(iexPriceLevelUpdateMessage_1.getSize());

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_2);
        assertThat(orderBook.getBidLevels().get(0).getSize()).isEqualTo(iexPriceLevelUpdateMessage_2.getSize());
    }

    @Test
    public void shouldUpdateAskPriceLevel() {
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_1 = anIEXPriceLevelUpdateMessage()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL).withSize(200).build();
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_2 = anIEXPriceLevelUpdateMessage()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL).build();
        final IEXOrderBook orderBook = new IEXOrderBook(TEST_SYMBOL);

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_1);
        assertThat(orderBook.getAskLevels().get(0).getSize()).isEqualTo(iexPriceLevelUpdateMessage_1.getSize());

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_2);
        assertThat(orderBook.getAskLevels().get(0).getSize()).isEqualTo(iexPriceLevelUpdateMessage_2.getSize());
    }

    @Test
    public void shouldRemoveBidPriceLevel() {
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_1 = anIEXPriceLevelUpdateMessage().withSize(200).build();
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_2 = anIEXPriceLevelUpdateMessage().withSize(0).build();
        final IEXOrderBook orderBook = new IEXOrderBook(TEST_SYMBOL);

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_1);
        assertThat(orderBook.getBidLevels().get(0).getSize()).isEqualTo(iexPriceLevelUpdateMessage_1.getSize());

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_2);
        assertThat(orderBook.getBidLevels()).isEmpty();
    }

    @Test
    public void shouldRemoveAskPriceLevel() {
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_1 = anIEXPriceLevelUpdateMessage()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL).withSize(200).build();
        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage_2 = anIEXPriceLevelUpdateMessage()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL).withSize(0).build();
        final IEXOrderBook orderBook = new IEXOrderBook(TEST_SYMBOL);

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_1);
        assertThat(orderBook.getAskLevels().get(0).getSize()).isEqualTo(iexPriceLevelUpdateMessage_1.getSize());

        orderBook.priceLevelUpdate(iexPriceLevelUpdateMessage_2);
        assertThat(orderBook.getAskLevels()).isEmpty();
    }

}
