package tableRestart;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Main {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String username;
		String password;
		char choice = '0';
		int[] tables = { 1, 2, 10, 51, 100, 120, 130, 140, 150, 170, 1000, 1001, 1010, 1200, 2400, 2410, 5001, 7000,
				7100, 30100, 201000, 228000, 507000, 517000, 602000, 602100, 606000, 611000, 1000023 };
		System.out.println("Username:");
		username = sc.nextLine();
		System.out.println("Password:");
		password = sc.nextLine();
		System.out.println("====================================");
		System.out.println(" Please, choose what to do");
		System.out.println("====================================");
		System.out.println("- Press 1 to close and open tables");
		System.out.println("- Press 2 to close tables");
		System.out.println("- Press 3 to open tables");
		System.out.println("====================================");
		while (choice < 49 || choice > 51) {
			choice = sc.next().charAt(0);
		}
		WebDriverManager.chromedriver().setup();

		WebDriver webDriver = new ChromeDriver();
		webDriver.get("https://boint.tableslive.com/office.php?page=login");

		webDriver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(username);
		webDriver.findElement(By.xpath("//*[@id=\"login_tab\"]/tbody/tr[4]/td[2]/input")).sendKeys(password);
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webDriver.findElement(By.xpath("//*[@id=\"login_tab\"]/tbody/tr[6]/td/input")).click();

		if (webDriver.getCurrentUrl().equals("https://boint.tableslive.com/office.php?page=login")) {
			System.out.println("Invalid user or password. Try again.");
			webDriver.quit();
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(1);
		}

		webDriver.get(
				"https://boint.tableslive.com/?action=settings&sub_act=table_mgmt&TableID=&GameTypeGroupIN=0&GameType=0&Limit=500&AllowJackpot=");
		if (choice == '1' || choice == '2') {
			webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]")).click();
			webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]")).click();
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Close tables
			while (webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]/a")).getText().equalsIgnoreCase("Reset")) {
				webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]/a")).click();
				System.out.println("=======================");
				System.out.println(
						"Table " + webDriver.findElement(By.xpath("//*[@id=\"TableID\"]/a")).getText() + " closed");
				System.out.println("=======================");
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				webDriver.findElement(By.xpath("/html/body/div[13]/div[7]/div/button")).click();

			}
		}
		if (choice == '1' || choice == '3') {
			// Open tables
			for (int i : tables) {
				webDriver.findElement(By.xpath("//*[@id=\"report_form\"]/div[1]/div/table/tbody/tr/td[2]/input")).clear();
				webDriver.findElement(By.xpath("//*[@id=\"report_form\"]/div[1]/div/table/tbody/tr/td[2]/input"))
						.sendKeys(String.valueOf(i));
				webDriver.findElement(By.xpath("//*[@id=\"filter_submit\"]/div/a")).click();
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]/a")).getText()
						.equalsIgnoreCase("Initialize")) {

					webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]/a")).click();
					try {
						TimeUnit.SECONDS.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					webDriver.findElement(By.xpath("/html/body/div[13]/div[7]/div/button")).click();
					System.out.println("=======================");
					System.out.println(
							"Table " + webDriver.findElement(By.xpath("//*[@id=\"TableID\"]/a")).getText() + " opened");
					System.out.println("=======================");
					webDriver.findElement(By.xpath("//*[@id=\"report_form\"]/div[1]/div/table/tbody/tr/td[2]/input"))
							.clear();
				}
			}
		}
		System.out.println("All done");
		webDriver.quit();
	}

}
