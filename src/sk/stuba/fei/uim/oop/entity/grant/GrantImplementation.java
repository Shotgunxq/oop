package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.*;
import java.util.stream.Collectors;

public class GrantImplementation implements GrantInterface {

    private String identifier;
    private int year;
    private AgencyInterface agency;
    private int budget;
    private int remainingBudget;
    //TODO: aby sa raz tam pridalo a aby poradie sa zanechalo SET HASHSET NEICO EXISST
    private GrantState state;
    private Set<ProjectInterface> registeredProjects;
    private HashMap<ProjectInterface, Integer> projectBudgets;
    private List<OrganizationInterface> registeredOrganizations;

    public GrantImplementation() { // No arguments constructor
        this.registeredProjects = new HashSet<>();
        this.projectBudgets = new HashMap<>();
        this.registeredOrganizations = new ArrayList<>();

    }


    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public AgencyInterface getAgency() {
        return agency;
    }

    @Override
    public void setAgency(AgencyInterface agency) {
        this.agency = agency;
    }

    @Override
    public int getBudget() {
        return budget;
    }

    @Override
    public void setBudget(int budget) {
        this.budget = budget;
        this.remainingBudget = budget;
    }

    @Override
    public int getRemainingBudget() {
        return remainingBudget;
    }


    @Override
    public int getBudgetForProject(ProjectInterface project) {
        if (!registeredProjects.contains(project)) {
            return 0; // Project is not registered in the grant
        }

        // Calculate budget based on the number of years the project is funded
        int projectDuration = project.getDuration();
        if (projectDuration == 0) {
            return 0; // Project duration is not valid
        }

        // Calculate budget per year
        int budgetPerYear = budget / projectDuration;

        return budgetPerYear;
    }

    @Override
    public boolean registerProject(ProjectInterface project) {
        if (state != GrantState.STARTED || year != project.getStartingYear() || project.getAllParticipants().isEmpty()) {
            return false;
        }
        registeredProjects.add(project);
        return true;
    }


    @Override
    public Set<ProjectInterface> getRegisteredProjects() {
        return registeredProjects;
    }

    @Override
    public GrantState getState() {
        return state;
    }

    @Override
    public void callForProjects() {
        state = GrantState.STARTED;
    }
    int allocatedBudgetPerProject;

    @Override
    public void evaluateProjects() {
        List<ProjectInterface> eligibleProjects = new ArrayList<>();

        for (ProjectInterface project : registeredProjects) {
            boolean isEligible = checkCapacity(project); // Check solver capacity

            // Set project budget based on eligibility
            if (isEligible) {
                eligibleProjects.add(project);
            } else {
                project.setBudgetForYear(project.getStartingYear(), 0); // Set budget to 0 for ineligible projects
            }
        }

        // Calculate allocated budget per eligible project (consider empty list scenario)
        int numEligibleProjects = eligibleProjects.size();
        if (numEligibleProjects == 0) {
            allocatedBudgetPerProject = 0; // No budget allocation if no projects are eligible
        } else {
            allocatedBudgetPerProject = remainingBudget / numEligibleProjects;
        }

        // Update budgets and notify organizations (only for eligible projects)
        for (ProjectInterface project : eligibleProjects) {
            // Calculate yearly budget based on project duration
            int projectDuration = project.getDuration();
            int yearlyBudget = allocatedBudgetPerProject / projectDuration;

            // Set budget for each year of the project
            for (int year = project.getStartingYear(); year < project.getStartingYear() + projectDuration; year++) {
                project.setBudgetForYear(year, yearlyBudget);
            }

            // Update projectBudgets HashMap with total allocated budget
            projectBudgets.put(project, allocatedBudgetPerProject);

            project.getApplicant().projectBudgetUpdateNotification(project, project.getStartingYear(), allocatedBudgetPerProject); // Total allocated budget
        }

        state = GrantState.CLOSED; // Update state after evaluation and allocation
    }



    @Override
    public void closeGrant() {
        if (state != GrantState.EVALUATING) {
            return;
        }
        remainingBudget = budget;

        // After project evaluation (assuming closeGrant is called after evaluation)
        for (ProjectInterface project : registeredProjects) {
            // Check if project is registered (assuming registration implies approval)
            if (!registeredProjects.contains(project)) {
                continue;
            }

            // Access project budget for the current year (assuming no additional input needed)
            int currentYearBudget = project.getBudgetForYear(year);

            // Assuming non-zero budget implies project approval
            if (currentYearBudget > 0) {
                // Proceed with budget access or calculations for approved projects
                int totalBudget = project.getTotalBudget(); // Or access specific year budgets
                // Use project budget information for your application logic (e.g., notifying organizations)
            }
        }

        state = GrantState.CLOSED;
    }

    private boolean checkCapacity(ProjectInterface project) {
        int maxWorkload = 5; // The maximum workload allowed per year

        // Iterate through each year of the project
        for (int year = project.getStartingYear(); year <= project.getEndingYear(); year++) {
            // Iterate through each participant of the project
            for (PersonInterface participant : project.getAllParticipants()) {
                int totalWorkloadForYear = 0;

                // Iterate through each grant of the agency
                for (GrantInterface grant : agency.getAllGrants()) {
                    // Check if the grant contains the project
                    if (grant.getRegisteredProjects().contains(project)) {
                        // Iterate through each participant of the project in the grant
                        for (PersonInterface grantParticipant : project.getAllParticipants()) {
                            // Check if the participant is the same as the current participant
                            if (grantParticipant.equals(participant)) {
                                // Get the employment level for the participant at the applicant organization of the project
                                OrganizationInterface applicant = project.getApplicant();
                                int employment = applicant.getEmploymentForEmployee(participant);

                                // Calculate the workload for this project and add it to the total workload for the year
                                totalWorkloadForYear += (employment / 100.0) * grant.getBudgetForProject(project) / project.getDuration();
                            }
                        }
                    }
                }

                // Check if the participant's workload for the current year exceeds the limit
                if (totalWorkloadForYear > maxWorkload) {
                    System.out.println("!!!!!!!!!!!!!!!!Workload exceeded for year " + year + " and participant " + participant.getName() );
                    return false; // Workload exceeded for this year and participant
                }
            }
        }
        System.out.println("Workload not exceeded for any year and participant!!!!!!!!");
        return true; // Workload not exceeded for any year and participant
    }










}