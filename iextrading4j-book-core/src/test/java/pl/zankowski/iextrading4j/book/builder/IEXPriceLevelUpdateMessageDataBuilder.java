package pl.zankowski.iextrading4j.book.builder;

import pl.zankowski.iextrading4j.hist.api.IEXMessageType;
import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;
import pl.zankowski.iextrading4j.hist.deep.trading.field.IEXEventFlag;
import pl.zankowski.iextrading4j.hist.deep.trading.message.IEXPriceLevelUpdateMessage;

public class IEXPriceLevelUpdateMessageDataBuilder {

    private IEXMessageType iexMessageType = IEXMessageType.PRICE_LEVEL_UPDATE_BUY;
    private IEXEventFlag iexEventFlag = IEXEventFlag.EVENT_PROCESSING_COMPLETE;
    private long timestamp = 14875433212L;
    private String symbol = "AAPL";
    private int size = 100;
    private IEXPrice iexPrice = new IEXPrice(17532L);

    public static IEXPriceLevelUpdateMessage defaultIEXPriceLevelUpdateMessage() {
        return anIEXPriceLevelUpdateMessage().build();
    }

    public static IEXPriceLevelUpdateMessageDataBuilder anIEXPriceLevelUpdateMessage() {
        return new IEXPriceLevelUpdateMessageDataBuilder();
    }

    public IEXPriceLevelUpdateMessageDataBuilder withIEXMessageType(final IEXMessageType iexMessageType) {
        this.iexMessageType = iexMessageType;
        return this;
    }

    public IEXPriceLevelUpdateMessageDataBuilder withIEXEventFlag(final IEXEventFlag iexEventFlag) {
        this.iexEventFlag = iexEventFlag;
        return this;
    }

    public IEXPriceLevelUpdateMessageDataBuilder withTimestamp(final long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public IEXPriceLevelUpdateMessageDataBuilder withSize(final int size) {
        this.size = size;
        return this;
    }

    public IEXPriceLevelUpdateMessageDataBuilder withSymbol(final String symbol) {
        this.symbol = symbol;
        return this;
    }

    public IEXPriceLevelUpdateMessageDataBuilder withIEXPrice(final IEXPrice iexPrice) {
        this.iexPrice = iexPrice;
        return this;
    }

    public IEXPriceLevelUpdateMessage build() {
        return new IEXPriceLevelUpdateMessage(iexMessageType, iexEventFlag, timestamp, symbol, size, iexPrice);
    }

}
