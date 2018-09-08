package edu.ucsb.cs56.pconrad;

import lombok.Data;


/**
   RouteEntry is a class that will have getters and
   setters by virtue of Lombok (<a href="https://projectlombok.org/">https://projectlombok.org</a>)
   
*/

@Data
class RouteEntry {
    private String httpMethod;
    private String uri;
	private spark.Route route;
    private String description;

	RouteEntry(String httpMethod,
			   String uri,
			   spark.Route route,
			   String description) {
		this.httpMethod = httpMethod;
		this.uri = uri;
		this.route = route;
		this.description = description;		
	}

}
