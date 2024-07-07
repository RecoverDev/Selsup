package ru.selsup;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {


        String signature = "FFFFFFFFFFFFFFFFF";
        String url = "https://ismp.crpt.ru/api/v3/lk/documents/create";

        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 100, url);


        for (int i = 0; i < 200; i++) {
            crptApi.startTask(createDocument(crptApi), signature);
        }

        crptApi.getResult();

    }

    private static CrptApi.Document createDocument(CrptApi crptApi) {

        CrptApi.Document document = crptApi.getDocument();

        document.setDescription(createDescription(crptApi));
        document.setDoc_id("1");
        document.setDoc_status("good");
        document.setDoc_type(CrptApi.type_enum.LP_INTRODUCE_GOODS);
        document.setImportRequest(true);
        document.setOwner_inn("123456");
        document.setParticipant_inn("222");
        document.setProducer_inn("333");
        document.setProduction_date("2024-01-01");
        document.setProduction_type("54321");
        document.setReg_date("2024-01-01");
        document.setReg_number("111");

        List<CrptApi.Product> products =  new ArrayList<CrptApi.Product>();
        for (int i = 0; i < 3; i++) {
            products.add(createProduct(crptApi));
        }
        document.setProducts(products);

        return document;
    }
    
    private static CrptApi.Product createProduct(CrptApi crptApi) {

        CrptApi.Product product = crptApi.getProduct();
        product.setCertificate_document("sertificatte1");
        product.setCertificate_document_date("2024-01-01");
        product.setCertificate_document_number("111");
        product.setOwner_inn("1234567890");
        product.setProducer_inn("212121");
        product.setProduction_date("2024-01-01");
        product.setTnved_code("32");
        product.setUit_code("12");
        product.setUitu_code("22");


        return product;
    }

    private static CrptApi.Description createDescription(CrptApi crptApi) {
        CrptApi.Description description = crptApi.getDescription();

        description.setParticipantInn("12121212");

        return description;
    }
}