import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.selsup.dev.ConsoleVisitorResult;
import ru.selsup.dev.Description;
import ru.selsup.dev.Document;
import ru.selsup.dev.ManagerPool;
import ru.selsup.dev.Product;
import ru.selsup.dev.Request;

public class ManagerPoolTest {

    @Test
    @DisplayName("Работа пула потоков")
    public void managerPoolTest100() {
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
        String url = "https://ismp.crpt.ru/api/v3/lk/documents/create";


        ManagerPool<String> managerPool = new ManagerPool<>(TimeUnit.SECONDS, 10);

        for(int i = 1; i < 100; i++) {
            Request request = new Request(url,document,signature);
            managerPool.addTask(request);
        }

        managerPool.giveResult(new ConsoleVisitorResult());
        managerPool.close();
    }

}
