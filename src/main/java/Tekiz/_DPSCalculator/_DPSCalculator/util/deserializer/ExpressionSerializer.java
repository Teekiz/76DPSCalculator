package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;

/**
 * A utility service used to allow a {@link JsonGenerator} to convert a {@link Expression} object into an {@link String} object.
 */
@Slf4j
public class ExpressionSerializer extends JsonSerializer<Expression>
{
	/**
	 * A method used to convert a {@link Expression} object into an {@link String} object.
	 * @param expression The {@link Expression} object to be converted into a string.
	 * @param jsonGenerator An object used to convert the resulted string into Json.
	 * @throws IOException If expression cannot be converted or the {@link JsonGenerator} cannot write the string to Json.
	 */
	@Override
	public void serialize(Expression expression, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
	{
		String conditionString = expression.getExpressionString();
		log.debug("Serializing expression: '{}'", conditionString);
		jsonGenerator.writeString(conditionString);
	}
}
