package Testes;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.time.Duration;
import org.openqa.selenium.interactions.Actions;

public class HomePageTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        // Configura o EdgeDriver automaticamente usando o WebDriverManager
        WebDriverManager.edgedriver().setup();
        
        driver = new EdgeDriver(); // Inicia o navegador Edge
        driver.manage().window().maximize(); // Maximiza a janela do navegador
    }

    @Test
    public void testHomePageLoad() {
        // Navega até a página inicial do site
        driver.get("https://magento.softwaretestingboard.com/");
        
        // Verifica se a página inicial carregou corretamente, buscando um elemento característico (logo)
        WebElement logo = driver.findElement(By.cssSelector("a.logo"));
        assertTrue(logo.isDisplayed()); // Verifica se o logo está visível na página
    }

    @Test
    public void testSearchShirt() {
        // Navega até a página inicial
        driver.get("https://magento.softwaretestingboard.com/");

        // Busca pelo termo "shirt" no campo de pesquisa
        WebElement searchBox = driver.findElement(By.id("search"));
        searchBox.sendKeys("shirt");
        searchBox.submit();

        // Verifica se a página de resultados carregou corretamente
        WebElement resultsHeader = driver.findElement(By.cssSelector("h1 span"));
        assertTrue(resultsHeader.getText().toLowerCase().contains("shirt"));
    }

    @Test
    public void testAddProductToCart() {
        // Navega até a página inicial
        driver.get("https://magento.softwaretestingboard.com/");

        // Busca pelo termo "shirt" no campo de pesquisa
        WebElement searchBox = driver.findElement(By.id("search"));
        searchBox.sendKeys("shirt");
        searchBox.submit();

        // Aguarda os resultados serem carregados
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-item")));

        // Seleciona o primeiro item
        WebElement productItem = driver.findElement(By.cssSelector("#maincontent > div.columns > div.column.main > div.search.results > div.products.wrapper.grid.products-grid > ol > li:nth-child(1) > div"));
        Actions actions = new Actions(driver);
        actions.moveToElement(productItem).perform();

        // Clica no produto
        productItem.click();

        // Aguarda a nova página carregar completamente
        WebDriverWait wait41 = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait41.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".swatch-attribute")));

        // Seleciona o tamanho XS
        WebElement sizeOption = wait41.until(ExpectedConditions.elementToBeClickable(By.id("option-label-size-143-item-166")));
        sizeOption.click();

        // Seleciona a cor Azul
        WebElement colorOption = wait41.until(ExpectedConditions.elementToBeClickable(By.id("option-label-color-93-item-50")));
        colorOption.click();

        // Clica no botão "Add to Cart" após selecionar tamanho e cor
        WebElement addToCartButtonFinal = wait41.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='product-addtocart-button']")));
        addToCartButtonFinal.click();

        // Verifica se o item foi adicionado ao carrinho (mensagem de sucesso)
        WebElement cartConfirmation = wait41.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#maincontent > div.page.messages > div:nth-child(2) > div > div > div")));
        assertTrue(cartConfirmation.isDisplayed());
    }
    
    @After
    public void tearDown() {
        // Fecha o navegador após os testes
        if (driver != null) {
            driver.quit();
        }
    }
}
