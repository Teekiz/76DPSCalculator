package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ModLoaderService
{
	private final ObjectMapper objectMapper;
	private final File receiversFile;

	@Autowired
	public ModLoaderService(ObjectMapper objectMapper, @Value("${receiver.data.file.path}") String receiverDataFilePath) throws IOException
	{
		this.objectMapper = objectMapper;
		this.receiversFile = new File(receiverDataFilePath);
	}

	public Receiver getReceiver(String receiverName) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(receiversFile);
		JsonNode weaponNode = rootNode.get(receiverName.toUpperCase());
		return objectMapper.treeToValue(weaponNode, Receiver.class);
	}

	public List<Receiver> getAllReceivers() throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(receiversFile);
		List<Receiver> receivers = new ArrayList<>();
		Iterator<JsonNode> elements = rootNode.elements();

		while (elements.hasNext()) {
			JsonNode receiverNode = elements.next();
			Receiver receiver = objectMapper.treeToValue(receiverNode, Receiver.class);
			receivers.add(receiver);
		}

		return receivers;

	}

}
