package com.online.assignment.camelbootservice2.models.bindy;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import static com.online.assignment.camelbootservice2.constants.UserConstants.*;
import com.online.assignment.camelbootservice2.models.User;

/**
 * Bindy Model class for writing data to CSV
 *
 * @author Ankit Kumar
 */
@CsvRecord(separator = CSV_FILE_SEPERATOR,
    generateHeaderColumns = false,
    skipFirstLine = true,
    quoting = true,
    crlf = "UNIX")
public class UserDataCsvRecords {

    @DataField(pos = 1, columnName = "User No.")
    private Integer userId;

    @DataField(pos = 2, columnName = "User Name")
    private String name;

    @DataField(pos = 3, columnName = "User Age")
    private Long age;

    @DataField(pos = 4, columnName = "Date of  Birth")
    private String dob;

    @DataField(pos = 5, columnName = "User Salary")
    private Double salary;

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Long getAge() {
        return age;
    }

    public String getDob() {
        return dob;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getSalary() {
        return salary;
    }

    public UserDataCsvRecords(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.age = user.getAge();
        this.salary = user.getSalary();
        this.dob = user.getDob();
    }

}
