package ru.selsup;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


public class CrptApi {
    private ManagerPool<String> pool;
    private static String strURL = "https://ismp.crpt.ru/api/v3/lk/documents/create";

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
                    result = Integer.toString(statusCode);
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

    /**
     * класс управляет пулом потоков
     */
    public class ManagerPool<T> {
        private ThreadPoolExecutor pool;
        private List<Future<T>> results = null;
     
        public ManagerPool(TimeUnit timeUnit, int requestLimit) {
            pool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
            pool.setCorePoolSize(requestLimit);
            pool.setMaximumPoolSize(requestLimit);
            pool.setKeepAliveTime(1,timeUnit);
            pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    
        }
    
        public void addTask(Callable<T> task) {
            if(results == null) {
                results = new LinkedList<>();
            }
            results.add(pool.submit(task));
        }
    
        public void giveResult(Visitor<T> visitor) {
    
            List<T> outResults = new LinkedList<>();
            for (Future<T> f : results) {
                try {
                    outResults.add(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Ошибка получения результата задания - " + e.getMessage());
                }
            }
            visitor.out(outResults);
        }
    
        public void close() {
            pool.shutdown();
            while (!pool.isTerminated()) {
            }
        }
    }
    
    /**
     * интерфейс описывает шаблон Посетитель
     */
    public interface Visitor<E> {
        void out(List<E> values);
    }

    /**
     * класс реализует обработку результата работы пула потоков
     */
    public class ConsoleVisitorResult implements Visitor<String> {

        @Override
        public void out(List<String> values) {
            for (String s : values) {
                System.out.println(s);
            }
        }
    
    }

    /**
     * Описание структуры документа
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Description {
        private String participantInn;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Document {
        private Description description;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        private List<Product> products;
        private String reg_date;
        private String reg_number;
    
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Product {
        private String certificate_document;
        private String certificate_document_date;
        private String certificate_document_number;
        private String owner_inn;
        private String producer_inn;
        private String production_date;
        private String tnved_code;
        private String uit_code;
        private String uitu_code;
    }

    /**
     * Работа с JSON
     */
    public class JsonSerialization<T> {

        public String serialization(T object) {
            ObjectMapper mapper = new ObjectMapper();
            String result = "";

            try {
                result =  mapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                System.out.println("Ошибка серелизации объекта - " + e.getMessage());
            }

            return result;
        }

        public T deSerialization(String json,Class<T> value) {
            ObjectMapper mapper = new ObjectMapper();
            T result = null;

            try {
                result =  mapper.readValue(json, value);
            } catch (JsonParseException  e) {
                System.out.println("Ощибка преобразования JSON - " + e.getMessage());
            } catch (JsonMappingException e) {
                System.out.println("Ощибка преобразования JSON - " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Ощибка преобразования JSON - " + e.getMessage());
            }

            return result;
        }

    }

    /**
     * Многопотная регистрация документов на сайте "Честный знак"
     * @param timeUnit - единица измерения времени
     * @param requestLimit - максимальное количество потоков
     */
    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        pool = new ManagerPool<>(timeUnit, requestLimit);
    }

    public void startTask(Document document,String signamture) {
        pool.addTask(new Request(strURL, document, signamture));
    }

    public void getResult() {
        pool.giveResult(new ConsoleVisitorResult());
    }

    public void closePool() {
        pool.close();
    }

}
