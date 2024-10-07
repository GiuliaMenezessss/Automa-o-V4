package Testes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;

public class NewAccount {
    private WebDriver driver;

    @Before
    public void setUp() {
        // Configura o EdgeDriver 
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }

    // Busca por "shirt" e clicar no último resultado 
    @Test
    public void testSearchShirtAndClick() {
        driver.get("https://magento.softwaretestingboard.com/");
        
        WebElement searchBox = driver.findElement(By.id("search"));
        searchBox.sendKeys("shirt");
        searchBox.submit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-item")));

        List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));
        WebElement lastProduct = products.get(products.size() - 1);
        lastProduct.click();
    }

    // Cria uma conta utilizando dados aleatórios do Random User
    @Test
    public void testCreateAccount() {
        // Obtém dados aleatórios do usuário através da API do Random User sugerida
        HttpResponse<JsonNode> response = Unirest.get("https://randomuser.me/api/").asJson();
        JsonNode userData = response.getBody();
        String firstName = userData.getObject().getJSONArray("results").getJSONObject(0).getJSONObject("name").getString("first");
        String lastName = userData.getObject().getJSONArray("results").getJSONObject(0).getJSONObject("name").getString("last");
        String email = userData.getObject().getJSONArray("results").getJSONObject(0).getString("email");
        String password = "Password123!";  // Senha padrão para o teste

        driver.get("https://magento.softwaretestingboard.com/customer/account/create/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-validate")));

        driver.findElement(By.id("firstname")).sendKeys(firstName);
        driver.findElement(By.id("lastname")).sendKeys(lastName);
        driver.findElement(By.id("email_address")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("password-confirmation")).sendKeys(password);

        driver.findElement(By.cssSelector("button[title='Create an Account']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-title")));
        WebElement successMessage = driver.findElement(By.cssSelector(".page-title"));
        assertTrue(successMessage.getText().contains("My Account"));
    }

    // Adiciona um produto aleatório do catálogo de moda masculina no carrinho
    @Test
    public void testAddRandomProductToCart() {
        driver.get("https://magento.softwaretestingboard.com/men.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-item")));

        List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));
        WebElement randomProduct = products.get((int) (Math.random() * products.size()));
        randomProduct.click();

        // Seleciona as opções de tamanho e cor, caso existam
        try {
            WebElement sizeOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='swatch-attribute size']//div[contains(@class, 'swatch-option')]")));
            sizeOption.click();
        } catch (Exception e) {
            System.out.println("Nenhuma opção de tamanho encontrada.");
        }

        try {
            WebElement colorOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='swatch-attribute color']//div[contains(@class, 'swatch-option')]")));
            colorOption.click();
        } catch (Exception e) {
            System.out.println("Nenhuma opção de cor encontrada.");
        }

        driver.findElement(By.id("product-addtocart-button")).click();

        // Verifica se o produto foi adicionado ao carrinho
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
        WebElement successMessage = driver.findElement(By.cssSelector(".message-success"));
        assertTrue(successMessage.isDisplayed());
    }

    // Adiciona um comentário em um produto aleatório do catálogo de moda masculina
    @Test
    public void testAddReviewToProduct() {
        driver.get("https://magento.softwaretestingboard.com/men.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-item")));

        List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));
        WebElement randomProduct = products.get((int) (Math.random() * products.size()));
        randomProduct.click();

        WebElement reviewTab = driver.findElement(By.id("tab-label-reviews-title"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", reviewTab);
        reviewTab.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("review-form")));
        driver.findElement(By.id("nickname_field")).sendKeys("TestUser");
        driver.findElement(By.id("summary_field")).sendKeys("Great product!");
        driver.findElement(By.id("review_field")).sendKeys("The quality of the product is really good, and I'm satisfied!");

        driver.findElement(By.cssSelector("button[title='Submit Review']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
        WebElement successMessage = driver.findElement(By.cssSelector(".message-success"));
        assertTrue(successMessage.isDisplayed());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
