package pl.zankowski.iextrading4j.book.api;

import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;

import java.util.Objects;

public class PriceLevel {

    private final String symbol;
    private final long timestamp;
    private final IEXPrice price;
    private final int size;

    public PriceLevel(
            final String symbol,
            final long timestamp,
            final IEXPrice price,
            final int size) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.price = price;
        this.size = size;
    }

    public String getSymbol() {
        return symbol;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public IEXPrice getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceLevel that = (PriceLevel) o;
        return timestamp == that.timestamp &&
                size == that.size &&
                Objects.equals(symbol, that.symbol) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, timestamp, price, size);
    }

    @Override
    public String toString() {
        return "PriceLevel{" +
                "symbol='" + symbol + '\'' +
                ", timestamp=" + timestamp +
                ", price=" + price +
                ", size=" + size +
                '}';
    }
}
