package sk.stuba.fei.uim.oop.entity.people;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;

import java.util.Set;

public class PersonImplementation implements PersonInterface {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public void setAddress(String address) {

    }

    @Override
    public void addEmployer(OrganizationInterface organization) {

    }

    @Override
    public Set<OrganizationInterface> getEmployers() {
        return null;
    }
}
