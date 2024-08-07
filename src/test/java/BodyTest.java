import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.selsup.dev.Body;
import ru.selsup.dev.Description;
import ru.selsup.dev.Document;
import ru.selsup.dev.JsonSerialization;
import ru.selsup.dev.Product;

public class BodyTest {

    @Test
    @DisplayName("Получение JSON из объекта класса Body")
    public void BodyJsonTest() {
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

        String signature = "FFFFFFFFFFFFFFFFF";

        Body body = new Body();
        body.setProduct_document(document);
        body.setSignature(signature);

        String json = new JsonSerialization<Body>().serialization(body);

        Assertions.assertEquals(json.contains("{"), true);

    }

}
