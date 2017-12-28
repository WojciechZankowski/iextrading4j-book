# IEXTrading4j Book

[![Build Status](https://travis-ci.org/WojciechZankowski/iextrading4j-book.svg?branch=master)](https://travis-ci.org/WojciechZankowski/iextrading4j-book)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/pl.zankowski/iextrading4j-book-core/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22pl.zankowski%22%20AND%20a%3A%22iextrading4j-book-core%22)


IEX Trading open source incremental Order Book implementation

* [Quick Start](#quick-start)
* [Description](#description)  
* [IEX Trading](#iex-trading)
* [Roadmap](#roadmap)
* [License](#license)

## Quick Start

Java SE 8 is required to use IEXTrading4j Book library.

```
<dependency>
	<groupId>pl.zankowski</groupId>
	<artifactId>iextrading4j-book-core</artifactId>
	<version>1.0.0</version>
</dependency>
```

Library is up to:

* DEEP version 1.04 - August 1, 2017

## Description

IEXTrading4j Book library allows to build incremental Order Book based on PriceLevelUpdateMessages from DEEP market data stream. OrderBook implementation follows description from specification. Description down bellow:

Each symbol should be considered to have an independent Order Book. The following bullet-point statements are to be
read as "within a given symbol," unless otherwise specified. Therefore, when an Order Book is said to be "transitioning"
it is the Order Book of a particular symbol and not all Order Books across all symbols.
* Given a singular update to an Order Book (i.e., an event) (e.g., an aggressively priced order entering the Order
Book), the System may need to affect multiple price levels atomically (i.e., at once). The Order Book, along with
the IEX BBO in such symbol, should be considered to have atomically transitioned from the state immediately
prior to the Order Book transaction ("atomic update") to the state immediately subsequent to the transaction,
but not to every state in between.
* Atomic updates to a given symbol s Order Book are described in DEEP by a series of zero or more Price Level
Update Messages with the Event Flags OFF (i.e., 0x0), followed by a Price Level Update Message ( PLU ) with
the Event Flags ON (i.e., 0x1) (e.g., PLU 0x0, PLU 0x0, . . ., PLU 0x1).
* When there are updates to multiple price levels that must occur as part of a single transaction to a given
symbol's Order Book (e.g., multiple price levels' shares being exhausted via a single active order's execution),
each PLU transmitted, except for the last one, will have Event Flags OFF, and Event Flags will be ON in the final
PLU of the transaction.
* For each update to an Order Book, there will be exactly one final PLU with the Event Flags ON, but there is no
guarantee to have received preceding PLUs with an Event Flags OFF. In the case, where only a single price level
is being updated atomically, there will be a single PLU transmitted with Event Flags ON, without any preceding
PLUs transmitted with Event Flags OFF (i.e., PLUs with an Event Flags OFF are only transmitted when multiple
price levels must be affected atomically).
* A PLU with Event Flags OFF begins an atomic event (note that one or more Trade Report Message may
proceed a PLU that is part of the same atomic event). A PLU with Event Flags ON ends a transaction. If no
transaction was ongoing when the PLU with Event Flags ON arrived, then such PLU both begins and ends a
transaction.
* The Order Book should be considered to retain its BBO from prior to the transaction during the processing of
the transaction. Once the transaction is complete, then the IEX BBO should be derived. This ensures that the
transactional update has taken place atomically (i.e., that an in-transition IEX BBO, which never truly existed, is
not seen/acted upon).
Example

1. Order Book for symbol ZIEXT
* Sell 100 @ 25.30
* Sell 100 @ 25.20
* Sell 100 @ 25.10
* Buy 100 @ 25.00
* Buy 100 @ 24.90

IEX BBO: Buy 100 @ 25.00 x Sell 100 @ 25.10

2. Price Level Update on the Sell Side received: Event Flags: 0x0, Price: 25.10, Shares: 0
* Sell 100 @ 25.30
* Sell 100 @ 25.20
* Buy 100 @ 25.00
* Buy 100 @ 24.90

IEX BBO: Buy 100 @ 25.00 x Sell 100 @ 25.10 (Reason: ZIEXT Order Book is in transition)

3. Price Level Update on the Sell Side received: Event Flags: 0x1, Price: 25.20, Shares: 0
* Sell 100 @ 25.30
* Buy 100 @ 25.00
* Buy 100 @ 24.90

IEX BBO: Buy 100 @ 25.00 x Sell 100 @ 25.30 (Reason: ZIEXT Order Book transition complete)

## IEX Trading

IEX A Fair, Simple, Transparent Exchange.

IEX is a stock exchange based in the United States. Started by Brad Katsuyama, it opened for trading on October 25, 2013. The company’s offices are located at 4 World Trade Center in New York City. The matching engine is located across the Hudson River in Weehawken, New Jersey, and the initial point of presence is located in a data center in Secaucus, New Jersey. IEX's main innovation is a 38-mile coil of optical fiber placed in front of its trading engine. This 350 microsecond delay adds a round-trip delay of 0.0007 seconds and is designed to negate the certain speed advantages utilized by some high-frequency traders.

IEX was created in response to questionable trading practices that had become widely used across traditional public Wall Street exchanges as well as dark pools and other alternative trading systems. The IEX exchange aims to attract investors by promising to "play fair" by operating in a transparent and straightforward manner, while also helping to level the playing field for traders. Strategies to achieve those goals include:

* Publishing the matching rules used in the exchanges's computerized order matching engine.
* Offering a limited number of simple and familiar order types.
* Charging fixed fees on most orders (or a flat percentage rate on small orders).
* Ensuring market pricing data arrives at external points of presence simultaneously.
* Slightly delaying market pricing data to all customers (no colocation).
* Refusing to pay for order flow.

Check out their beautiful site: [IEX Trading](https://iextrading.com/)

## Roadmap

* Improve performance and implementation

## License

Code and documentation released under the Apache License, Version 2.0

Data provided for free by [IEX](https://iextrading.com/developer).

IEX Trading API Exhibit A: [Exhibit A](https://iextrading.com/api-exhibit-a)
