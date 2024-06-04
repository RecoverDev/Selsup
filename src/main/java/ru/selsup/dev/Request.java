package ru.selsup.dev;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

    /**
     * класс описывает задание
     */
    public class Request implements Callable<String> {
        private String url;
        private Document document;
        private String signature;


        public Request(String url, Document document, String signature) {
            this.url = url;
            this.document = document;
            this.signature = signature;
        }
        public String getRequest()  {

            String result = "";

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(url);

                httpPost.setHeader("Content-Type", "application/json");

                JsonSerialization<Document> jsonSerialization = new JsonSerialization<>();
                String documentJson = jsonSerialization.serialization(document);

                String requestBody = String.format("{ \"product_document\": \"%s\", \"document_format\": \"MANUAL\", \"type\": \"LP_INTRODUCE_GOODS\", \"signature\": \"%s\" }", documentJson, signature);
                StringEntity entity = new StringEntity(requestBody);
                httpPost.setEntity(entity);

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    // Обработка ответа
                    int statusCode = response.getStatusLine().getStatusCode();
                    result = response.getStatusLine().getReasonPhrase() + Integer.toString(statusCode);
                }
            } catch (IOException e) {
                System.out.println("Ошибка регистрации документа - " + e.getMessage());
            }

            return result;

        }

        @Override
        public String call() throws Exception {
            return getRequest();
        }
    }
