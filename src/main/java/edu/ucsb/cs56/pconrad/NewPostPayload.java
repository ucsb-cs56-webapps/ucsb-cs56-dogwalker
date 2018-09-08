package edu.ucsb.cs56.pconrad;


import lombok.Data;
import com.fasterxml.jackson.annotation.JsonAlias;

/**                                                                                                            
  NewPostPayload is a class that will have getters and                                                                  
   setters by virtue of Lombok (<a href="https://projectlombok.org/">https://projectlombok.org</a>)            
                                                                                                               
                                                                                                               
*/


@Data
class NewPostPayload {
    private int id;
    private String email;
    private String password;
    private String name;
    private String dogType;
    private String boyOrGirl;
    private String description;
    private String availability;
}







