package com.codehunter.springcitrussexample.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Getter
public class Todo {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
  @Type(
      type =
          "org.hibernate.type.UUIDCharType") // use that type to save value as string instead of
                                             // binary
  private UUID id;

  @Setter private String task;
}
