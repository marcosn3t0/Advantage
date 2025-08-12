package utils;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class ApiClient {
    private final OkHttpClient client;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public ApiClient() {
        this.client = new OkHttpClient();
    }

    public Response sendRequest(
            String method,
            String url,
            String jsonBody,
            Map<String, String> headers
    ) throws IOException {

        RequestBody body = null;

        if (jsonBody != null && !method.equalsIgnoreCase("GET") && !method.equalsIgnoreCase("DELETE")) {
            body = RequestBody.create(jsonBody, JSON);
        }

        Request.Builder builder = new Request.Builder().url(url);

        if (headers != null) {
            headers.forEach(builder::addHeader);
        }

        switch (method.toUpperCase()) {
            case "POST":
                builder.post(body);
                break;
            case "PUT":
                builder.put(body);
                break;
            case "DELETE":
                builder.delete(body != null ? body : RequestBody.create(new byte[0]));
                break;
            case "GET":
            default:
                builder.get();
        }

        Request request = builder.build();
        return client.newCall(request).execute();
    }
}
