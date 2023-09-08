import com.codeborne.selenide.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static com.codeborne.selenide.Selenide.*;


public class MainPageTest {
    @Before
    public void startUp() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.driverManagerEnabled = true;
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;
        Selenide.open("https://mnogosna.ru/");
    }
    @Test
    public void checkSearchRequest() {
        // Модальное окно "Ваш регион"
        $x("//a[text()='Да, все верно']").click();
        // Клик в поле поиска на главной странице
        $x("//input[@class='js-search-input']").click();
        // Ввод запроса на странице поиска
        $x("//input[@id='search-input']").shouldBe(Condition.enabled).setValue("матрас аскона 160х200");
        // Явное ожидание появления результата поиска по разделам
        sleep(1000);
        // Сохраняю категории в коллекцию
        ElementsCollection slinksPopularQuery = $$x("//*[@id='slinks-popular-query']//a");
        String actualHref = slinksPopularQuery.first().getAttribute("href");
        String actualText = slinksPopularQuery.first().attr("text").trim();
        String expectedHref = "https://mnogosna.ru/tipy-matrasov/topper/ascona/160x200/";
        String expectedText = "Топперы Аскона 160х200";
        Assert.assertEquals(expectedHref,actualHref);
        Assert.assertEquals(expectedText, actualText);
    }
    @Test
    public void checkFirstGood(){
        $x("//input[@class='js-search-input']").click();
        $x("//input[@id='search-input']").shouldBe(Condition.enabled).setValue("матрас аскона 160х200");
        sleep(1000);
        ElementsCollection popularGoodsWrap = $$x("//div[@class='pop-card']");
        String firstGoodName = popularGoodsWrap.first().toString();
        ElementsCollection images = $$x("//a[@class='pop-card__img']//picture//source[2]");
        String imgHref = images.first().getAttribute("srcset").toString();
        Assert.assertTrue(firstGoodName.contains("Матрас Аскона Фитнес Формула 160x200"));
        Assert.assertTrue(imgHref.contains("/upload/iblock/afe/zqj01rmc90p1v2qk05f8m0dnxorou6lk/thumbs/" +
                "1x/product_img_163.jpg"));
    }
}

