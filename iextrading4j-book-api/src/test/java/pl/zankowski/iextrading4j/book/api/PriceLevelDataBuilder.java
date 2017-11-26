package pl.zankowski.iextrading4j.book.api;

import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;

public class PriceLevelDataBuilder {

    private String symbol = "IBM";
    private long timestamp = 123456789L;
    private IEXPrice price = new IEXPrice(12345);
    private int size = 100;

    public static PriceLevel defaultPriceLevel() {
        return aPriceLevel().build();
    }

    public static PriceLevelDataBuilder aPriceLevel() {
        return new PriceLevelDataBuilder();
    }

    public PriceLevelDataBuilder withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public PriceLevelDataBuilder withPrice(IEXPrice iexPrice) {
        this.price = iexPrice;
        return this;
    }

    public PriceLevelDataBuilder withTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public PriceLevelDataBuilder withSize(int size) {
        this.size = size;
        return this;
    }

    public PriceLevel build() {
        return new PriceLevel(symbol, timestamp, price, size);
    }

}
