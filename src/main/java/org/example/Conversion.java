package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Conversion {
    @SerializedName("base_code")
    private String baseCode;
    @SerializedName("target_code")
    private String targetCode;
    private double amount;
    private double result;

    public Conversion(String baseCode, String targetCode, double amount) {
        this.baseCode = baseCode.toUpperCase();
        this.targetCode = targetCode.toUpperCase();
        this.amount = amount;
    }
    public double convert() throws IOException, InterruptedException {
        String apiKey = "fe777c9723789d6a6298b74d";
        String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCode;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");
            double conversionRate = conversionRates.get(targetCode).getAsDouble();
            result = amount * conversionRate;
            return result;
        } else {
            throw new IOException("Error al realizar la solicitud sel Servidor: " + response.statusCode());
        }
    }
}
