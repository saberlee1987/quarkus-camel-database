package com.saber.camel.entity;

import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "persons")
@NamedQueries(
		value = {
				@NamedQuery(name = "findPersonByNationalCode",query = "select p from Person p where p.nationalCode=:nationalCode")
		}
)
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "firstName",length = 70)
	private String firstName;
	@Column(name = "lastName",length = 85)
	private String lastName;
	@Column(name = "nationalCode",length = 10,unique = true)
	private String nationalCode;
	private Integer age;
	
	@Override
	public String toString() {
		return new GsonBuilder()
				.setLenient()
				.setPrettyPrinting()
				.setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
				.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
				.enableComplexMapKeySerialization()
				.create()
				.toJson(this, Person.class);
	}
}
