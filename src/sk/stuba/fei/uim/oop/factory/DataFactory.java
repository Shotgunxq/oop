package sk.stuba.fei.uim.oop.factory;

import sk.stuba.fei.uim.oop.entity.grant.*;
import sk.stuba.fei.uim.oop.entity.organization.CompanyImplementation;
import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.organization.UniversityImplementation;
import sk.stuba.fei.uim.oop.entity.people.PersonImplementation;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DataFactory {

    public static LinkedList<AgencyInterface> getAgencies(int count) {
        LinkedList<AgencyInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            AgencyInterface agency = new AgencyImplementation( "Agency " + i);
            retVal.add(agency);
        }
        return retVal;
    }

    public static LinkedList<GrantInterface> getGrants(int count) {
        String identifier = "GR2024XYZ"; // Example identifier
        int year = 2024; // Example year
        AgencyInterface agency = new AgencyImplementation("Agency"); // Example agency implementation
        int budget = Constants.COMPANY_INIT_OWN_RESOURCES; // Example budget

        LinkedList<GrantInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            GrantImplementation grant = new GrantImplementation(identifier, year, agency, budget);
            retVal.add(grant);
        }
        return retVal;
    }

    public static LinkedList<ProjectInterface> getProjects(int count) {
        LinkedList<ProjectInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            ProjectInterface project = new ProjectImplementation();
            retVal.add(project);
        }
        return retVal;
    }

    public static LinkedList<OrganizationInterface> getUniversities(int count) {
        LinkedList<OrganizationInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            OrganizationInterface org = new UniversityImplementation("STU");
            retVal.add(org);
        }
        return retVal;
    }

    public static LinkedList<OrganizationInterface> getCompanies(int count) {
        String name = "TechSolutions Inc."; // Example company name
        Set<PersonInterface> employees = new HashSet<>(); // Example set of employees
//        employees.add(new PersonImplementation("Bence", "Trakany", new HashSet<>()); // Example employee
//        employees.add(new PersonImplementation("Bodnar", "Velke", new HashSet<>()); // Example employee

        Set<ProjectInterface> projects = new HashSet<>(); // Example set of projects
        projects.add(new ProjectImplementation()); // Example project
        projects.add(new ProjectImplementation()); // Example project

        int initialBudget = 1000000; // Example initial budget
        int remainingBudget = 750000; // Example remaining budget

        CompanyImplementation company = new CompanyImplementation(name, employees, projects, initialBudget, remainingBudget);

        LinkedList<OrganizationInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            CompanyImplementation org = new CompanyImplementation(name, employees, projects, initialBudget, remainingBudget);
            retVal.add(org);
        }
        return retVal;
    }

    public static LinkedList<PersonInterface> getPersons(int count) {
        LinkedList<PersonInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            PersonInterface person = new PersonImplementation("Bence", "Trakany", new HashSet<>());
            retVal.add(person);
        }
        return retVal;
    }
}
