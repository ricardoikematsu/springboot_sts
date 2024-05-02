package com.ikematsu.integrationtests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

@XmlRootElement
public class WrapperPersonDTO implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@JsonProperty("_embedded")
	private PersonEmbeddedDTO embedded;

	public WrapperPersonDTO() {}

	public PersonEmbeddedDTO getEmbedded() {
		return embedded;
	}

	public void setEmbedded(PersonEmbeddedDTO embedded) {
		this.embedded = embedded;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((embedded == null) ? 0 : embedded.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WrapperPersonDTO other = (WrapperPersonDTO) obj;
		if (embedded == null) {
			if (other.embedded != null)
				return false;
		} else if (!embedded.equals(other.embedded))
			return false;
		return true;
	}
}