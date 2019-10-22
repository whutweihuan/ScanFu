package scanfu.com.bean;

public class DeptItem {
    int deptId;
    String name;

    public DeptItem() {
    }

    public DeptItem(int deptId, String name) {
        this.deptId = deptId;
        this.name = name;
    }

    public int getDeptId() {

        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
