package com.company.audit;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeAudit
{
    public static void main(String[] args)
    {
        //reading the input from employee.java
        List<Employee> employees = Employee.getSampleData();

        //1. List of high-earning engineers list
        List<Employee> highEarningEngineers =
                employees.stream()
                        .filter(Employee -> Employee.getSalary() > 70000 )
                        .filter(Employee -> Employee.getDepartment().equals("ENGINEERING"))
                        .toList();

        //2. Standardized Name Report
        List<String> standardizedNamed =
                employees.stream()
                        .map(Employee -> Employee.getName().toUpperCase())
                        .collect(Collectors.toList());

        //3. Total Annual Salary Budget
        double totalSalaryBudget =
                employees.stream()
                        .mapToDouble(Employee -> Employee.getSalary())
                        .sum();


        //printing the results
        System.out.println("High-Earning Engineers:");
        highEarningEngineers.forEach(System.out::println);

        System.out.println("Standardized Named:");
        standardizedNamed.forEach(System.out::println);

        System.out.println("Total Salary Budget:");
        System.out.println("$" + totalSalaryBudget);
    }
}