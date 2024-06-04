import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.selsup.dev.Description;
import ru.selsup.dev.Document;
import ru.selsup.dev.JsonSerialization;
import ru.selsup.dev.Product;

public class JsonSerializationTest {
    private static String body = " {\"description\":" +    
    "{ \"participantInn\": \"string\" }, \"doc_id\": \"string\", \"doc_status\": \"string\"," +
    "\"doc_type\": \"LP_INTRODUCE_GOODS\", \"importRequest\": true," +
    "\"owner_inn\": \"string\", \"participant_inn\": \"string\", \"producer_inn\":" +
    "\"string\", \"production_date\": \"2020-01-23\", \"production_type\": \"string\"," +
    "\"products\": [ { \"certificate_document\": \"string\"," +
    "\"certificate_document_date\": \"2020-01-23\"," +
    "\"certificate_document_number\": \"string\", \"owner_inn\": \"string\"," +
    "\"producer_inn\": \"string\", \"production_date\": \"2020-01-23\"," +
    "\"tnved_code\": \"string\", \"uit_code\": \"string\", \"uitu_code\": \"string\" } ]," +
    "\"reg_date\": \"2020-01-23\", \"reg_number\": \"string\"}";


    @Test
    @DisplayName("Получение объекта из JSON")
    public void deSerializationTest() {
        JsonSerialization<Document> jsonSerialization = new JsonSerialization<>();
        Document document = jsonSerialization.deSerialization(body, Document.class);

        Assertions.assertNotNull(document);
    }

    @Test
    @DisplayName("Получение JSON из объекта")
    public void serializationTest() {
        JsonSerialization<Document> jsonSerialization = new JsonSerialization<>();
        Product prod1 = new Product("sertificate1", "2024-01-01", "111", "1234567890", "212121", "2024-01-01", "32", "12", "22");
        Product prod2 = new Product("sertificate2", "2024-01-02", "222", "1234567890", "121212", "2024-01-02", "23", "33", "44");
        Document document = new Document(new Description("12121212"), 
                                        "1", 
                                        "good",
                                        "LP_INTRODUCE_GOODS", 
                                        true, 
                                        "123456", 
                                        "222", 
                                        "333", 
                                        "2024-01-01", 
                                        "54321", 
                                        List.of(prod1,prod2), 
                                        "2024-01-01",
                                        "1111");

        String json = jsonSerialization.serialization(document);
        
        Assertions.assertEquals(json.contains("{"), true);

    }

}
