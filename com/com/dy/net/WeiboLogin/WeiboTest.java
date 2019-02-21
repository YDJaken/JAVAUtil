package com.dy.net.WeiboLogin;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class WeiboTest {

	static final String loginUrl = "https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F";

	public static void main(String[] args) {
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_60)) {
			webClient.addRequestHeader("User-Agent",
					"Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");
			HtmlPage page = webClient.getPage(loginUrl);
			Thread.sleep(1000);
			DomElement loginName = page.getElementById("loginName");
			if (loginName.getTagName().equals("input")) {
				HtmlTextInput loginNameInput = (HtmlTextInput) loginName;
				loginNameInput.type("123");
			}
			DomElement loginPassword = page.getElementById("loginPassword");
			if (loginPassword.getTagName().equals("input")) {
				HtmlPasswordInput loginPassInput = (HtmlPasswordInput) loginPassword;
				loginPassInput.type("123");
			}
			DomElement loginButton = page.getElementById("loginAction");
			page = (HtmlPage) loginButton.rightClick();
			Thread.sleep(1000);
			System.out.println(page.asText());

		} catch (FailingHttpStatusCodeException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
