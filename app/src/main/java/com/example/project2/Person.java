package com.example.project2;

public class Person {
    private String Name;
    private String Number;
    private String Email;
    private Long Photo_id;
    //private Long Photo_id;
    //private Long Person_id;

    public Person() {}

    public Person(String name, String email, String number, Long photo_id)
    {
        this.Name = name;
        this.Email = email;
        this.Number = number;
        this.Photo_id = photo_id;
    }

    public Long getPhoto_id() { return Photo_id; }

    public void setPhoto_id(Long photo_id) { this.Photo_id = photo_id; }

    //public Long getPerson_id() { return Person_id; }

    //public void setPerson_id(Long person_id) { this.Person_id = person_id; }

    public String getEmail() { return Email; }

    public void setEmail(String email) { this.Email = email; }

    public String getName()
    {
        return Name;
    }

    public void setName(String name) { this.Name = name; }

    public String getNumber()
    {
        return Number;
    }

    public void setNumber(String number) { this.Number = number; }
}
