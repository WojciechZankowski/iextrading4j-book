import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;

/**
 * @author Wojciech Zankowski
 */
public class PriceLevel {

    private String symbol;
    private long timestamp;
    private IEXPrice price;
    private int size;

    public PriceLevel(String symbol, long timestamp, IEXPrice price, int size) {
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

        if (timestamp != that.timestamp) return false;
        if (size != that.size) return false;
        if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) return false;
        return price != null ? price.equals(that.price) : that.price == null;

    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + size;
        return result;
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
