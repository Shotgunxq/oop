package sk.stuba.fei.uim.oop.entity.grant;
import sk.stuba.fei.uim.oop.utility.Constants;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GrantImplementation implements GrantInterface {

    private String identifier;
    private int year;
    private AgencyInterface agency;
    private int budget;
    private int remainingBudget;
    private GrantState state;
    private Set<ProjectInterface> registeredProjects;
    private HashMap<ProjectInterface, Integer> projectBudgets;

    public GrantImplementation(String identifier, int year, AgencyInterface agency, int budget) {
        this.identifier = identifier;
        this.year = year;
        this.agency = agency;
        this.budget = budget;
        this.remainingBudget = budget;
        this.state = GrantState.NEW;
        this.projectBudgets = new HashMap<>();
        this.registeredProjects = new HashSet<>();
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
        return projectBudgets.getOrDefault(project, 0);
    }

    @Override
    public boolean registerProject(ProjectInterface project) {
        if (state == GrantState.STARTED && project.getStartingYear() == year && project.getAllParticipants().size() > 0) {
            registeredProjects.add(project);
            return true;
        }
        return false;


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
        if (state == GrantState.NEW) {
            state = GrantState.STARTED;
        }
    }

    @Override
    public void evaluateProjects() {
        if (state == GrantState.STARTED && !registeredProjects.isEmpty()) {
            int numProjectsToFund = Math.min(registeredProjects.size() / 2, registeredProjects.size());
            int budgetPerProject = remainingBudget / numProjectsToFund;
            int fundedProjects = 0;
            for (ProjectInterface project : registeredProjects) {
//                if (fundedProjects < numProjectsToFund && project.checkResearchCapacity()) {
//TODO: valamit ide mert ez a checkresearchcapacity nem jo
                if (fundedProjects < numProjectsToFund && project.getBudgetForYear(year) > project.getAllParticipants().size() * Constants.MAX_EMPLOYMENT_PER_AGENCY)
                {
                    projectBudgets.put(project, budgetPerProject);
                    remainingBudget -= budgetPerProject;
                    fundedProjects++;
                } else {
                    projectBudgets.put(project, 0);
                }
            }
            state = GrantState.EVALUATING;
        }


//        if (state == GrantState.STARTED && !registeredProjects.isEmpty()) {
//            int numProjectsToFund = Math.min(registeredProjects.size() / 2, registeredProjects.size());
//
//            // Calculate maximum total funding based on company resources
//            int maxTotalFunding = Math.min(Constants.COMPANY_INIT_OWN_RESOURCES, numProjectsToFund * Constants.PROJECT_DURATION_IN_YEARS * Constants.MAX_FUNDING_PER_PROJECT); // Assuming Constants.MAX_FUNDING_PER_PROJECT is defined
//
//            // Calculate budget per project based on remaining resources and number of projects
//            int budgetPerProject = Math.min(remainingBudget, maxTotalFunding / numProjectsToFund);
//            int fundedProjects = 0;
//            for (ProjectInterface project : registeredProjects) {
////                if (fundedProjects < numProjectsToFund && project.checkResearchCapacity())
//                    if (fundedProjects < numProjectsToFund && project.getBudgetForYear(year) > project.getAllParticipants().size() * Constants.MAX_EMPLOYMENT_PER_AGENCY)
//                {
//                    projectBudgets.put(project, budgetPerProject);
//                    remainingBudget -= budgetPerProject;
//                    fundedProjects++;
//                } else {
//                    projectBudgets.put(project, 0);
//                }
//            }
//            state = GrantState.EVALUATING;
//        }
    }

    @Override
    public void closeGrant() {
            if (state == GrantState.EVALUATING) {
                for (ProjectInterface project : registeredProjects) {
                    int allocatedBudget = projectBudgets.get(project);
                    int yearlyBudget = allocatedBudget / Constants.PROJECT_DURATION_IN_YEARS;
                    project.setBudgetForYear(year, yearlyBudget);
                }
                state = GrantState.CLOSED;
            }

    }
}
