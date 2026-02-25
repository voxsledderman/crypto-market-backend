package org.voxsledderman.cryptoExchange.infrastructure.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.voxsledderman.cryptoExchange.domain.market.PriceProvider;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BinanceWebSocketProvider implements PriceProvider {

    private final Map<String, BigDecimal> latestPrices = new ConcurrentHashMap<>();
    private final Map<String, String> latestChanges = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BinanceWebSocketProvider(List<String> tickers) {
        initConnection(tickers);
    }

    private void initConnection(List<String> tickers) {
        try {
            StringBuilder urlBuilder = new StringBuilder("wss://stream.binance.com:9443/stream?streams=");
            for (int i = 0; i < tickers.size(); i++) {
                urlBuilder.append(tickers.get(i).toLowerCase()).append("@ticker");
                if (i < tickers.size() - 1) urlBuilder.append("/");
            }

            WebSocketClient client = new WebSocketClient(new URI(urlBuilder.toString())) {
                @Override
                public void onOpen(ServerHandshake handShakeData) {
                    System.out.println("Połączono z Binance WebSocket");
                }

                @Override
                public void onMessage(String message) {
                    try {
                        JsonNode root = objectMapper.readTree(message);
                        JsonNode data = root.get("data");

                        String symbol = data.get("s").asText().toUpperCase();
                        BigDecimal price = new BigDecimal(data.get("c").asText());
                        String change = data.get("P").asText();

                        latestPrices.put(symbol, price);
                        latestChanges.put(symbol, change);
                    } catch (Exception e) {
                        System.err.println("Błąd parsowania: " + e.getMessage());
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Połączenie przerwane! Kod: " + code + ", Powód: " + reason);
                    new Thread(() -> {
                        try {
                            System.out.println("Próba ponownego połączenia za 5 sekund...");
                            Thread.sleep(5000);
                            reconnect();

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }).start();
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };

            client.connect();

        } catch (Exception e) {
            throw new RuntimeException("Nie udało się zainicjować WebSocketa", e);
        }
    }

    public Map<String, BigDecimal> getPrices(List<String> tickers) {
        if (tickers == null || tickers.isEmpty()) {
            return new HashMap<>();
        }

        return tickers.stream()
                .map(String::toUpperCase)
                .filter(latestPrices::containsKey)
                .collect(Collectors.toMap(
                        ticker -> ticker,
                        latestPrices::get
                ));
    }

    public Map<String, PriceInfo> getFullMarketData(List<String> tickers) {
        return tickers.stream()
                .map(String::toUpperCase)
                .filter(latestPrices::containsKey)
                .collect(Collectors.toMap(
                        ticker -> ticker,
                        ticker -> new PriceInfo(latestPrices.get(ticker), latestChanges.get(ticker))
                ));
    }

    @Override
    public BigDecimal getCurrentPrice(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cant be null or blank");
        }

        BigDecimal price = latestPrices.get(ticker.toUpperCase());

        if (price == null) {
            throw new IllegalArgumentException("Unknown ticker: [%s]".formatted(ticker));
        }
        return price;
    }

    public record PriceInfo(BigDecimal price, String changePercent) {}
}