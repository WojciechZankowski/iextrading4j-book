package pl.zankowski.iextrading4j.book.sample;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import pl.zankowski.iextrading4j.book.IEXOrderBook;
import pl.zankowski.iextrading4j.hist.api.message.IEXMessage;
import pl.zankowski.iextrading4j.hist.api.message.IEXSegment;
import pl.zankowski.iextrading4j.hist.deep.IEXDEEPMessageBlock;
import pl.zankowski.iextrading4j.hist.deep.trading.message.IEXPriceLevelUpdateMessage;

import java.io.IOException;

public class OrderBookSample {

    private final OrderBookStore orderBookStore = new OrderBookStore();

    public static void main(String[] args) throws IOException, PcapNativeException, InterruptedException, NotOpenException {
        final OrderBookSample orderBookSample = new OrderBookSample();
        orderBookSample.readDEEPSample();
    }

    private void readDEEPSample() throws PcapNativeException, NotOpenException, InterruptedException {
        final PcapHandle handle = Pcaps.openOffline("path_to_pcap", PcapHandle.TimestampPrecision.NANO);

        handle.loop(-1, new PacketListener() {
            @Override
            public void gotPacket(Packet packet) {
                final byte[] data = packet.getPayload().getPayload().getPayload().getRawData();
                final IEXSegment block = IEXDEEPMessageBlock.createIEXSegment(data);

                for (final IEXMessage iexMessage : block.getMessages()) {
                    if (iexMessage instanceof IEXPriceLevelUpdateMessage) {
                        final IEXPriceLevelUpdateMessage iexPriceLevelUpdateMessage = (IEXPriceLevelUpdateMessage) iexMessage;
                        final IEXOrderBook iexOrderBook = orderBookStore.getOrderBook(iexPriceLevelUpdateMessage.getSymbol())
                                .orElseGet(() -> new IEXOrderBook(iexPriceLevelUpdateMessage.getSymbol()));
                        iexOrderBook.priceLevelUpdate(iexPriceLevelUpdateMessage);
                        orderBookStore.updateOrderBook(iexOrderBook);

                        if (iexOrderBook.getSymbol().equals("AAPL")) {
                            System.out.println(iexOrderBook);
                        }
                    }
                }
            }
        });


        handle.close();
    }

}
