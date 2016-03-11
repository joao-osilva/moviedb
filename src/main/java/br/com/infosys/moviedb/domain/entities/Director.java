package br.com.infosys.moviedb.domain.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "DIRECTOR")
public class Director implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DIRECTOR_ID", nullable = false, unique = true)
	private Long directorId;

	@Column(name = "NAME", nullable = false, length = 100)
	private String name;

	@Column(name = "BIOGRAPHY", nullable = false, length = 500)
	private String biography;

	@Column(name = "COUNTRY", nullable = false, length = 50)
	private String country;

	@OneToMany(mappedBy = "director")
	private Set<Movie> movies;

	@Version
	private Integer version;

	public Director() {
	}

	public Long getIdDirector() {
		return directorId;
	}

	public void setIdDirector(Long idDirector) {
		this.directorId = idDirector;
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
		result = prime * result + ((directorId == null) ? 0 : directorId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Director))
			return false;

		final Director other = (Director) obj;
		if (directorId == null) {
			if (other.directorId != null)
				return false;
		} else if (!directorId.equals(other.directorId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
