package org.voxsledderman.cryptoExchange.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.voxsledderman.cryptoExchange.domain.entities.enums.PositionState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TradeOrderTest {

    private TradeOrder tradeOrder;
    private final UUID WALLET_UUID = UUID.randomUUID();
    private final LocalDateTime OPEN_TIME = LocalDateTime.of(2024, 1, 15, 10, 0);

    @BeforeEach
    void setUp() {
        tradeOrder = TradeOrder.builder()
                .id(1L)
                .ticker("BTC")
                .walletUuid(WALLET_UUID)
                .amount(new BigDecimal("2.0"))
                .openPrice(new BigDecimal("50000.00"))
                .openTime(OPEN_TIME)
                .positionState(PositionState.OPENED)
                .build();
    }

    @Nested
    @DisplayName("Builder & constructor tests")
    class BuilderTests {

        @Test
        @DisplayName("Builder powinien poprawnie zbudować obiekt")
        void shouldBuildTradeOrderCorrectly() {
            assertEquals(1L, tradeOrder.getId());
            assertEquals("BTC", tradeOrder.getTicker());
            assertEquals(WALLET_UUID, tradeOrder.getWalletUuid());
            assertEquals(new BigDecimal("2.0"), tradeOrder.getAmount());
            assertEquals(new BigDecimal("50000.00"), tradeOrder.getOpenPrice());
            assertEquals(OPEN_TIME, tradeOrder.getOpenTime());
            assertEquals(PositionState.OPENED, tradeOrder.getPositionState());
        }

        @Test
        @DisplayName("NoArgsConstructor powinien stworzyć pusty obiekt")
        void shouldCreateEmptyObjectWithNoArgsConstructor() {
            TradeOrder emptyOrder = new TradeOrder();
            assertNull(emptyOrder.getId());
            assertNull(emptyOrder.getTicker());
            assertNull(emptyOrder.getAmount());
        }

        @Test
        @DisplayName("AllArgsConstructor powinien poprawnie ustawić wszystkie pola")
        void shouldSetAllFieldsWithAllArgsConstructor() {
            TradeOrder order = new TradeOrder(2L, "ETH", WALLET_UUID,
                    new BigDecimal("10.0"), new BigDecimal("3000.00"),
                    OPEN_TIME, PositionState.CLOSED, new BigDecimal("4000.00"));

            assertEquals(2L, order.getId());
            assertEquals("ETH", order.getTicker());
            assertEquals(new BigDecimal("10.0"), order.getAmount());
        }
    }

    @Nested
    @DisplayName("getTradeValueNow tests")
    class GetTradeValueNowTests {

        @Test
        @DisplayName("Powinien zwrócić poprawną wartość: amount * currentPrice")
        void shouldReturnCorrectTradeValueNow() {
            // 2.0 * 55000.00 = 110000.000
            BigDecimal result = tradeOrder.getTradeValueNow(new BigDecimal("55000.00"));
            assertEquals(new BigDecimal("110000.000"), result);
        }

        @Test
        @DisplayName("Powinien zwrócić 0 gdy currentPrice wynosi 0")
        void shouldReturnZeroWhenCurrentPriceIsZero() {
            BigDecimal result = tradeOrder.getTradeValueNow(BigDecimal.ZERO);
            assertEquals(new BigDecimal("0.0"), result);
        }

        @Test
        @DisplayName("Powinien obsłużyć ułamkowe ceny")
        void shouldHandleFractionalPrice() {
            // 2.0 * 0.005 = 0.010
            BigDecimal result = tradeOrder.getTradeValueNow(new BigDecimal("0.005"));
            assertEquals(new BigDecimal("0.0100"), result);
        }

        @Test
        @DisplayName("Powinien rzucić NullPointerException gdy currentPrice jest null")
        void shouldThrowExceptionWhenCurrentPriceIsNull() {
            assertThrows(NullPointerException.class,
                    () -> tradeOrder.getTradeValueNow(null));
        }
    }

    @Nested
    @DisplayName("getProfit tests")
    class GetProfitTests {

        @Test
        @DisplayName("Powinien zwrócić dodatni profit gdy cena wzrosła")
        void shouldReturnPositiveProfitWhenPriceIncreased() {
            // (2.0 * 60000.00) - (2.0 * 50000.00) = 120000.00 - 100000.00 = 20000.00
            BigDecimal profit = tradeOrder.getProfit(new BigDecimal("60000.00"));
            assertEquals(new BigDecimal("20000.000"), profit);
        }

        @Test
        @DisplayName("Powinien zwrócić ujemny profit gdy cena spadła")
        void shouldReturnNegativeProfitWhenPriceDecreased() {
            // (2.0 * 40000.00) - (2.0 * 50000.00) = 80000.00 - 100000.00 = -20000.00
            BigDecimal profit = tradeOrder.getProfit(new BigDecimal("40000.00"));
            assertEquals(new BigDecimal("-20000.000"), profit);
        }

        @Test
        @DisplayName("Powinien zwrócić 0 gdy cena nie zmieniła się")
        void shouldReturnZeroProfitWhenPriceUnchanged() {
            // (2.0 * 50000.00) - (2.0 * 50000.00) = 0.00
            BigDecimal profit = tradeOrder.getProfit(new BigDecimal("50000.00"));
            assertEquals(new BigDecimal("0.000"), profit);
        }

        @Test
        @DisplayName("Powinien poprawnie obliczyć profit dla ułamkowych wartości")
        void shouldCalculateProfitForFractionalValues() {
            TradeOrder fractionalOrder = TradeOrder.builder()
                    .amount(new BigDecimal("0.5"))
                    .openPrice(new BigDecimal("100.000"))
                    .build();

            // (0.5 * 120.00) - (0.5 * 100.00) = 60.00 - 50.00 = 10.00
            BigDecimal profit = fractionalOrder.getProfit(new BigDecimal("120.00"));
            assertEquals(new BigDecimal("10.0000"), profit);
        }

        @Test
        @DisplayName("Powinien rzucić NullPointerException gdy amount jest null")
        void shouldThrowExceptionWhenAmountIsNull() {
            TradeOrder orderWithNullAmount = TradeOrder.builder()
                    .openPrice(new BigDecimal("50000.00"))
                    .build();

            assertThrows(NullPointerException.class,
                    () -> orderWithNullAmount.getProfit(new BigDecimal("55000.00")));
        }
    }

    @Nested
    @DisplayName("Lombok @Data tests")
    class LombokDataTests {

        @Test
        @DisplayName("Setter powinien poprawnie zmienić wartość pola")
        void shouldSetFieldCorrectly() {
            tradeOrder.setTicker("ETH");
            assertEquals("ETH", tradeOrder.getTicker());
        }

        @Test
        @DisplayName("Dwa obiekty z tymi samymi polami powinny być równe")
        void shouldBeEqualWhenFieldsAreTheSame() {
            TradeOrder order1 = TradeOrder.builder()
                    .id(1L).ticker("BTC").amount(new BigDecimal("1.0"))
                    .openPrice(new BigDecimal("50000.00")).build();

            TradeOrder order2 = TradeOrder.builder()
                    .id(1L).ticker("BTC").amount(new BigDecimal("1.0"))
                    .openPrice(new BigDecimal("50000.00")).build();

            assertEquals(order1, order2);
            assertEquals(order1.hashCode(), order2.hashCode());
        }

        @Test
        @DisplayName("toString powinien zawierać ticker")
        void toStringShouldContainTicker() {
            assertTrue(tradeOrder.toString().contains("BTC"));
        }
    }
}
