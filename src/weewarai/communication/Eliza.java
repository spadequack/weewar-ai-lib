package weewarai.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import weewarai.model.Coordinate;
import weewarai.model.Faction;
import weewarai.model.Game;
import weewarai.model.HqGame;
import weewarai.model.WeewarMap;
import weewarai.util.Debug;

public class Eliza {

	private String username;
	private String token;
	private String apiUrlPrefix;

	public static final int HTTP_REQUEST_TIMEOUT = 4000;

	public Eliza(String apiUrlPrefix, String username, String token) {
		this.username = username;
		this.apiUrlPrefix = apiUrlPrefix;
		this.token = token;
	}

	private String httpRequest(String u, String username, String password,
			String xml) throws IOException {
		URL url = new URL(u);
		URLConnection conn = url.openConnection();
		if (username != null) {
			String userPassword = username + ":" + password;
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword
					.getBytes());
			conn.setRequestProperty("Authorization", "Basic " + encoding);
		}
		conn.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
		if (xml != null) {
			conn.addRequestProperty("Content-Type", "application/xml");
			conn.setDoOutput(true);
			conn.getOutputStream().write(xml.getBytes());
		}
		conn.connect();
		String status = conn.getHeaderField("Status");
		if (!(status != null && status.contains("404"))) {
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			StringWriter str = new StringWriter();
			PrintWriter out = new PrintWriter(str);
			String line;
			line = rd.readLine();
			while (line != null) {
				out.println(line);
				line = rd.readLine();
			}
			return str.toString();
		} else {
			return null;
		}
	}

	private String elizaRequest(String requestXML) throws IOException {
		return httpRequest(apiUrlPrefix + "eliza", username, token, requestXML);
	}

	private String gameStateRequest(int gameId) throws IOException {
		return httpRequest(apiUrlPrefix + "gamestate/" + gameId, username,
				token, "");
	}

	private String mapLayoutRequest(int mapId) throws IOException {
		return httpRequest(apiUrlPrefix + "map/" + mapId, username, token, "");
	}

	/**
	 * Returns a collection of games in the user's headquarters that are not
	 * finished
	 * 
	 * @return a collection of games in the user's headquarters that are not
	 *         finished
	 * @throws IOException
	 * @throws JDOMException
	 */
	@SuppressWarnings("unchecked")
	public Collection<HqGame> headquarterGames() throws IOException,
			JDOMException {
		String xml = httpRequest(apiUrlPrefix + "headquarters", username,
				token, null);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xml));
		Collection<HqGame> ret = new LinkedList<HqGame>();
		Collection<Element> games = doc.getRootElement().getChildren("game");
		for (Element gameEle : games) {
			if (!gameEle.getChildText("state").equals("finished")) {
				int id = Integer.parseInt(gameEle.getChildText("id"));
				Element gameEleWithMoreInfo = getGameXmlElement(id);
				HqGame g = new HqGame();
				g.parseHqXmlElement(gameEle);
				// a little redundant because some fields are overwritten with
				// the same info, but it's necessary to get the whole picture
				g.parseXmlElement(gameEleWithMoreInfo);
				ret.add(g);
			}
		}
		return ret;
	}

	/**
	 * Gets the game xml using the normal api, which lacks bulky information
	 * such as the location and quantity (health) of every unit on the map. Use
	 * getGameState() for the complete information
	 * 
	 * @param id
	 *            the game id
	 * @return the game xml using the normal api
	 * @throws IOException
	 * @throws JDOMException
	 */
	private Element getGameXmlElement(int id) throws IOException, JDOMException {
		String xml = httpRequest(apiUrlPrefix + "game/" + id, username, token,
				null);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xml));
		return doc.getRootElement();
	}

	/**
	 * Creates a new Game object with the game state information from the eliza
	 * API of the game with the given id
	 * 
	 * @param id
	 *            the game id
	 * @return a new Game with complete game state information from the eliza
	 *         API
	 * @throws IOException
	 * @throws JDOMException
	 */
	public Game getGameState(int id) throws IOException, JDOMException {
		Document doc = getGameStateXML(id);
		Game g = new Game(username);
		g.parseXmlElement(doc.getRootElement(), true);
		return g;
	}

	/**
	 * Adds faction/unit data from the eliza API to an existing Game object
	 * 
	 * @param game
	 *            the Game object
	 * @throws IOException
	 * @throws JDOMException
	 */
	public void getGameState(Game game) throws IOException, JDOMException {
		Document doc = getGameStateXML(game.getId());
		game.parseXmlElement(doc.getRootElement(), false);
	}

	private Document getGameStateXML(int id) throws IOException, JDOMException {
		String xml = gameStateRequest(id);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xml));
		return doc;
	}

	/**
	 * Updates the data of only a specific Faction
	 * 
	 * @param id
	 *            the game id
	 * @param f
	 *            the faction
	 * @throws IOException
	 * @throws JDOMException
	 */
	@SuppressWarnings("unchecked")
	public void refreshFaction(int id, Faction f) throws IOException,
			JDOMException {
		Document doc = getGameStateXML(id);
		Element gameEle = doc.getRootElement();
		if (gameEle.getChild("factions") != null) {
			List<Element> factions = gameEle.getChild("factions").getChildren(
					"faction");
			for (Element factionEle : factions) {
				if (f.getPlayerName().equals(
						factionEle.getAttributeValue("playerName"))) {
					f.parseXmlElement(factionEle);
				}
			}
		}
	}

	public WeewarMap getMap(int mapId) throws JDOMException, IOException {
		String xml = mapLayoutRequest(mapId);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xml));
		WeewarMap wmap = new WeewarMap();
		wmap.parseXmlElement(doc.getRootElement());
		return wmap;
	}

	// // Send calls to API ////

	public boolean acceptInvite(int id) throws IOException {
		String requestXML = "<weewar game=\"" + id
				+ "\"><acceptInvitation/></weewar>";
		String result = elizaRequest(requestXML);
		return result.contains("<ok/>");
	}

	public boolean declineInvite(int id) throws IOException {
		String requestXML = "<weewar game=\"" + id
				+ "\"><declineInvitation/></weewar>";
		String result = elizaRequest(requestXML);
		return result.contains("<ok/>");
	}

	public boolean surrender(int id) throws IOException {
		String requestXML = "<weewar game=\"" + id + "\"><surrender/></weewar>";
		String result = elizaRequest(requestXML);
		return result.contains("<ok/>");
	}

	public String moveAttackCapture(int id, Coordinate from, Coordinate to,
			Coordinate attack, boolean capture) throws IOException {
		String attackString = "";
		if (attack != null) {
			attackString = "<attack x='" + attack.getX() + "' y='"
					+ attack.getY() + "' />";
		}

		String moveString = "";
		if (to != null && !from.equals(to)) {
			moveString = "<move x='" + to.getX() + "' y='" + to.getY() + "' />";
		}

		String captureString = capture ? "<capture/>" : "";

		String requestXML = "<weewar game='" + id + "'><unit x='" + from.getX()
				+ "' y='" + from.getY() + "' >" + moveString + captureString
				+ attackString + "</unit></weewar>";
		String xml = elizaRequest(requestXML);
		return xml;
	}

	public String finishTurn(int id) throws IOException {
		String requestXML = "<weewar game='" + id + "'><finishTurn /></weewar>";
		String xml = elizaRequest(requestXML);
		return xml;
	}

	public String build(int id, Coordinate c, String type) throws IOException {
		String requestXML = "<weewar game='" + id + "'><build x='" + c.getX()
				+ "' y='" + c.getY() + "' type='" + type + "' /></weewar>";
		String xml = elizaRequest(requestXML);
		return xml;
	}

	public String repair(int id, Coordinate coordinate) throws IOException {
		String requestXML = "<weewar game='" + id + "'><unit x='"
				+ coordinate.getX() + "' y='" + coordinate.getY()
				+ "' ><repair/></unit></weewar>";
		String xml = elizaRequest(requestXML);
		return xml;
	}

	@SuppressWarnings("unchecked")
	public List<Coordinate> getAttackCoords(int id, Coordinate from, String type)
			throws IOException, JDOMException {
		// Debug.print("eliza: getAttackCoords");
		String requestXML = "<weewar game='" + id + "'><attackOptions x='"
				+ from.getX() + "' y='" + from.getY() + "' type='" + type
				+ "' /></weewar>";
		String xml = elizaRequest(requestXML);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xml));
		List<Coordinate> coords = new LinkedList<Coordinate>();
		for (Element coord : (Collection<Element>) doc.getRootElement()
				.getChildren("coordinate")) {
			Coordinate c = new Coordinate(Integer.parseInt(coord
					.getAttributeValue("x")), Integer.parseInt(coord
					.getAttributeValue("y")));
			coords.add(c);
		}
		return coords;
	}

	@SuppressWarnings("unchecked")
	public List<Coordinate> getMovementCoords(int id, Coordinate from,
			String type) throws IOException, JDOMException {
		// Debug.print("eliza: getMovementCoords");
		String requestXML = "<weewar game='" + id + "'><movementOptions x='"
				+ from.getX() + "' y='" + from.getY() + "' type='" + type
				+ "' /></weewar>";
		String xml = elizaRequest(requestXML);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xml));
		List<Coordinate> moveCoords = new LinkedList<Coordinate>();
		Collection<Element> coords = doc.getRootElement().getChildren(
				"coordinate");
		for (Element coord : coords) {
			Coordinate c = new Coordinate(Integer.parseInt(coord
					.getAttributeValue("x")), Integer.parseInt(coord
					.getAttributeValue("y")));
			moveCoords.add(c);
		}
		return moveCoords;
	}

	public String chat(int id, String msg) throws IOException {
		String requestXML = "<weewar game='" + id + "'><chat>" + msg
				+ "</chat></weewar>";
		String xml = elizaRequest(requestXML);
		return xml;
	}

}