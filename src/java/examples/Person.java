package examples;

public class Person {

    String name;
    private String firstName;
    private String lastName;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public Person(String firstName, String lastName, int age) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    
    public Person() {
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    

}
