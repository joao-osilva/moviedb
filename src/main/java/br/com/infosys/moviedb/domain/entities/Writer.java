package br.com.infosys.moviedb.domain.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Writer entity.
 * 
 * @author vitor191291@gmail.com
 *
 */
@Entity
@Table(name = "WRITER")
public class Writer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WRITER_ID", nullable = false, unique = true)
	private Long writerId;

	@Column(name = "NAME", nullable = false, length = 100)
	private String name;

	@Column(name = "BIOGRAPHY", nullable = false, length = 500)
	private String biography;

	@Column(name = "COUNTRY", nullable = false, length = 50)
	private String country;

	@ManyToMany(mappedBy = "writers")
	@JsonIgnore
	private Set<Movie> movies;

	@Version
	private Integer version;

	public Writer() {
	}

	public Long getIdWriter() {
		return writerId;
	}

	public void setIdWriter(Long idWriter) {
		this.writerId = idWriter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<Movie> getMovies() {
		return movies;
	}

	public void setMovies(Set<Movie> movies) {
		this.movies = movies;
	}

	public Integer getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((writerId == null) ? 0 : writerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Writer))
			return false;
		
		final Writer other = (Writer) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (writerId == null) {
			if (other.writerId != null)
				return false;
		} else if (!writerId.equals(other.writerId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
