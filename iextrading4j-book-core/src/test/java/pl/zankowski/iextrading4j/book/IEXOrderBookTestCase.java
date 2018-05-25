package pl.zankowski.iextrading4j.book;

import org.junit.Before;
import org.junit.Test;
import pl.zankowski.iextrading4j.book.api.PriceLevel;
import pl.zankowski.iextrading4j.book.builder.IEXPriceLevelUpdateMessageDataBuilder;
import pl.zankowski.iextrading4j.hist.api.IEXMessageType;
import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;
import pl.zankowski.iextrading4j.hist.deep.trading.field.IEXEventFlag;
import pl.zankowski.iextrading4j.hist.deep.trading.message.IEXPriceLevelUpdateMessage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 * 1. Order Book for symbol ZIEXT
 * Sell 100 @ 25.30
 * Sell 100 @ 25.20
 * Sell 100 @ 25.10
 * Buy 100 @ 25.00
 * Buy 100 @ 24.90
 * IEX BBO: Buy 100 @ 25.00 x Sell 100 @ 25.10
 *
 * 2. Price Level Update on the Sell Side received: Event Flags: 0x0, Price: 25.10, Shares: 0
 * Sell 100 @ 25.30
 * Sell 100 @ 25.20
 * Buy 100 @ 25.00
 * Buy 100 @ 24.90
 * IEX BBO: Buy 100 @ 25.00 x Sell 100 @ 25.10 (Reason: ZIEXT Order Book is in transition)
 *
 * 3. Price Level Update on the Sell Side received: Event Flags: 0x1, Price: 25.20, Shares: 0
 * Sell 100 @ 25.30
 * Buy 100 @ 25.00
 * Buy 100 @ 24.90
 * IEX BBO: Buy 100 @ 25.00 x Sell 100 @ 25.30 (Reason: ZIEXT Order Book transition complete)
 * </pre>
 */
public class IEXOrderBookTestCase {

    private final String SYMBOL = "ZIEXT";
    private IEXOrderBook orderBook;

    @Before
    public void setUp() {
        orderBook = new IEXOrderBook(SYMBOL);
    }

    @Test
    public void testCase_01() {
        // 1 Step
        System.out.println("==FIRST STEP");
        prepareInitialOrderBook();
        printOrderBook();
        assertFirstStep();

        // 2 Step
        System.out.println("==SECOND STEP");
        updateOrderBookWithProcessingEvent();
        printOrderBook();
        assertSecondStep();

        // 3 Step
        System.out.println("==THIRD STEP");
        updateOrderBookWithProcessingComplete();
        printOrderBook();
        assertThirdStep();
    }

    private void prepareInitialOrderBook() {
        final IEXPriceLevelUpdateMessage firstAsk = new IEXPriceLevelUpdateMessageDataBuilder()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL)
                .withIEXEventFlag(IEXEventFlag.EVENT_PROCESSING_COMPLETE)
                .withSymbol(SYMBOL)
                .withIEXPrice(new IEXPrice(253000))
                .withSize(100)
                .build();
        final IEXPriceLevelUpdateMessage secondAsk = new IEXPriceLevelUpdateMessageDataBuilder()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL)
                .withIEXEventFlag(IEXEventFlag.EVENT_PROCESSING_COMPLETE)
                .withSymbol(SYMBOL)
                .withIEXPrice(new IEXPrice(252000))
                .withSize(100)
                .build();
        final IEXPriceLevelUpdateMessage thirdAsk = new IEXPriceLevelUpdateMessageDataBuilder()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL)
                .withIEXEventFlag(IEXEventFlag.EVENT_PROCESSING_COMPLETE)
                .withSymbol(SYMBOL)
                .withIEXPrice(new IEXPrice(251000))
                .withSize(100)
                .build();
        orderBook.priceLevelUpdate(firstAsk);
        orderBook.priceLevelUpdate(secondAsk);
        orderBook.priceLevelUpdate(thirdAsk);

        final IEXPriceLevelUpdateMessage firstBid = new IEXPriceLevelUpdateMessageDataBuilder()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_BUY)
                .withIEXEventFlag(IEXEventFlag.EVENT_PROCESSING_COMPLETE)
                .withSymbol(SYMBOL)
                .withIEXPrice(new IEXPrice(250000))
                .withSize(100)
                .build();
        final IEXPriceLevelUpdateMessage secondBid = new IEXPriceLevelUpdateMessageDataBuilder()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_BUY)
                .withIEXEventFlag(IEXEventFlag.EVENT_PROCESSING_COMPLETE)
                .withSymbol(SYMBOL)
                .withIEXPrice(new IEXPrice(249000))
                .withSize(100)
                .build();
        orderBook.priceLevelUpdate(firstBid);
        orderBook.priceLevelUpdate(secondBid);
    }

    private void assertFirstStep() {
        final PriceLevel bestAskOffer = orderBook.getBestAskOffer();
        assertThat(bestAskOffer.getPrice()).isEqualTo(new IEXPrice(251000));
        assertThat(bestAskOffer.getSize()).isEqualTo(100);

        final PriceLevel bestBidOffer = orderBook.getBestBidOffer();
        assertThat(bestBidOffer.getPrice()).isEqualTo(new IEXPrice(250000));
        assertThat(bestBidOffer.getSize()).isEqualTo(100);
    }

    private void updateOrderBookWithProcessingEvent() {
        final IEXPriceLevelUpdateMessage update = new IEXPriceLevelUpdateMessageDataBuilder()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL)
                .withIEXEventFlag(IEXEventFlag.ORDER_BOOK_IS_PROCESSING_EVENT)
                .withSymbol(SYMBOL)
                .withIEXPrice(new IEXPrice(251000))
                .withSize(0)
                .build();
        orderBook.priceLevelUpdate(update);
    }

    private void assertSecondStep() {
        final PriceLevel bestAskOffer = orderBook.getBestAskOffer();
        assertThat(bestAskOffer.getPrice()).isEqualTo(new IEXPrice(251000));
        assertThat(bestAskOffer.getSize()).isEqualTo(100);

        final PriceLevel bestBidOffer = orderBook.getBestBidOffer();
        assertThat(bestBidOffer.getPrice()).isEqualTo(new IEXPrice(250000));
        assertThat(bestBidOffer.getSize()).isEqualTo(100);
    }

    private void updateOrderBookWithProcessingComplete() {
        final IEXPriceLevelUpdateMessage update = new IEXPriceLevelUpdateMessageDataBuilder()
                .withIEXMessageType(IEXMessageType.PRICE_LEVEL_UPDATE_SELL)
                .withIEXEventFlag(IEXEventFlag.EVENT_PROCESSING_COMPLETE)
                .withSymbol(SYMBOL)
                .withIEXPrice(new IEXPrice(252000))
                .withSize(0)
                .build();
        orderBook.priceLevelUpdate(update);
    }

    private void assertThirdStep() {
        final PriceLevel bestAskOffer = orderBook.getBestAskOffer();
        assertThat(bestAskOffer.getPrice()).isEqualTo(new IEXPrice(253000));
        assertThat(bestAskOffer.getSize()).isEqualTo(100);

        final PriceLevel bestBidOffer = orderBook.getBestBidOffer();
        assertThat(bestBidOffer.getPrice()).isEqualTo(new IEXPrice(250000));
        assertThat(bestBidOffer.getSize()).isEqualTo(100);
    }

    private void printOrderBook() {
        System.out.println(orderBook.toString());
    }

}
