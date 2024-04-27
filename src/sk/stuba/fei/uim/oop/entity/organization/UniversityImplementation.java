package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;

public class UniversityImplementation extends OrganizationImplementation {

    @Override
    public int getProjectBudget(ProjectInterface pi) {
        // Universities only receive funding from grant agency
        if (projects.contains(pi)) {
            // Implement logic to get budget from grant agency for the project
            return pi.getTotalBudget() /* grant agency budget for project */;
        } else {
            return 0;
        }
    }
}
