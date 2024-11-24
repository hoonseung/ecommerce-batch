package com.ecommerce.api.util;

import com.ecommerce.api.domain.order.dto.OrderRequest;
import com.ecommerce.api.domain.order.dto.OrderResponse;
import com.ecommerce.api.domain.order.dto.PaymentRequest;
import com.ecommerce.api.domain.orderProduct.dto.OrderProductRequest;
import com.ecommerce.api.domain.payment.PaymentMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class OrderRequester {

    private static final String BASE_URL = "http://localhost:8080/v1";
    private static final String PRODUCTS_URL = BASE_URL + "/products";
    private static final String ORDERS_URL = BASE_URL + "/orders";
    private static final Random RANDOM = new Random();
    private static final PaymentMethod[] PAYMENT_METHODS = PaymentMethod.values();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }


    public static void main(String[] args) {
        int maxWorker = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(maxWorker);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        try {
            int page = 0;
            int size = 1000;
            boolean hasNextPage = true;

            while (hasNextPage && page < 10000) {
                String body = fetchProduct(page, size);
                JsonNode rootNode = OBJECT_MAPPER.readTree(body);
                JsonNode contentNode = rootNode.get("result").get("content");
                for (JsonNode productNode : contentNode) {
                    String productId = productNode.get("productId").asText();
                    int stockQuantity = productNode.get("stockQuantity").asInt();
                    CompletableFuture<Void> future = CompletableFuture.runAsync(
                            () -> processProduct(productId, stockQuantity), executorService);
                    futures.add(future);
                }

                hasNextPage = !rootNode.get("result").get("last").asBoolean();
                page++;
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            log.error("ERROR!! message: {}", e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }

    private static void processProduct(String productId, int stockQuantity) {
        int quantity = Math.max((int) Math.floor(stockQuantity / 10.0), 1);
        int randomNum = RANDOM.nextInt(16) + 1;

        OrderResponse orderResponse = createOrder(productId, quantity);
        if (orderResponse != null) {
            if (randomNum % 4 < 2) {
                completePayment(orderResponse.orderId(), randomNum % 2 == 0);
            }
            if (randomNum % 8 < 4) {
                completeOrder(orderResponse.orderId());
            }
            if (randomNum % 16 < 8) {
                cancelOrder(orderResponse.orderId());
            }
        }
    }

    private static OrderResponse createOrder(String productId, int quantity) {
        OrderRequest orderRequest = new OrderRequest(
                RANDOM.nextLong(1000) + 1,
                PAYMENT_METHODS[RANDOM.nextInt(PAYMENT_METHODS.length)],
                List.of(new OrderProductRequest(productId, quantity))
        );
        try {
            String requestBody = OBJECT_MAPPER.writeValueAsString(orderRequest);
            HttpResponse<String> httpResponse = sendPostRequest(ORDERS_URL, requestBody);

            if (httpResponse.statusCode() == 200) {
                log.info("order success");
                String result = OBJECT_MAPPER.readTree(httpResponse.body()).get("result").toString();
                return OBJECT_MAPPER.readValue(result, OrderResponse.class);
            } else {
                log.info("order failed, api response failed");
                return null;
            }

        } catch (Exception e) {
            log.error("exception occurred while order creating!! message: {}", e.getMessage());
            return null;
        }
    }

    private static void completePayment(Long orderId, boolean isSuccess) {
        PaymentRequest paymentRequest = new PaymentRequest(isSuccess);
        try {
            String requestBody = OBJECT_MAPPER.writeValueAsString(paymentRequest);
            HttpResponse<String> httpResponse = sendPostRequest(ORDERS_URL + "/" + orderId + "/payment", requestBody);

            if (httpResponse.statusCode() == 200) {
                if (isSuccess) {
                    log.info("payment success");
                } else {
                    log.info("payment failed");
                }
            } else {
                log.info("payment failed, api response failed");
            }

        } catch (Exception e) {
            log.error("exception occurred while payment!! message: {}", e.getMessage());
        }
    }

    private static void completeOrder(Long orderId) {
        try {
            HttpResponse<String> httpResponse = sendPostRequest(ORDERS_URL + "/" + orderId + "/complete", "");
            if (httpResponse.statusCode() == 200) {
                log.info("order complete success");
            } else {
                log.info("order complete failed, api response failed");
            }

        } catch (Exception e) {
            log.error("exception occurred while order complete!! message: {}", e.getMessage());
        }
    }

    private static void cancelOrder(Long orderId) {
        try {
            HttpResponse<String> httpResponse = sendPostRequest(ORDERS_URL + "/" + orderId + "/cancel", "");
            if (httpResponse.statusCode() == 200) {
                log.info("order cancel success");
            } else {
                log.info("order cancel failed, api response failed");
            }

        } catch (Exception e) {
            log.error("exception occurred while order cancel!! message: {}", e.getMessage());
        }
    }


    private static String fetchProduct(int page, int size) throws IOException, InterruptedException {
        String url = String.format("%s?page=%d&size=%d&sort=productId,asc", PRODUCTS_URL, page, size);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static HttpResponse<String> sendPostRequest(String url, String requestBody) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        return HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
