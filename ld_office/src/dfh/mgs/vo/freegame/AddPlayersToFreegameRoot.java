package dfh.mgs.vo.freegame;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dfh.mgs.vo.OrionResponse;

@XmlRootElement(name = "AddPlayersToFreegameResponse")
public class AddPlayersToFreegameRoot extends OrionResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AddPlayersToFreegameResult addPlayersToFreegameResult;

	@XmlElement(name = "AddPlayersToFreegameResult")
	public AddPlayersToFreegameResult getAddPlayersToFreegameResult() {
		return addPlayersToFreegameResult;
	}

	public void setAddPlayersToFreegameResult(AddPlayersToFreegameResult addPlayersToFreegameResult) {
		this.addPlayersToFreegameResult = addPlayersToFreegameResult;
	}

}
