package tableRestart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Main {
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String date = "21/04/2020";
		String username;
		String password;

		char choice = '0';
		ArrayList<Integer> defaultTables = new ArrayList<Integer>(
				Arrays.asList(1, 2, 10, 51, 100, 120, 130, 140, 150, 170, 1000, 1001, 1010, 1200, 2400, 2410, 5001,
						7000, 7100, 30100, 201000, 228000, 507000, 517000, 602000, 602100, 606000, 611000, 1000023));
		String listStr = org.apache.commons.lang3.StringUtils.join(defaultTables, ", ");

		try {
			File file = new File("C:\\Windows\\Temp\\tables.list");
			if (file.createNewFile()) {
				FileWriter myWriter = new FileWriter("C:\\Windows\\Temp\\tables.list");
				myWriter.write(listStr);
				myWriter.close();
			}

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		

		String currentList = String.valueOf(readFile());
		int[] tables = createTables(currentList);
		System.out.println("Username:");
		username = sc.nextLine();
		System.out.println("Password:");
		password = sc.nextLine();
		while (choice < 49 || choice > 51) {
			System.out.println("============================================");
			System.out.println(" Please, choose what to do");
			System.out.println("============================================");
			System.out.println("- Press 1 to close and open tables");
			System.out.println("- Press 2 to close tables");
			System.out.println("- Press 3 to open tables");
			System.out.println("- Press 4 to view or change the table list");
			System.out.println("============================================");

			choice = sc.next().charAt(0);

			if (choice == '4') {
				getTablelist(currentList);
				System.out.println("Do you want to change it? (Y/N)");
				outerloop: while (true) {
					sc = new Scanner(System.in);
					String answer = sc.nextLine().trim().toLowerCase();
					if (answer.equals("y")) {
						while (true) {
							System.out.println("Print table number and press Enter");
							System.out.println("If this table exists in list - it will be removed else will be added");
							System.out.println("Press L to see the table list again");
							System.out.println("Press R to restore the default list from " + date);
							System.out.println("Press 0 to exit");
							String table = sc.nextLine().trim().toLowerCase();
							if (table.equalsIgnoreCase("L")) {
								getTablelist(currentList);
							}
							if (table.equalsIgnoreCase("R")) {
								try {
									File file = new File("C:\\Windows\\Temp\\tables.list");
									FileWriter myWriter = new FileWriter("C:\\Windows\\Temp\\tables.list");
									myWriter.write(listStr);
									myWriter.close();
									System.out.println("Default restored\n");
								} catch (IOException e) {
									System.out.println("An error occurred.");
									e.printStackTrace();
								}
								currentList = String.valueOf(readFile());
								tables = createTables(currentList);
							}
							if (table.equals("0")) {
								break outerloop;
							}
							if (table.equalsIgnoreCase("L") || table.equalsIgnoreCase("R")
									|| table.equalsIgnoreCase("0")) {
								continue;
							}
							try {
								int t = Integer.parseInt(table);
							} catch (Exception e) {
								System.out.println("Invalid input\n");
								continue;
							}
							currentList = String.valueOf(readFile());
							tables = createTables(currentList);
							
							for (int i = 0; i < tables.length; ++i) {
								
								if (tables[i] == Integer.parseInt(table)) {

									tables = ArrayUtils.removeElement(tables, tables[i]);
									System.out.println("Table " + table + " removed");
									
									break;
								}
								if (i==tables.length-1) {
									tables = ArrayUtils.add(tables, Integer.parseInt(table));
									System.out.println("Table " + table + " added");
									break;
								}
							}
							Arrays.sort(tables);
							currentList = Arrays.toString(tables);
							try {
								FileWriter myWriter = new FileWriter("C:\\Windows\\Temp\\tables.list");
								myWriter.write(currentList);
								myWriter.close();
							} catch (IOException e) {
								System.out.println("An error occurred.");
								e.printStackTrace();

							}

						}
					} else if (answer.equals("n")) {

						break;
					}

				}

			}
		}
		currentList = String.valueOf(readFile());
		tables = createTables(currentList);
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
				webDriver.findElement(By.xpath("//*[@id=\"report_form\"]/div[1]/div/table/tbody/tr/td[2]/input"))
						.clear();
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

	public static void getTablelist(String currentList) {
		System.out.println("Current table list:");
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get("C:\\Windows\\Temp\\tables.list"), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentList = String.valueOf(contentBuilder);
		System.out.println(currentList);

	}

	public static int[] createTables(String currentList) {
		String[] numbers = currentList.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
		int[] tables = new int[numbers.length];
		for (int i = 0; i < numbers.length; ++i) {
			try {
				tables[i] = Integer.parseInt(numbers[i]);
			} catch (NumberFormatException e) {
				System.out.println("Something went wrong");
			}
		}
		return tables;
	}
	
	public static StringBuilder readFile () {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get("C:\\Windows\\Temp\\tables.list"), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder;
		
	}
}
