//ENTITY CLASS USER
//MAP TO TABLES IN DB
//EACH OBJECT MAPS TO A ROW IN THE TABLE AND EACH FEILD MAPS TO A COLUMN

package com.oopproject.VirtualStockMarketSimulator.model;
/*
ATTRIBUTES OF USER
ID -------------bigint(allows to store big >8byte integers); cant be kept null, mysql populates it, and it is auto
username -------User’s login name, must be unique. varchar(50)
Password Hash---Encrypted password. varchar(255) encryption space
created at------When the user registered (autofilled).
 */
// toolkit for the class
import jakarta.persistence.*;
// Java persistence API imported so we can manage and connect to db without having to run SQL code
//allows for the objects to be mapped to the db. this is just an interface import
//also defines the relationships between objects 1:1, 1:M, M:N
import lombok.Getter;
import lombok.Setter;
//lombok reduces writing boilerplate code by allowing annotations for constructors, getter and setters
import java.sql.Timestamp;
//it is the specific class in the sql library that matches the data type of created_at in my sql db
import org.springframework.security.core.userdetails.UserDetails;
//class creation now
@Entity //basically tell springboot this class is a db table
@Table(name = "users") //tells springboot the class user maps to db users
@Getter //create the get method for all attributes.
@Setter //create the set method for all attributes.

public class User implements UserDetails {

    @Id
    // this marks this field as the primary key,directly connects to ID in db
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Let MySql handle autoincrement of IDs;
    // tells them to not generate ids themselves, and ask db to
    //map the db to java and then decide on the attribute that does get
    //the assigned initialization
    private Long id;
    //primary key field to hold the ID (connected to db)

    @Column(nullable = false, unique = true, length = 50)
    //map java field to the db column
    //nullable = false means a value must always be provided (SQL: NOT NULL)
    //unique = true all values must be unique no two same usernames (SQL: UNIQUE)
    //length = 50; means (SQL: VARCHAR(50))
    private String username; // java side field for username

    @Column(name = "password_hash", nullable = false, length = 255)
    //this is the same as above, map the db to java, then assign
    //an attribute field
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    // field mapped to MySql created_at
    //it cannot be empty
    //cannot update it once it is saved
    //Use this exact SQL definition when creating the table.
    private Timestamp createdAt;

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // We can leave these as simple returns for this project:
    @Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return new java.util.ArrayList<>();
    }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // ... existing code ...

    @PrePersist // <--- ADD THIS ANNOTATION
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new java.sql.Timestamp(System.currentTimeMillis());
        }
    }
}



/*
ORM: Object Relational Mapping
JPA:
“A standard set of rules for ORM in Java.”
interface for labels and stuff
Hibernate:
“The most popular ORM tool that implements JPA and performs real SQL operations.”
realtime sql operations on the db but by eliminating the need for running sql queries
 */