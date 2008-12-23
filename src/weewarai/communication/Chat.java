package weewarai.communication;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class Chat {

	private static final int GAME_ID_CHAT_ADJUST = -109;
	private static final String LOGIN_URL = "http://weewar.com/login";

	public static String getMessages(int gameId, String username,
			String userPassword) throws IOException {
		String messages = null;

		GetMethod getLogin = null;
		PostMethod postLogin = null;
		GetMethod getMessages = null;

		try {
			// get login page
			getLogin = new GetMethod(LOGIN_URL);
			HttpClient httpclientGetLogin = new HttpClient();

			int responseCodeLogin = httpclientGetLogin.executeMethod(getLogin);
			if (responseCodeLogin == 200) {
				String bodyLogin = getLogin.getResponseBodyAsString();
				int indexStartView = bodyLogin
						.indexOf("javax.faces.ViewState\" value=\"") + 30;
				int indexEndView = bodyLogin.indexOf("\"", indexStartView);
				String view = bodyLogin.substring(indexStartView, indexEndView);

				postLogin = new PostMethod(LOGIN_URL);
				postLogin.setParameter("content:loginForm:login", username);
				postLogin.setParameter("content:loginForm:password",
						userPassword);
				postLogin.setParameter("content:loginForm:loginButton", "");
				postLogin.setParameter("content:loginForm_SUBMIT", "1");
				postLogin.setParameter("javax.faces.ViewState", view);

				HttpClient httpclientLogin = new HttpClient();
				int responseCodePostLogin = httpclientLogin
						.executeMethod(postLogin);
				if (responseCodePostLogin == 302) {
					Header cookiesLogin = postLogin
							.getResponseHeader("Set-Cookie");
					String cookieStringLogin = cookiesLogin.getValue();

					// now try to get chat
					String urlChat = "http://weewar.com/ajax/click.jsp"
							+ "?method=eventRequest&eventCollection="
							+ (gameId + GAME_ID_CHAT_ADJUST) + "&tbUid="
							+ System.currentTimeMillis();
					getMessages = new GetMethod(urlChat);
					getMessages.setRequestHeader(cookieStringLogin,
							cookieStringLogin);
					HttpClient httpclientGetChat = new HttpClient();
					int responseCode = httpclientGetChat
							.executeMethod(getMessages);
					if (responseCode == 200) {
						messages = getMessages.getResponseBodyAsString();
					}
				}
			}
		} finally {
			if (getLogin != null) {
				getLogin.releaseConnection();
			}
			if (postLogin != null) {
				postLogin.releaseConnection();
			}
			if (getMessages != null) {
				getMessages.releaseConnection();
			}
		}
		return messages;
	}

	public static void sendMessage(int gameId, String message, String username,
			String userPassword) throws IOException {
		GetMethod getLogin = null;
		PostMethod postLogin = null;
		GetMethod getSendMessage = null;

		try {
			// get login page
			getLogin = new GetMethod(LOGIN_URL);
			HttpClient httpclientGetChat = new HttpClient();

			int responseCode = httpclientGetChat.executeMethod(getLogin);
			if (responseCode == 200) {
				String messages = getLogin.getResponseBodyAsString();
				int indexStart = messages.indexOf("action=\"") + 8;
				int indexEnd = messages.indexOf("\"", indexStart);
				String path = messages.substring(indexStart, indexEnd);
				System.out.println("path = " + path);

				int indexStartSession = path.indexOf(";") + 1;
				String Session = path.substring(indexStartSession);

				int indexStartView = messages
						.indexOf("javax.faces.ViewState\" value=\"") + 30;
				int indexEndView = messages.indexOf("\"", indexStartView);
				String view = messages.substring(indexStartView, indexEndView);

				// login
				postLogin = new PostMethod("http://www.weewar.com/" + path);
				postLogin.setParameter("content:loginForm:login", username);
				postLogin.setParameter("content:loginForm:password",
						userPassword);
				postLogin.setParameter("content:loginForm:loginButton", "");
				postLogin.setParameter("content:loginForm_SUBMIT", "1");
				postLogin.setParameter("javax.faces.ViewState", view);

				HttpClient httpclientLogin = new HttpClient();
				int responseCodeLogin = httpclientLogin
						.executeMethod(postLogin);
				if (responseCodeLogin == 302) {
					Header cookiesLogin = postLogin
							.getResponseHeader("Set-Cookie");
					String cookieStringLogin = cookiesLogin.getValue();
					int indexSemiColon = cookieStringLogin.indexOf(";");
					String WeewarChocolateCookie = cookieStringLogin.substring(
							0, indexSemiColon);

					message = URLEncoder.encode(message, "UTF-8");
					String urlSendMessage = "http://weewar.com/ajax/click.jsp"
							+ "?method=eventSend&eventCollection="
							+ (gameId + GAME_ID_CHAT_ADJUST) + "&tbUid="
							+ System.currentTimeMillis() + "&message="
							+ message;

					// send message
					getSendMessage = new GetMethod(urlSendMessage);
					getSendMessage.setRequestHeader("Cookie",
							WeewarChocolateCookie + "; " + Session);

					HttpClient httpclientgetSendMessage = new HttpClient();
					httpclientgetSendMessage.executeMethod(getSendMessage);
				}
			}
		} finally {
			if (getLogin != null) {
				getLogin.releaseConnection();
			}
			if (postLogin != null) {
				postLogin.releaseConnection();
			}
			if (getSendMessage != null) {
				getSendMessage.releaseConnection();
			}
		}
	}
}
