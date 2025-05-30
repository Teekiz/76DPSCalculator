package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Keyable;
import Tekiz._DPSCalculator._DPSCalculator.persistence.MutationRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a mutation that a player character might have.
 * @param id An identifier used if the object has been retrieved from a database.
 *           This is not required if object has been stored in a JSON file.
 * @param name The name of the mutation.
 * @param description The description of the perks effects.
 * @param positiveEffects Stores the positive effects a mutation has.
 * @param negativeEffects Stores the negative effects a mutation has.
 */
@Document(collection = "mutation")
@RepositoryObject(repository = MutationRepository.class)
public record Mutation(@Id
					   @JsonProperty("id") @JsonAlias("_id") String id,
					   @JsonProperty("name") String name,
					   @JsonProperty("description") String description,
					   @JsonProperty("positiveEffects")
					   MutationEffects positiveEffects,
					   @JsonProperty("negativeEffects")
					   MutationEffects negativeEffects) implements Keyable
{
	/**
	 * A method that aggregates and returns all positive and negative {@link Mutation} {@link Modifier}s.
	 *
	 * @return A {@link HashMap} of {@link Modifier}s and condition values.
	 */
	@JsonIgnore
	public List<Modifier> aggregateMutationEffects()
	{
		List<Modifier> mutations = new ArrayList<>();
		if (negativeEffects != null)
		{
			mutations.add(negativeEffects);
		}
		if (positiveEffects != null)
		{
			mutations.add(positiveEffects);
		}
		return mutations;
	}

	@Override
	public boolean equals(Object object)
	{
		if (this == object)
		{
			return true;
		}
		if (object == null || getClass() != object.getClass())
		{
			return false;
		}
		Mutation mutation = (Mutation) object;
		return Objects.equals(id, mutation.id) && Objects.equals(name, mutation.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name);
	}
}
