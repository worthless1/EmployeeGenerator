import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Employee {

    private String orgPersonId;
    private String lastName;
    private String firstName;
    private String midName;
    private String birthDate;
    private String gender;
    private String orgDeptId;
    private String orgPostId;
    private String empType;
    private String postType;
    private String postName;
    private String academicDegree;
    private String academicRank;
    private String educationType;

    public Employee() {
    }

    public Employee(int orgPersonId, String lastName, String firstName, String midName, String birthDate, String gender, String orgDeptId, String orgPostId, String empType, String postType, String postName, String academicDegree, String academicRank, String educationType) {
        this.orgPersonId = Integer.toString(orgPersonId);
        this.lastName = lastName;
        this.firstName = firstName;
        this.midName = midName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.orgDeptId = orgDeptId;
        this.orgPostId = orgPostId;
        this.empType = empType;
        this.postType = postType;
        this.postName = postName;
        this.academicDegree = academicDegree;
        this.academicRank = academicRank;
        this.educationType = educationType;
    }

    public String getOrgPersonId() {
        return orgPersonId;
    }

    public void setOrgPersonId(String orgPersonId) {
        this.orgPersonId = orgPersonId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrgDeptId() {
        return orgDeptId;
    }

    public void setOrgDeptId(String orgDeptId) {
        this.orgDeptId = orgDeptId;
    }

    public String getOrgPostId() {
        return orgPostId;
    }

    public void setOrgPostId(String orgPostId) {
        this.orgPostId = orgPostId;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public String getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(String academicRank) {
        this.academicRank = academicRank;
    }

    public String getEducationType() {
        return educationType;
    }

    public void setEducationType(String educationType) {
        this.educationType = educationType;
    }

    public List<String> getList() {
        return new ArrayList<>(Arrays.asList(orgPersonId, lastName, firstName, midName, birthDate,
                gender, orgDeptId, orgPostId, empType, postType, postName, academicDegree, academicRank, educationType));
    }

    @Override
    public String toString() {
        return orgPersonId + "," +
                lastName + "," +
                firstName + "," +
                midName + "," +
                birthDate + "," +
                gender + "," +
                orgDeptId + "," +
                orgPostId + "," +
                empType + "," +
                postType + "," +
                postName + "," +
                academicDegree + "," +
                academicRank;
    }
}