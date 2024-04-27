package sk.stuba.fei.uim.oop;

import sk.stuba.fei.uim.oop.entity.grant.*;
import sk.stuba.fei.uim.oop.entity.grant.AgencyImplementation;
import sk.stuba.fei.uim.oop.entity.grant.GrantImplementation;
import sk.stuba.fei.uim.oop.entity.grant.ProjectImplementation;
import sk.stuba.fei.uim.oop.entity.organization.CompanyImplementation;
import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.organization.UniversityImplementation;
import sk.stuba.fei.uim.oop.entity.people.PersonImplementation;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

public class Main {

    public static void main(String[] args) {
        OrganizationInterface STU = new UniversityImplementation();
        OrganizationInterface UK = new UniversityImplementation();
        OrganizationInterface ESET = new CompanyImplementation();

        STU.setName("STU");
        UK.setName("UK");
        ESET.setName("ESET");

        PersonInterface Peter = new PersonImplementation();
        PersonInterface Jozef = new PersonImplementation();
        PersonInterface Anna = new PersonImplementation();
        PersonInterface Karol = new PersonImplementation();

        Peter.setName("Peter");
        Jozef.setName("Jozef");
        Anna.setName("Anna");
        Karol.setName("Karol");
        STU.addEmployee(Peter, 2);
        Peter.addEmployer(STU);
        STU.addEmployee(Jozef, 3);
        Jozef.addEmployer(STU);
        UK.addEmployee(Anna, 3);
        UK.addEmployee(Karol, 1);
        Anna.addEmployer(UK);
        Karol.addEmployer(UK);
        ESET.addEmployee(Peter, 4);
        ESET.addEmployee(Anna, 2);
        ESET.addEmployee(Karol, 1);
        Peter.addEmployer(ESET);
        Anna.addEmployer(ESET);
        Karol.addEmployer(ESET);

        AgencyInterface APVV = new AgencyImplementation();
        AgencyInterface VEGA = new AgencyImplementation();
        GrantInterface grant1 = new GrantImplementation();
        GrantInterface grant2 = new GrantImplementation();
        ProjectInterface P1 = new ProjectImplementation();
        ProjectInterface P2 = new ProjectImplementation();

        grant1.setIdentifier("grant1");
        grant1.setBudget(100000);
        grant1.setAgency(APVV);
        grant1.getAgency().setName("APVV");
        System.out.println("GRANT 1: "+grant1.getAgency().getName()+ " BUDGET: " + grant1.getBudget());
        grant2.setIdentifier("grant2");
        grant2.setBudget(8000);
        grant2.setAgency(VEGA);
        grant2.getAgency().setName("VEGA");
        System.out.println("GRANT 2: "+grant2.getAgency().getName()+ " BUDGET: " + grant2.getBudget());

        APVV.addGrant(grant1, 2022);
        VEGA.addGrant(grant2, 2023);

        P1.setApplicant(STU);
        P1.getApplicant().registerProjectInOrganization(P1);
        P1.addParticipant(Peter);
        P1.addParticipant(Jozef);
        P1.setStartingYear(2022);
//        System.out.println("----Projekt 1----");
        P2.setApplicant(UK);
        P2.getApplicant().registerProjectInOrganization(P2);
        P2.addParticipant(Karol);
        P2.addParticipant(Anna);
        P2.setStartingYear(2023);
        grant1.callForProjects();
        grant2.callForProjects();
        grant1.registerProject(P1);
        grant2.registerProject(P2);
        grant1.evaluateProjects();
        grant2.evaluateProjects();
        grant1.closeGrant();
        grant2.closeGrant();
//        System.out.println("Projekt 1 total budget "+P1.getTotalBudget());
//        System.out.println("Projekt 2 total budget "+P2.getTotalBudget());
//        System.out.println("Projekt 1 total budget for year "+P1.getBudgetForYear(2022));
//        System.out.println("Projekt 2 total budget for year "+P2.getBudgetForYear(2023));
//        System.out.println(grant1.getBudgetForProject(P1));
//        System.out.println(grant2.getBudgetForProject(P2));
//        System.out.println("STU PROJECT: " + STU.getProjectBudget(P1));
//        System.out.println("UK PROJECT: " + UK.getProjectBudget(P2));

        System.out.println("tab 1");
        for ( ProjectInterface p : STU.getAllProjects()){
            System.out.println(p.getApplicant().getName() + " " + p.getAllParticipants());
            System.out.println("______" + p.getApplicant().getEmploymentForEmployee(Peter));

            // System.out.println( p.getApplicant().getEmploymentForEmployee(Jozef));
        }

        GrantInterface grant3 = new GrantImplementation();
        grant3.setAgency(APVV);
        grant3.setBudget(100000);
        grant3.setYear(2024);
        APVV.addGrant(grant3, 2024);
        ProjectInterface P3 = new ProjectImplementation();
        P3.setApplicant(ESET);
        P3.getApplicant().registerProjectInOrganization(P3);
        P3.addParticipant(Peter);
        P3.setStartingYear(2024);
        grant3.callForProjects();
        System.out.println(grant3.registerProject(P3));
        grant3.evaluateProjects();
        grant3.closeGrant();
        System.out.println(P3.getTotalBudget());
        System.out.println(P3.getBudgetForYear(2024));
        System.out.println(grant3.getBudgetForProject(P3));
        System.out.println("ESET PROJECT: " + ESET.getProjectBudget(P3));

        GrantInterface grant4 = new GrantImplementation();
        grant4.setAgency(VEGA);
        grant4.setBudget(8000);
        grant4.setYear(2024);
        VEGA.addGrant(grant4, 2024);
        ProjectInterface P4 = new ProjectImplementation();
        P4.setApplicant(ESET);
        P4.getApplicant().registerProjectInOrganization(P4);
        P4.addParticipant(Karol);
        P4.addParticipant(Anna);
        P4.setStartingYear(2024);
        ProjectInterface P5 = new ProjectImplementation();
        P5.setApplicant(STU);
        P5.getApplicant().registerProjectInOrganization(P5);
        P5.addParticipant(Peter);
        P5.addParticipant(Jozef);
        P5.setStartingYear(2024);
        ProjectInterface P6 = new ProjectImplementation();
        P6.setApplicant(UK);
        P6.getApplicant().registerProjectInOrganization(P6);
        P6.addParticipant(Anna);
        P6.setStartingYear(2024);
        ProjectInterface P7 = new ProjectImplementation();
        P7.setApplicant(UK);
        P7.getApplicant().registerProjectInOrganization(P7);
        P7.addParticipant(Karol);
        P7.setStartingYear(2024);
        grant4.callForProjects();
        System.out.println(grant4.registerProject(P4));
        System.out.println(grant4.registerProject(P5));
        System.out.println(grant4.registerProject(P6));
        System.out.println(grant4.registerProject(P7));
        grant4.evaluateProjects();
        grant4.closeGrant();
        System.out.println(P4.getTotalBudget());
        System.out.println(P5.getTotalBudget());
        System.out.println(P6.getTotalBudget());
        System.out.println(P7.getTotalBudget());
        System.out.println(P4.getBudgetForYear(2024));
        System.out.println(grant4.getBudgetForProject(P4));
        System.out.println("ESET PROJECT: "+ ESET.getProjectBudget(P4));
        System.out.println(P5.getBudgetForYear(2024));
        System.out.println(grant4.getBudgetForProject(P5));
        System.out.println(P6.getBudgetForYear(2024));
        System.out.println(grant4.getBudgetForProject(P6));
        System.out.println(P7.getBudgetForYear(2024));
        System.out.println(grant4.getBudgetForProject(P7));

        System.out.println("ESET CELKOVO:" + ESET.getBudgetForAllProjects());
        System.out.println("STU CELKOVO:" + STU.getBudgetForAllProjects());
        System.out.println("UK CELKOVO:" + UK.getBudgetForAllProjects());


    }
}