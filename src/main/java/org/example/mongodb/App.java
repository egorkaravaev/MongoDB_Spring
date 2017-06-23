package org.example.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@Component
public class App implements CommandLineRunner {

    @Autowired
    private StudentRepository studRepository;

    @Autowired
    private UniversityRepository univRepository;

    public static void main( String[] args ) throws Exception {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        showMenu();
    }

    private void showMenu() {
        System.out.println();
        System.out.println("Choose 1 to add student\n" +
                "Choose 2 to delete student\n" +
                "Choose 3 to search student\n" +
                "Choose 4 to show data base\n" +
                "Choose 5 to delete data base\n" +
                "Choose 6 to exit\n");
        switch(readTheNumb()){
            case 1:
                addStudent();
                break;
            case 2:
                deleteStudent();
                break;
            case 3:
                searchStudent();
                break;
            case 4:
                showDB();
                break;
            case 5:
                deleteDB();
                break;
            case 6:
                break;
            default:
                System.out.println("Choose between 1-6!\n");
                showMenu();
                break;
        }
    }

    private void addStudent() {
        Student stud = new Student();
        System.out.println("Enter student's name, surname, age and department:");
        stud.setName(readTheLine());
        stud.setSurname(readTheLine());
        stud.setAge(readTheNumb());
        String department = readTheLine();
        studRepository.save(stud);
        if(univRepository.findByDepartment(department) == null){
            University university = new University();
            university.setDepartment(department);
            university.getStudents().add(stud);
            univRepository.save(university);
        } else {
            University university = univRepository.findByDepartment(department);
            university.getStudents().add(stud);
            univRepository.save(university);
        }
        System.out.println();
        showMenu();
    }

    private void searchStudent() {
        System.out.println("Choose 1 to search student by name and age\n" +
                "Choose 2 to search student by name\n");
        switch(readTheNumb()){
            case 1:
                search1();
                break;
            case 2:
                search2();
                break;
            default:
                System.out.println("Choose between 1-2!\n");
                searchStudent();
                break;
        }

    }

    private void search1() {
        System.out.println("Enter student's name:");
        String name = readTheLine();
        System.out.println("Enter student's age: ");
        int age = readTheNumb();
        List<Student> studentList = studRepository.findAllByNameAndAge(name,age);
        for (Student st : studentList) {
            System.out.println(st.getId()+" "+st.getName()+" "+st.getSurname()+" "+st.getAge());
        }
        System.out.println();
        showMenu();
    }

    private void search2() {
        System.out.println("Enter student's name:");
        String name = readTheLine();
        List<Student> studentList = studRepository.findAllByName(name);
        for (Student st : studentList) {
            System.out.println(st.getId()+" "+st.getName()+" "+st.getSurname()+" "+st.getAge());
        }
        System.out.println();
        showMenu();
    }

    private void deleteStudent() {
        System.out.println("Enter name of student to delete:");
        String name = readTheLine();
        for (University university: univRepository.findAll()) {
            List<Student> studentList = university.getStudents();
            for (Iterator<Student> iterator = studentList.iterator(); iterator.hasNext();) {
                if(iterator.next().getName().equals(name)){
                    iterator.remove();
                    univRepository.save(university);
                }
            }
        }
        studRepository.deleteByName(name);
        System.out.println();
        showMenu();
    }

    private void showDB() {
        for (University un: univRepository.findAll()) {
            System.out.println(un.getDepartment()+":");
            for (Student st: un.getStudents()) {
                System.out.println(st.getId()+" "+st.getName()+" "+st.getSurname()+" "+st.getAge());
            }
            System.out.println();
        }
        System.out.println("Number of students: " + studRepository.count() + "\n");
        showMenu();
    }

    private void deleteDB() {
        studRepository.deleteAll();
        univRepository.deleteAll();
        showMenu();
    }

    private int readTheNumb(){
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()) {
            return sc.nextInt();
        } else {
            System.out.println("It should be a number! Try again!");
            return readTheNumb();
        }

    }

    private String readTheLine(){
        Scanner sc = new Scanner(System.in);
        String str = sc.next();
        boolean b = checkWithRegExp(str);
        if (b){
            return str;
        } else {
            System.out.println("It should be a line! Try again!");
            return readTheLine();
        }
    }

    public static boolean checkWithRegExp(String str){
        Pattern p = Pattern.compile("^[a-zA-Z]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }


}
