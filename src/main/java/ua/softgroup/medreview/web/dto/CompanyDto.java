
package ua.softgroup.medreview.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public class CompanyDto {
    @NotEmpty(message = "Name can not be empty")
    @Size(max = 64, message = "Name is too long (maximum is 64 characters)")
    private String companyName;

    public CompanyDto() {
    }

    public CompanyDto(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "CompanyDto{" +
                "companyName='" + companyName + '\'' +
                '}';
    }
}
