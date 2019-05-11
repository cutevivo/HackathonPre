import java.util.LinkedList;
import java.util.List;

public class Manager extends Worker {
    private static final String ACCESS_DENIED = "Access denied!";
    private static final String EMPTY = "Empty";

    private List<Worker> workers;

    public Manager() {

    }

    //Manager类的初始化
    public Manager(String name, int age, int salary, String department) {
        super(name, age, salary, department);
        workers = new LinkedList<>();

    }

    // 管理人员可以查询本部门员工的基本信息，跨部门查询提示权限不足，提示“Access Denied!”
    public String inquire(Worker worker) throws IllegalArgumentException {
        if (department.equals(worker.getDepartment())){
            return worker.show();
        } else{
            throw new IllegalArgumentException(Manager.ACCESS_DENIED);
        }
    }

    // 管理人员给自己的队伍添加工作人员，同一部门的工作人员可以添加，并返回true，不同部门的工作人员无法添加，返回false
    public boolean lead(Worker worker) {
        if (department.equals(worker.getDepartment())){
            workers.add(worker);
            return true;
        }
        return false;
    }

    private static final String MANAGER_STATEMENT = "Statement for %s";
    private static final String WORKER_STATEMENT = "\n - %s";

    // 打印自己队伍的人员姓名，没有打印“Empty”
    public String print() {
        if(workers.isEmpty()) {
            return Manager.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Manager.MANAGER_STATEMENT, name));
        for(Worker worker : workers){
            sb.append(String.format(WORKER_STATEMENT, worker.getName()));
        }
        return sb.toString();
    }

}
