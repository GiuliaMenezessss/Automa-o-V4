import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.konghq.unirest.http.HttpResponse;
import com.konghq.unirest.http.JsonNode;
import com.konghq.unirest.http.Unirest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class CreateAccountTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        // Configura o EdgeDriver automaticamente usando o WebDriverManager
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testCreateAccount() {
        // Obtém dados aleatórios do usuário através da API do Random User
        HttpResponse<JsonNode> response = Unirest.get("https://randomuser.me/api/").asJson();
        JsonNode userData = response.getBody();
        String firstName = userData.getObject().getJSONArray("results").getJSONObject(0).getJSONObject("name").getString("first");
        String lastName = userData.getObject().getJSONArray("results").getJSONObject(0).getJSONObject("name").getString("last");
        String email = userData.getObject().getJSONArray("results").getJSONObject(0).getString("email");
        String password = "Password123!";  // Defina uma senha padrão para o teste

        // Navega até a página de registro do Magento
        driver.get("https://magento.softwaretestingboard.com/customer/account/create/");

        // Aguarda o formulário de registro ser carregado
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-validate")));

        // Preenche o formulário de registro
        WebElement firstNameField = driver.findElement(By.id("firstname"));
        firstNameField.sendKeys(firstName);

        WebElement lastNameField = driver.findElement(By.id("lastname"));
        lastNameField.sendKeys(lastName);

        WebElement emailField = driver.findElement(By.id("email_address"));
        emailField.sendKeys(email);

        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);

        WebElement confirmPasswordField = driver.findElement(By.id("password-confirmation"));
        confirmPasswordField.sendKeys(password);

        // Clica no botão "Create an Account"
        WebElement createAccountButton = driver.findElement(By.cssSelector("button[title='Create an Account']"));
        createAccountButton.click();

        // Aguarda a confirmação da criação da conta
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-title")));

        // Verifica se a criação da conta foi bem-sucedida
        WebElement successMessage = driver.findElement(By.cssSelector(".page-title"));
        assertTrue(successMessage.getText().contains("My Account"));
    }

    @After
    public void tearDown() {
        // Fecha o navegador após os testes
        if (driver != null) {
            driver.quit();
        }
    }
}