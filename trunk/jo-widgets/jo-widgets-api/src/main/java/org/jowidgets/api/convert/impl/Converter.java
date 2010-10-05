package org.jowidgets.api.convert.impl;

import org.jowidgets.api.convert.IConverter;
import org.jowidgets.api.convert.IObjectStringConverter;
import org.jowidgets.api.convert.IStringObjectConverter;
import org.jowidgets.api.validation.ValidationMessage;
import org.jowidgets.api.validation.ValidationResult;
import org.jowidgets.util.Assert;

public class Converter<TYPE> implements IConverter<TYPE> {

	private final IObjectStringConverter<TYPE> objectStringConverter;
	private final IStringObjectConverter<TYPE> stringObjectConverter;

	public Converter(
		final IObjectStringConverter<TYPE> objectStringConverter,
		final IStringObjectConverter<TYPE> stringObjectConverter) {
		super();
		Assert.paramNotNull(objectStringConverter, "objectStringConverter");
		Assert.paramNotNull(stringObjectConverter, "stringObjectConverter");
		this.objectStringConverter = objectStringConverter;
		this.stringObjectConverter = stringObjectConverter;
	}

	@Override
	public TYPE convertToObject(final String string) {
		return stringObjectConverter.convertToObject(string);
	}

	@Override
	public ValidationMessage isCompletableToValid(final String string) {
		return stringObjectConverter.isCompletableToValid(string);
	}

	@Override
	public ValidationResult validate(final String validationInput) {
		return stringObjectConverter.validate(validationInput);
	}

	@Override
	public String convertToString(final TYPE value) {
		return objectStringConverter.convertToString(value);
	}

	@Override
	public String getDescription(final TYPE value) {
		return objectStringConverter.getDescription(value);
	}

}
