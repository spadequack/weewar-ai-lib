package weewarai.model;

import org.jdom.Element;

public class HqGame {

	private int id;
	private String name;
	
	private boolean isRequiringAnInviteAccept = false;
	private boolean isInNeedOfAttention;

	private String state;
	private String url;

	public void parseXmlElement(Element ele) {
		String att = ele.getAttributeValue("inNeedOfAttention");
		setInNeedOfAttention(att != null && att.equals("true"));

		setId(Integer.parseInt(ele.getChildText("id")));

		setName(ele.getChildText("name"));

		// running, lobby, or finished
		setState(ele.getChildText("state"));

		setUrl(ele.getChildText("url"));

		setRequiringAnInviteAccept(getUrl().contains("join"));

		// there's also a <since> child
		// there's also a <result> field if game is finished
	}

	// ////////////////// Getters and Setters //////////////////////

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isRequiringAnInviteAccept
	 */
	public boolean isRequiringAnInviteAccept() {
		return isRequiringAnInviteAccept;
	}

	/**
	 * @param isRequiringAnInviteAccept
	 *            the isRequiringAnInviteAccept to set
	 */
	public void setRequiringAnInviteAccept(boolean isRequiringAnInviteAccept) {
		this.isRequiringAnInviteAccept = isRequiringAnInviteAccept;
	}

	/**
	 * @return the isInNeedOfAttention
	 */
	public boolean isInNeedOfAttention() {
		return isInNeedOfAttention;
	}

	/**
	 * @param isInNeedOfAttention
	 *            the isInNeedOfAttention to set
	 */
	public void setInNeedOfAttention(boolean isInNeedOfAttention) {
		this.isInNeedOfAttention = isInNeedOfAttention;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
