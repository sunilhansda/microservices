//package com.hansda.movie.catalog.service.model;
package com.hansda.moviecatalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatalogItem {

	private String name;
	private String description;
	private int rating;
}
