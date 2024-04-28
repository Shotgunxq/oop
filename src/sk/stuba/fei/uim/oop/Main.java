package sk.stuba.fei.uim.oop;

import sk.stuba.fei.uim.oop.entity.grant.*;
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
        APVV.setName("APVV");
        AgencyInterface VEGA = new AgencyImplementation();
        VEGA.setName("VEGA");
        GrantInterface grant1 = new GrantImplementation();
        GrantInterface grant2 = new GrantImplementation();
        ProjectInterface P1 = new ProjectImplementation();
        ProjectInterface P2 = new ProjectImplementation();

        grant1.setIdentifier("grant1");
        grant1.setBudget(100000);
        grant1.setAgency(APVV);
        grant2.setIdentifier("grant2");
        grant2.setBudget(8000);
        grant2.setAgency(VEGA);
        APVV.addGrant(grant1, 2022);
        VEGA.addGrant(grant2, 2023);

        P1.setApplicant(STU);
        P1.addParticipant(Peter);
        P1.addParticipant(Jozef);
        P1.setStartingYear(2022);
        STU.registerProjectInOrganization(P1);
        P2.setApplicant(UK);
        P2.addParticipant(Karol);
        P2.addParticipant(Anna);
        P2.setStartingYear(2023);
        UK.registerProjectInOrganization(P2);
        grant1.callForProjects();
        grant2.callForProjects();
        grant1.registerProject(P1);
        grant2.registerProject(P2);
        grant1.evaluateProjects();
        grant2.evaluateProjects();
        grant1.closeGrant();
        grant2.closeGrant();
        System.out.println("1:"+P1.getTotalBudget());
        System.out.println("2:"+P2.getTotalBudget());
        System.out.println("3:"+P1.getBudgetForYear(2022));
        System.out.println("4:"+P2.getBudgetForYear(2023));
        System.out.println("5:ENDING YEAR"+P1.getEndingYear());
        System.out.println("6:ENDING YEAR"+P2.getEndingYear());
        System.out.println("7:"+grant1.getBudgetForProject(P1));
        System.out.println("8:"+grant2.getBudgetForProject(P2));
        System.out.println("9:STU PROJECT: " + STU.getProjectBudget(P1));
        System.out.println("10:UK PROJECT: " + UK.getProjectBudget(P2));


        GrantInterface grant3 = new GrantImplementation();
        grant3.setAgency(APVV);
        grant3.setBudget(100000);
        grant3.setYear(2020);
        APVV.addGrant(grant3, 2020);
        ProjectInterface P3 = new ProjectImplementation();
        P3.setApplicant(ESET);
        P3.addParticipant(Peter);
        P3.setStartingYear(2020);
        ESET.registerProjectInOrganization(P3);
        grant3.callForProjects();
        System.out.println("11:"+grant3.registerProject(P3));
        grant3.evaluateProjects();
        grant3.closeGrant();
        System.out.println("12:"+APVV.getAllGrants());
        System.out.println("13:"+P3.getTotalBudget());
        System.out.println("14:"+P3.getBudgetForYear(2020));
        System.out.println("15:"+grant3.getBudgetForProject(P3));
        System.out.println("16:ESET PROJECT: " + ESET.getProjectBudget(P3));

//        GrantInterface grantX = new GrantImplementation();
//        grantX.setAgency(APVV);
//        grantX.setBudget(100000);
//        grantX.setYear(2021);
//        APVV.addGrant(grantX, 2021);
//        ProjectInterface PX = new ProjectImplementation();
//        PX.setApplicant(STU);
//        PX.addParticipant(Peter);
//        PX.setStartingYear(2021);
//        STU.registerProjectInOrganization(PX);
//        grantX.callForProjects();
//        System.out.println("17:"+grantX.registerProject(PX));
//        grantX.evaluateProjects();
//        grantX.closeGrant();
//        System.out.println("18:"+APVV.getAllGrants());
//        System.out.println("19:"+PX.getTotalBudget());
//        System.out.println("20:"+PX.getBudgetForYear(2021));
//        System.out.println("21:"+grantX.getBudgetForProject(PX));
//        System.out.println("22:ESET PROJECT: " + ESET.getProjectBudget(PX));

        GrantInterface grant4 = new GrantImplementation();
        grant4.setAgency(VEGA);
        grant4.setBudget(8000);
        grant4.setYear(2024);
        VEGA.addGrant(grant4, 2024);
        ProjectInterface P4 = new ProjectImplementation();
        P4.setApplicant(ESET);
        P4.addParticipant(Karol);
        P4.addParticipant(Anna);
        P4.setStartingYear(2024);
        ESET.registerProjectInOrganization(P4);
        ProjectInterface P5 = new ProjectImplementation();
        P5.setApplicant(STU);
        P5.addParticipant(Peter);
        P5.addParticipant(Jozef);
        P5.setStartingYear(2024);
        STU.registerProjectInOrganization(P5);
        ProjectInterface P6 = new ProjectImplementation();
        P6.setApplicant(UK);
        P6.addParticipant(Anna);
        P6.setStartingYear(2024);
        UK.registerProjectInOrganization(P6);
        ProjectInterface P7 = new ProjectImplementation();
        P7.setApplicant(UK);
        P7.addParticipant(Karol);
        P7.setStartingYear(2024);
        UK.registerProjectInOrganization(P7);
        grant4.callForProjects();
        System.out.println("23:"+grant4.registerProject(P4));
        System.out.println("24:"+grant4.registerProject(P5));
        System.out.println("25:"+grant4.registerProject(P6));
        System.out.println("26:"+grant4.registerProject(P7));
        grant4.evaluateProjects();//TODO
        grant4.closeGrant();
        System.out.println("27:"+P4.getTotalBudget());
        System.out.println("28:"+P5.getTotalBudget());
        System.out.println("29:"+P6.getTotalBudget());
        System.out.println("30:"+P7.getTotalBudget());
        System.out.println("31:"+P4.getBudgetForYear(2024));
        System.out.println("32:"+grant4.getBudgetForProject(P4));
        System.out.println("33:ESET PROJECT: "+ ESET.getProjectBudget(P4));
        System.out.println("34:"+P5.getBudgetForYear(2024));
        System.out.println("35:"+grant4.getBudgetForProject(P5));
        System.out.println("36:"+P6.getBudgetForYear(2024));
        System.out.println("37:"+grant4.getBudgetForProject(P6));
        System.out.println("38:"+P7.getBudgetForYear(2024));
        System.out.println("39:"+grant4.getBudgetForProject(P7));

        System.out.println("40:ESET CELKOVO:" + ESET.getBudgetForAllProjects());
        System.out.println("41:STU CELKOVO:" + STU.getBudgetForAllProjects());
        System.out.println("42:UK CELKOVO:" + UK.getBudgetForAllProjects());

    }
}